package com.github.cao.awa.apricot.plugin.ext.grass.handler.hitokoto;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.http.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.message.*;

public class Hitokoto extends GroupMessageReceivedEventHandler {
    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {
        GroupMessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.proxy();

        // Do not response commands.
        if (MessageProcess.command(
                packet.getMessage(),
                "一言"
        )) {
            if (MessageProcess.hasAt(
                    packet.getMessage(),
                    packet.getBotId()
            )) {
                JSONObject json = ApricotHttpUtil.get("https://v1.hitokoto.cn/")
                                                 .json();

                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        AssembledMessage.of()
                                        .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                        .participate(TextMessageElement.text(json.getString("hitokoto") + "\n--" + json.getString("from"))),
                        packet.getResponseId()
                ));
            }
        }
    }

    @Override
    public void onException(Exception exception) {
        exception.printStackTrace();
    }
}
