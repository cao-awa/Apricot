package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.element.cq.element.image.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.store.*;
import com.github.cao.awa.apricot.utils.file.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

public class OtherMessageStorage extends MessageReceivedEventHandler {
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
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        System.out.println("O");
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        MessageStore store = MessageStore.fromPacket(packet);

        server.getMessagesHeadOffice()
              .set(
                      packet.getOwnId(),
                      store
              );

        server.getRelationalDatabase(packet.getBotId(), packet.getResponseId())
              .append(packet.getOwnId());

        packet.getMessage()
              .forEach(message -> {
                  if (message instanceof ImageMessageElement image) {
                      EntrustEnvironment.trys(
                              () -> {
                                  downloadImg(
                                          image.getUrl(),
                                          image.getFile(),
                                          "databases/images"
                                  );
                              },
                              Throwable::printStackTrace
                      );
                  }
              });
    }

    public static void downloadImg(String url, String fileName, String savePath) {
        File saveDir = new File(savePath);
        FileUtil.mkdirs(saveDir);
        File saveFile = new File(saveDir + "/" + fileName);
        if (saveFile.isFile()) {
            return;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
            );

            InputStream input = connection.getInputStream();

            FileOutputStream output = new FileOutputStream(saveFile);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) != - 1) {
                output.write(
                        buffer,
                        0,
                        length
                );
            }
            output.close();
            input.close();
        } catch (Exception ignored) {
            saveFile.delete();
        } finally {
        }
    }
}
