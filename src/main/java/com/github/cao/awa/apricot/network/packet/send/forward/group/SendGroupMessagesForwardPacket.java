package com.github.cao.awa.apricot.network.packet.send.forward.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.*;
import java.util.function.*;

public class SendGroupMessagesForwardPacket extends WritablePacket<NoResponsePacket> {
    private List<ForwardMessage> messages;
    private long groupId;

    public SendGroupMessagesForwardPacket(List<ForwardMessage> messages, long groupId) {
        this.messages = messages;
        this.groupId = groupId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public List<ForwardMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<ForwardMessage> messages) {
        this.messages = messages;
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

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<NoResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(NoResponsePacket.NO_RESPONSE)
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
