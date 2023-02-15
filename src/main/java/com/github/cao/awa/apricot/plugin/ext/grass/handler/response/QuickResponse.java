package com.github.cao.awa.apricot.plugin.ext.grass.handler.response;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.message.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class QuickResponse extends GroupMessageReceivedEventHandler {
    public static final String QUICK_RESPONSE = "QuickResponse";
    public static final String WORD_RESPONSE = "WordResponse";
    private static final Random RANDOM = new Random();

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
        ApricotProxy proxy = event.getProxy();

        // Do not response commands.
        if (MessageProcess.command(
                packet.getMessage(),
                "/"
        ) || MessageProcess.command(
                packet.getMessage(),
                "禁言我"
        )) {
            return;
        }

        if (MessageProcess.isAt(
                packet.getMessage(),
                packet.getBotId()
        )) {
            JSONArray responses = config(QUICK_RESPONSE).array("quick_responses");

            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    new AssembledMessage().participate(new TextMessageElement(EntrustEnvironment.select(
                            responses,
                            RANDOM
                    ))),
                    packet.getResponseId()
            ));
        } else if (MessageProcess.afterAt(
                packet.getMessage(),
                packet.getBotId()
        )) {
            String response = config(WORD_RESPONSE).map("word_responses")
                                                   .getString(MessageProcess.plain(packet.getMessage()));
            if (response == null) {
                //                proxy.send(new SendMessagePacket(
                //                        packet.getType(),
                //                        new AssembledMessage().participate(new TextMessageElement("我只是一只Bot，我看不懂")),
                //                        packet.getResponseId()
                //                ));
            } else {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new TextMessageElement(response)),
                        packet.getResponseId()
                ));
            }
        }
    }
}
