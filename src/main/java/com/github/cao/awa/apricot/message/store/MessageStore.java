package com.github.cao.awa.apricot.message.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.information.compressor.deflater.*;
import com.github.cao.awa.apricot.io.bytes.reader.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;

import java.io.*;
import java.nio.charset.*;

public class MessageStore {
    private AssembledMessage message;
    private long senderId;
    private long targetId;
    private int messageId;
    private long timestamp;
    private boolean recalled;

    public MessageStore(AssembledMessage message, long senderId, long targetId, int messageId, long timestamp, boolean recalled) {
        this.message = message;
        this.senderId = senderId;
        this.targetId = targetId;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.recalled = recalled;
    }

    public static MessageStore fromPacket(MessagePacket packet) {
        return new MessageStore(
                packet.getMessage(),
                packet.getSenderId(),
                packet.getResponseId(),
                packet.getMessageId(),
                packet.getTimestamp(),
                false
        );
    }

    public static MessageStore fromBin(ApricotServer server, byte[] bin) {
        BytesReader reader = new BytesReader(bin);
        int mark = reader.read();
        if (mark == '{') {
            return fromJSONObject(
                    server,
                    JSONObject.parse(new String(
                            bin,
                            StandardCharsets.UTF_8
                    ))
            );
        } else {
            boolean isDynamic = mark == 115;
            int sign = isDynamic ? reader.read() : 1;
            sign = sign == - 92 ? 420 : sign;
            int messageId = Base256.intFromBuf(reader.reverseRound(
                    4,
                    isDynamic ? sign % 3 == 0 ? reader.read() : 4 : 4
            ));
            long senderId = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 4 == 0 ? reader.read() : 8 : 8
            ));
            long timestamp = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 5 == 0 ? reader.read() : 8 : 8
            ));
            long targetId = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 7 == 0 ? reader.read() : 8 : 8
            ));

            int compound = reader.read();
            boolean isCompressed = compound % 10 == 1;
            boolean recalled = compound > 9;

            int length = Base256.tagFromBuf(reader.read(2));

            byte[] message = reader.read(length);
            if (isCompressed) {
                message = DeflaterCompressor.INSTANCE.decompress(message);
            }

            return new MessageStore(
                    MessageUtil.process(
                            server,
                            new String(
                                    message,
                                    StandardCharsets.UTF_8
                            )
                    ),
                    senderId,
                    targetId,
                    messageId,
                    timestamp,
                    recalled
            );
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static MessageStore fromJSONObject(ApricotServer server, JSONObject json) {
        return new MessageStore(
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.getLong("a"),
                json.getInteger("r"),
                json.getLong("t"),
                json.containsKey("c")
        );
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] toBin() {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            byte[] message = this.message.toPlainText()
                                         .getBytes(StandardCharsets.UTF_8);
            byte[] tryCompress = DeflaterCompressor.INSTANCE.compress(message);

            boolean compressed = false;

            if (message.length > tryCompress.length + 2) {
                message = tryCompress;
                compressed = true;
            }

            // The base sign.
            int mark = 114;

            // Use in dynamic encoding.
            Receptacle<Integer> sign = Receptacle.of(1);

            // Skip the message id.
            byte[] messageId = Base256.intToBuf(this.messageId);
            messageId = skip(
                    messageId,
                    sign,
                    4,
                    3
            );

            // Skip the sender id.
            byte[] senderId = Base256.longToBuf(this.senderId);
            senderId = skip(
                    senderId,
                    sign,
                    8,
                    4
            );

            // Skip the sender id.
            byte[] timestamp = Base256.longToBuf(this.timestamp);
            timestamp = skip(
                    timestamp,
                    sign,
                    8,
                    5
            );

            // Skip the target id.
            byte[] targetId = Base256.longToBuf(this.targetId);
            targetId = skip(
                    targetId,
                    sign,
                    8,
                    7
            );

            // Write binary sign mark.
            // Mark it to 114 then do not need to write sign.
            // Use two sign for smaller storing.
            if (sign.get() == 1) {
                // The numbers are not used dynamic encoding.
                bytes.write(mark);
            } else {
                // The numbers are used dynamic encoding.
                bytes.write(mark + 1);

                // Write dynamic encoding metadata.
                bytes.write(sign.get());
            }

            if (sign.get() % 3 == 0) {
                // Write message id length.
                bytes.write(messageId.length);
            }
            bytes.write(messageId);

            if (sign.get() % 4 == 0) {
                // Write sender id length.
                bytes.write(senderId.length);
            }
            bytes.write(senderId);

            if (sign.get() % 5 == 0) {
                // Write timestamp length.
                bytes.write(timestamp.length);
            }
            bytes.write(timestamp);

            if (sign.get() % 7 == 0) {
                // Write target id length.
                bytes.write(targetId.length);
            }
            bytes.write(targetId);

            int compound = 0;
            if (this.recalled) {
                compound += 10;
            }
            if (compressed) {
                compound += 1;
            }
            bytes.write(compound);
            bytes.write(Base256.tagToBuf(message.length));
            bytes.write(message);

            return bytes.toByteArray();
        } catch (Exception e) {
            return toJSONObject().toString()
                                 .getBytes(StandardCharsets.UTF_8);
        }
    }

    private static byte[] skip(byte[] source, Receptacle<Integer> sign, int length, int targetNumber) {
        BytesReader reader = new BytesReader(source);
        int cursor = reader.skip((byte) 0)
                           .getCursor();
        // It only makes sense in the context of cursor not 0.
        // The zero means unable to skip empty bytes.
        if (cursor > 1) {
            int messageIdLength = length - cursor;
            // Sign it to evenly divisible by target number.
            sign.set(sign.get() * targetNumber);
            // Let source skip empty bytes.
            source = reader.read(messageIdLength);
        }
        return source;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.fluentPut(
                    "m",
                    this.message.toPlainText()
            )
            .fluentPut(
                    "s",
                    this.senderId
            )
            .fluentPut(
                    "r",
                    this.messageId
            )
            .fluentPut(
                    "a",
                    this.targetId
            )
            .fluentPut(
                    "t",
                    this.timestamp
            );
        if (this.recalled) {
            json.fluentPut(
                    "c",
                    0
            );
        }
        return json;
    }

    public AssembledMessage getMessage() {
        return this.message;
    }

    public void setMessage(AssembledMessage message) {
        this.message = message;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isRecalled() {
        return this.recalled;
    }

    public void setRecalled(boolean recalled) {
        this.recalled = recalled;
    }
}
