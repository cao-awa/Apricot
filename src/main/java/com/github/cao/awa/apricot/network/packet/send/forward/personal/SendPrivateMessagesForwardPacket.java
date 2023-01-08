package com.github.cao.awa.apricot.network.packet.send.forward.personal;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.*;
import java.util.function.*;

public class SendPrivateMessagesForwardPacket extends WritablePacket<NoResponsePacket> {
    private List<ForwardMessage> messages;
    private long userId;

    public SendPrivateMessagesForwardPacket(List<ForwardMessage> messages, long userId) {
        this.messages = messages;
        this.userId = userId;
    }

    public List<ForwardMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<ForwardMessage> messages) {
        this.messages = messages;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
                      "send_private_forward_msg"
              );
        writer.take("params")
              .fluentPut(
                      "user_id",
                      this.userId
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
