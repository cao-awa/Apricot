package com.github.cao.awa.apricot.message.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.information.compressor.deflater.*;
import com.github.cao.awa.apricot.io.bytes.reader.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;

import java.io.*;
import java.nio.charset.*;

public class MessageStore {
    private AssembledMessage message;
    private long senderId;
    private long targetId;
    private int messageId;
    private boolean recalled;

    public MessageStore(AssembledMessage message, long senderId, long targetId, int messageId, boolean recalled) {
        this.message = message;
        this.senderId = senderId;
        this.targetId = targetId;
        this.messageId = messageId;
        this.recalled = recalled;
    }

    public static MessageStore fromPacket(MessagePacket packet) {
        return new MessageStore(
                packet.getMessage(),
                packet.getSenderId(),
                packet.getResponseId(),
                packet.getMessageId(),
                false
        );
    }

    public static MessageStore fromBin(ApricotServer server, byte[] bin) {
        BytesReader reader = new BytesReader(bin);
        int sign = reader.read();
        if (sign == 114) {
            int messageIdLength = reader.read();
            int messageId = Base256.intFromBuf(reader.read(messageIdLength));
            long senderId = Base256.longFromBuf(reader.read(8));
            long targetId = Base256.longFromBuf(reader.read(8));

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
                    recalled
            );
        } else {
            if (sign == '{') {
                return fromJSONObject0(
                        server,
                        JSONObject.parse(new String(
                                bin,
                                StandardCharsets.UTF_8
                        ))
                );
            }
            throw new IllegalArgumentException();
        }
    }

    public static MessageStore fromJSONObject0(ApricotServer server, JSONObject json) {
        return new MessageStore(
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.getLong("a"),
                json.getInteger("r"),
                json.containsKey("c")
        );
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

            // Binary sign
            bytes.write(114);
            // Message id length
            bytes.write(4);
            bytes.write(Base256.intToBuf(this.messageId));
            bytes.write(Base256.longToBuf(this.senderId));
            bytes.write(Base256.longToBuf(this.targetId));
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

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.fluentPut(
                "m",
                this.message.toPlainText()
        );
        json.fluentPut(
                "s",
                this.senderId
        );
        json.fluentPut(
                "r",
                this.messageId
        );
        json.fluentPut(
                "a",
                this.targetId
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
