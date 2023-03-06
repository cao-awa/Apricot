package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.learn;

import com.github.cao.awa.apricot.config.plugin.handler.ApsConfig;
import com.github.cao.awa.apricot.event.handler.message.received.MessageReceivedEventHandler;
import com.github.cao.awa.apricot.event.receive.message.MessageReceivedEvent;
import com.github.cao.awa.apricot.network.packet.receive.message.MessageReceivedPacket;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.HanasuHandler;
import com.github.cao.awa.apricot.util.message.MessageProcess;

public class HanasuLearn extends MessageReceivedEventHandler {
    @Override
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        MessageReceivedPacket packet = event.getPacket();

        ApsConfig learnConfig = config("HanasuLearn");

        if (learnConfig.array("targets",
                              Long.class
                       )
                       .contains(packet.getResponseId()) && (! learnConfig.array("excludes",
                                                                                 Long.class
                                                                          )
                                                                          .contains(packet.getSenderId()) && ! learnConfig.array("excludes",
                                                                                                                                 Long.class
                                                                                                                          )
                                                                                                                          .contains(packet.getResponseId()))) {
            HanasuHandler.MODEL.model(MessageProcess.plain(packet.getMessage()));
        }
    }
}
