package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler;

import com.github.cao.awa.apricot.event.handler.message.received.group.GroupMessageReceivedEventHandler;
import com.github.cao.awa.apricot.event.receive.message.group.received.GroupMessageReceivedEvent;
import com.github.cao.awa.apricot.message.assemble.AssembledMessage;
import com.github.cao.awa.apricot.message.element.plain.text.TextMessageElement;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.GroupMessageReceivedPacket;
import com.github.cao.awa.apricot.network.packet.send.message.SendMessagePacket;
import com.github.cao.awa.apricot.network.router.ApricotProxy;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.HanasuModel;
import com.github.cao.awa.apricot.util.message.MessageProcess;

public class HanasuHandler extends GroupMessageReceivedEventHandler {
    public static final HanasuModel MODEL = new HanasuModel("test");

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
            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    new AssembledMessage().participate(new TextMessageElement(
                            text("")
                    )),
                    packet.getResponseId()
            ));
        } else if (MessageProcess.afterAt(
                packet.getMessage(),
                packet.getBotId()
        )) {
            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    new AssembledMessage().participate(new TextMessageElement(
                            text(MessageProcess.plain(packet.getMessage()))
                    )),
                    packet.getResponseId()
            ));
        }
    }

    private String text(String preGen) {
        String result = MODEL.text(
                preGen
        );
        int unableToText = 10;
        while (result == null || result.equals("")) {
            result = MODEL.text(preGen);;
            if (unableToText-- == 0) {
                break;
            }
        }
        return result;
    }

    @Override
    public void reload() {

    }
}
