package com.github.cao.awa.apricot.network.packet.send.group.mute.personal.anonymous;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendGroupMuteAnonymousPacket extends WritablePacket<NoResponsePacket> {
    private long groupId;
    private String flag;
    private int duration;

    public SendGroupMuteAnonymousPacket(long groupId, String flag, int duration) {
        this.groupId = groupId;
        this.flag = flag;
        this.duration = duration;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFlag() {
        return this.flag;
    }

    public int getDuration() {
        return this.duration;
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
                      "set_group_anonymous_ban"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "flag",
                      this.flag
              )
              .fluentPut(
                      "duration",
                      this.duration
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
