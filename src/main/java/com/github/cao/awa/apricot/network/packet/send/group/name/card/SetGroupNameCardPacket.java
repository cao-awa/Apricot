package com.github.cao.awa.apricot.network.packet.send.group.name.card;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SetGroupNameCardPacket extends WritablePacket<NoResponsePacket> {
    private String cardName;
    private long groupId;
    private long userId;

    public SetGroupNameCardPacket(String cardName, long groupId, long userId) {
        this.cardName = cardName;
        this.groupId = groupId;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
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
                      "set_group_card"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "user_id",
                      this.userId
              )
              .fluentPut(
                      "card",
                      this.cardName
              );
    }

    @Override
    public boolean shouldEcho() {
        return false;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<NoResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(NoResponsePacket.NO_RESPONSE)
        );
    }
}
