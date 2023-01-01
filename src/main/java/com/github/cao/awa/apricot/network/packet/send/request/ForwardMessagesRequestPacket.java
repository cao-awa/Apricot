package com.github.cao.awa.apricot.network.packet.send.request;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

@Deprecated
@Unsupported
public class ForwardMessagesRequestPacket extends WritablePacket {
    private long messageId;

    public ForwardMessagesRequestPacket(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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

    @Override
    public boolean shouldEcho() {
        return true;
    }
}
