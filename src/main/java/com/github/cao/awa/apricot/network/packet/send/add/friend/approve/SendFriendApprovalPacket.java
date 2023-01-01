package com.github.cao.awa.apricot.network.packet.send.add.friend.approve;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

public class SendFriendApprovalPacket extends WritablePacket {
    private String flag;
    private boolean approve;
    private @NotNull String remark;

    public SendFriendApprovalPacket(String flag, boolean approve, @Nullable String remark) {
        this.flag = flag;
        this.approve = approve;
        this.remark = remark == null ? "" : remark;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public @NotNull String getRemark() {
        return remark;
    }

    public void setRemark(@NotNull String remark) {
        this.remark = remark;
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
                      "set_friend_add_request"
              );
        writer.take("params")
              .fluentPut(
                      "approve",
                      this.approve
              )
              .fluentPut(
                      "flag",
                      this.flag
              )
              .fluentPut(
                      "remark",
                      this.remark
              );
    }

    @Override
    public boolean shouldEcho() {
        return false;
    }
}
