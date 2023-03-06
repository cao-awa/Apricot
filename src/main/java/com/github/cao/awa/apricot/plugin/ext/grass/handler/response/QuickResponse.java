package com.github.cao.awa.apricot.plugin.ext.grass.handler.response;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.assemble.AssembledMessage;
import com.github.cao.awa.apricot.message.element.plain.text.TextMessageElement;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.GroupMessageReceivedPacket;
import com.github.cao.awa.apricot.network.packet.send.message.SendMessagePacket;
import com.github.cao.awa.apricot.network.router.ApricotProxy;

public class QuickResponse extends GroupMessageReceivedEventHandler {
    @Override
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {
//        GroupMessageReceivedPacket packet = event.getPacket();
//        ApricotProxy proxy = event.getProxy();
//
//        proxy.send(new SendMessagePacket(
//                packet.getType(),
//                new AssembledMessage().participate(new TextMessageElement("www")),
//                packet.getResponseId()
//        ));
    }
}
