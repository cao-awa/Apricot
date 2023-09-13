package com.github.cao.awa.apricot.message.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.information.compressor.deflater.*;
import com.github.cao.awa.apricot.io.bytes.reader.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;

import java.io.*;
import java.nio.charset.*;

@Stable
public class MessageStore {
    private MessageType type;
    private AssembledMessage message;
    private long senderId;
    private long targetId;
    private long messageId;
    private long timestamp;
    private boolean recalled;

    public MessageStore(MessageType type, AssembledMessage message, long senderId, long targetId, long messageId, long timestamp, boolean recalled) {
        this.type = type;
        this.message = message;
        this.senderId = senderId;
        this.targetId = targetId;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.recalled = recalled;
    }

    public static MessageStore fromPacket(MessagePacket packet) {
        return new MessageStore(
                packet.getType(),
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
            int sign = (isDynamic ? reader.read() : 1);
            // Let -92 be all compatible, can be 0 or 420.
            if (sign == - 92) {
                sign = 0;
            }
            if (sign < 0) {
                // The 256 is range of byte.
                sign = 256 + sign;
            }
            // Read message id.
            long messageId = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 3 == 0 ? reader.read() : 8 : 8
            ));
            // Read sender id.
            long senderId = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 4 == 0 ? reader.read() : 8 : 8
            ));
            // Read timestamp.
            long timestamp = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 5 == 0 ? reader.read() : 8 : 8
            ));
            // Read target id.
            long targetId = Base256.longFromBuf(reader.reverseRound(
                    8,
                    isDynamic ? sign % 7 == 0 ? reader.read() : 8 : 8
            ));

            // Read compound.
            int compound = reader.read();
            if (compound < 0) {
                // The 256 is range of byte.
                compound = 256 + compound;
            }

            // Handle compound.
            // Compound can mod 9 mean the message is sent from guild.
            // Compound can mod 7 mean the message is sent from group.
            // Else then mean the message is sent from private.
            MessageType type = compound % 9 == 0 ?
                               MessageType.GUILD :
                               compound % 7 == 0 ? MessageType.GROUP : MessageType.PRIVATE;
            // Compound can mod 3 mean the message is recalled.
            boolean recalled = compound % 3 == 0;
            // Compound can mod 5 mean the message is compressed.
            boolean isCompressed = compound % 5 == 0;

            // Read message length, for next step to read message.
            int length = Base256.tagFromBuf(reader.read(2));

            // Read message.
            byte[] message = reader.read(length);

            // Decompress if the message is compressed.
            if (isCompressed) {
                message = DeflaterCompressor.INSTANCE.decompress(message);
            }

            // Make the message store.
            return new MessageStore(
                    type,
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

    public static MessageStore fromJSONObject(ApricotServer server, JSONObject json) {
        return new MessageStore(
                MessageType.of(json.getString("p")),
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.getLong("a"),
                json.getLong("r"),
                json.getLong("t"),
                json.containsKey("c")
        );
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public byte[] toBin() {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            byte[] message = this.message.toPlainText()
                                         .getBytes(StandardCharsets.UTF_8);

            // Try to compress the message.
            byte[] tryCompress = DeflaterCompressor.INSTANCE.compress(message);

            boolean compressed = false;

            // Only save compress result if success reduced the size.
            if (message.length > tryCompress.length + 2) {
                message = tryCompress;
                compressed = true;
            }

            // The base sign.
            int mark = 114;

            // Use in dynamic encoding.
            Receptacle<Integer> sign = Receptacle.of(1);

            // Skip the message id.
            byte[] messageId = Base256.longToBuf(this.messageId);
            messageId = skip(
                    messageId,
                    sign,
                    3
            );

            // Skip the sender id.
            byte[] senderId = Base256.longToBuf(this.senderId);
            senderId = skip(
                    senderId,
                    sign,
                    4
            );

            // Skip the sender id.
            byte[] timestamp = Base256.longToBuf(this.timestamp);
            timestamp = skip(
                    timestamp,
                    sign,
                    5
            );

            // Skip the target id.
            byte[] targetId = Base256.longToBuf(this.targetId);
            targetId = skip(
                    targetId,
                    sign,
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

            int compound = 1;
            // Handle recall mark.
            if (this.recalled) {
                // Sign it to evenly divisible by 3.
                compound *= 3;
            }

            // Handle compress mark.
            if (compressed) {
                // Sign it to evenly divisible by 5.
                compound *= 5;
            }

            // Handle message type mark.
            // Sign it to evenly divisible by target 3 or 5 to mark it is group or guild.
            // Else then mean it is private type.
            if (this.type == MessageType.GROUP) {
                // Sign it to evenly divisible by 7.
                compound *= 7;
            } else if (this.type == MessageType.GUILD) {
                // Sign it to evenly divisible by 9.
                compound *= 9;
            }

            // Write compound.
            bytes.write(compound);

            // Write message length mark byte.
            bytes.write(Base256.tagToBuf(message.length));

            // Write message.
            bytes.write(message);

            return bytes.toByteArray();
        } catch (Exception e) {
            return toJSONObject().toString()
                                 .getBytes(StandardCharsets.UTF_8);
        }
    }

    private static byte[] skip(byte[] source, Receptacle<Integer> sign, int targetNumber) {
        BytesReader reader = new BytesReader(source);
        // Skip, and get the cursor.
        int cursor = reader.next((byte) 0)
                           .getCursor();
        // It only makes sense in the context of cursor not 0.
        // The zero means unable to skip empty bytes.
        if (cursor > 1) {
            int messageIdLength = source.length - cursor;
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
            )
            .fluentPut(
                    "p",
                    this.type
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

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public boolean isRecalled() {
        return this.recalled;
    }

    public void setRecalled(boolean recalled) {
        this.recalled = recalled;
    }
}
