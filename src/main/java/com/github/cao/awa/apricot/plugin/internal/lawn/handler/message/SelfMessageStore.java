package com.github.cao.awa.apricot.plugin.internal.lawn.handler.message;

import com.github.cao.awa.apricot.event.handler.message.sent.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.element.cq.image.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.task.intensive.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;

public class SelfMessageStore extends MessageSentEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("MessageStorage");

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(MessageSentEvent<?> event) {
        MessageSentPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        MessageStore store = MessageStore.fromPacket(packet);

        server.getMessagesHeadOffice()
              .set(
                      packet.getOwnId(),
                      store
              );

        server.getRelationalDatabase(
                      packet.getBotId(),
                      packet.getResponseId()
              )
              .append(packet.getOwnId());

        packet.getMessage()
              .forEach(message -> {
                  if (message instanceof ImageMessageElement image) {
                      EntrustEnvironment.trys(
                              () -> {
                                  server.download(
                                          image.getFile(),
                                          image.getUrl()
                                  );
                              },
                              Throwable::printStackTrace
                      );
                  }
              });
    }

    /**
     * The messages store service is intensive IO.
     *
     * @return Intensive
     */
    @Override
    public IntensiveType intensive() {
        return IntensiveType.IO;
    }
}
