package com.github.cao.awa.apricot.network.packet.send.group.mute;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.send.group.mute.personal.anonymous.*;
import com.github.cao.awa.apricot.network.packet.send.group.mute.personal.normal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendGroupMutePacket extends WritablePacket<NoResponsePacket> {
    private final long groupId;
    private final long userId;
    private final String flag;
    private final int duration;

    public SendGroupMutePacket(long groupId, long userId, String flag, int duration) {
        this.groupId = groupId;
        this.userId = userId;
        this.flag = flag;
        this.duration = duration;
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
        if (this.flag != null) {
            new SendGroupMuteAnonymousPacket(
                    this.groupId,
                    this.flag,
                    this.duration
            ).write(writer);
        } else {
            new SendGroupMuteNormalPacket(
                    this.groupId,
                    this.userId,
                    this.duration
            ).write(writer);
        }
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
