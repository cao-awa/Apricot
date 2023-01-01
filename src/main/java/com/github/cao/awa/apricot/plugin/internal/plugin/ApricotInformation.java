package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.times.*;

public class ApricotInformation extends MessageReceivedEventHandler {
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
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();

        if (packet.getMessage()
                  .toPlainText()
                  .equals(".apricot")) {
            StringBuilder builder = new StringBuilder();
            builder.append("Apricot v")
                   .append(ApricotServer.VERSION)
                   .append("\n");
            builder.append("Plugins: \n");
            proxy.getServer()
                 .getPlugins()
                 .forEach(plugin -> {
                     builder.append(plugin.getName())
                            .append(" ")
                            .append(plugin.version())
                            .append("\n");
                 });
            long millions = TimeUtil.processMillion(proxy.getServer()
                                                         .getStartupTime());
            long seconds = (millions / 1000) % 60;
            long minutes = (millions / 1000 / 60) % 60;
            long hours = (millions / 1000 / 60 / 60) % 60;
            long days = millions / 1000 / 60 / 60 / 24;
            builder.append("Uptime: ");
            appendNoZero(builder,
                         days,
                         "d,"
            );
            appendNoZero(builder,
                         hours,
                         "h,"
            );
            appendNoZero(builder,
                         minutes,
                         "m,"
            );
            appendNoZero(builder,
                         seconds,
                         "s"
            );


            //            builder.append("Uptime: ").append();

            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    new TextMessageElement(builder.toString()).toMessage(),
                    packet.getResponseId(),
                    packet.getResponseId()
            ));
        }
    }

    private void appendNoZero(StringBuilder builder, long num, String suffix) {
        if (num == 0) {
            return;
        }
        builder
               .append(num)
               .append(suffix);
    }
}