package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.resources.loader.*;
import com.github.cao.awa.apricot.utils.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import it.unimi.dsi.fastutil.objects.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class InternalMessageReceivedHandler extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalMessageHandler");

    private static final Map<String, String> replay = EntrustEnvironment.operation(
            new Object2ObjectOpenHashMap<>(),
            map -> {
                JSONObject json = JSONObject.parseObject(IOUtil.read(ResourcesLoader.getResource("kv.json")));
                json.forEach((k, v) -> {
                    map.put(
                            k,
                            v.toString()
                    );
                });
            }
    );

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
    public void onMessageReceived(MessageReceivedEvent event) {
        LOGGER.info(event.getPacket()
                         .getSender()
                         .getName() + ": " + event.getPacket()
                                                  .getMessage()
                                                  .toString());
        if (event.getPacket()
                 .getType() == SendMessageType.PRIVATE && event.getPacket()
                                                               .getMessage()
                                                               .handleMessage(
                                                                       element -> {
                                                                           if (element instanceof TextMessageElement text) {
                                                                               return replay.containsKey(text.getText());
                                                                           }
                                                                           return false;
                                                                       },
                                                                       0
                                                               )) {
            LOGGER.info(
                    "Handling 'awa' from {}, responding 'awa'",
                    event.getPacket()
                         .getSenderId()
            );
            event.getProxy()
                 .send(
                         new SendMessagePacket(
                                 SendMessageType.PRIVATE,
                                 replay.get(event.getPacket()
                                                 .getMessage()
                                                 .get(
                                                         0,
                                                         TextMessageElement.class
                                                 )
                                                 .getText()),
                                 event.getPacket()
                                      .getResponseId()
                         ),
                         echo -> {
                             System.out.println(echo.getIdentifier());
                         }
                 );
        }
    }
}
