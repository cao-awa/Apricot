package com.github.cao.awa.apricot.plugin.ext.lawn.handler.media;

import com.github.cao.awa.apricot.event.handler.message.MessageEventHandler;
import com.github.cao.awa.apricot.event.receive.message.MessageEvent;
import com.github.cao.awa.apricot.message.element.cq.image.ImageMessageElement;
import com.github.cao.awa.apricot.network.packet.receive.message.MessagePacket;
import com.github.cao.awa.apricot.server.ApricotServer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

public class MediaDownload extends MessageEventHandler {
    @Override
    public void onMessage(MessageEvent<?> event) {
        MessagePacket packet = event.getPacket();
        ApricotServer server = event.getProxy().server();

        packet.getMessage()
              .forEach(message -> {
                  if (message instanceof ImageMessageElement image) {
                      EntrustEnvironment.trys(
                              () -> {
                                  server.download(
                                          image.getFile()
                                               .getName(),
                                          image.getUrl()
                                  );
                              },
                              Throwable :: printStackTrace
                      );
                  }
              });
    }
}
