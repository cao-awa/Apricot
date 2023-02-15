package com.github.cao.awa.apricot.plugin.ext.example.forward;

import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.carve.*;
import com.github.cao.awa.apricot.message.element.cq.forward.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.get.*;
import com.github.cao.awa.apricot.network.router.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForwardGetTest extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("Forwarder");
    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();

        AssembledMessage message = packet.getMessage();

        CarvedMessage<ForwardMessageElement> forward = message.carver(ForwardMessageElement.class);

        if (forward.size() > 0) {
            proxy.send(new GetForwardMessagePacket(forward.get(0)
                                                          .getId()), response -> {
                response.getMessages().forEach(node -> {
                    node.ifMessage(msg -> {
                        LOGGER.info(msg.getMessage().toPlainText() + ":awa");
                    });

                    node.forEach(inner -> {
                        System.out.println("Next");
                    },msg -> {
                        LOGGER.info(msg.getMessage().toPlainText());
                    });
                });
                System.out.println(response.getIdentifier());
            });
        }
    }
}
