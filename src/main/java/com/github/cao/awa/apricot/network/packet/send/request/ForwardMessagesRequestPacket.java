package com.github.cao.awa.apricot.network.packet.send.request;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

import java.util.function.*;

@Deprecated
@Unsupported
public class ForwardMessagesRequestPacket extends WritablePacket {
    private String messageId;

    public ForwardMessagesRequestPacket(String messageId) {
        this.messageId = messageId;
    }

    public void compoundId(Function<String, String> function) {
        setMessageId(function.apply(getMessageId()));
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .fluentPut(
                      "action",
                      "get_forward_msg"
              )
              .fluentPut(
                      "prams",
                      new JSONObject().fluentPut(
                              "message_id",
                              this.messageId
                      )
              );
    }
}
