package com.github.cao.awa.apricot.network.packet.send.message.forward.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

import java.util.*;
import java.util.function.*;

public class SendGroupMessagesForwardPacket extends WritablePacket {
    private List<ForwardMessage> messages;
    private long groupId;

    public SendGroupMessagesForwardPacket(List<ForwardMessage> messages, long groupId) {
        this.messages = messages;
        this.groupId = groupId;
    }

    public void compoundUserId(Function<Long, Long> function) {
        setGroupId(function.apply(getGroupId()));
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void compoundMessages(Function<List<ForwardMessage>, List<ForwardMessage>> function) {
        setMessages(function.apply(getMessages()));
    }

    public List<ForwardMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<ForwardMessage> messages) {
        this.messages = messages;
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    /**
     * Write data to buffer.
     *
     * @param writer
     *         writer
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .fluentPut(
                      "action",
                      "send_group_forward_msg"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "messages",
                      messagesToArray()
              );
    }

    public JSONArray messagesToArray() {
        JSONArray array = new JSONArray();
        for (ForwardMessage message : this.messages) {
            JSONObject json = new JSONObject();
            message.write(json);
            array.add(json);
        }
        return array;
    }
}
