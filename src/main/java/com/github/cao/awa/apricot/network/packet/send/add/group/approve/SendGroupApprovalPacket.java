package com.github.cao.awa.apricot.network.packet.send.add.group.approve;

import com.github.cao.awa.apricot.approval.group.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

public class SendGroupApprovalPacket extends WritablePacket {
    private ApprovalType type;
    private String flag;
    private boolean approve;
    private @NotNull String reason;

    public SendGroupApprovalPacket(ApprovalType type, String flag, boolean approve, @Nullable String reason) {
        this.type = type;
        this.flag = flag;
        this.approve = approve;
        this.reason = reason == null ? "" : reason;
    }

    public ApprovalType getType() {
        return type;
    }

    public void setType(ApprovalType type) {
        this.type = type;
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

    public @NotNull String getReason() {
        return reason;
    }

    public void setReason(@NotNull String reason) {
        this.reason = reason;
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
                      "set_group_add_request"
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
                      "reason",
                      this.reason
              )
              .fluentPut(
                      "type",
                      this.type.getName()
              );
    }

    @Override
    public boolean shouldEcho() {
        return false;
    }
}
