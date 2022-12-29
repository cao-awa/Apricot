package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.immigration.illegal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.immigration.illegal.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import org.apache.logging.log4j.*;

public class InternalIllegalImmigrationHandler extends IllegalImmigrationHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalIllegalImmigrationHandler");

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
    public void onIllegalImmigration(IllegalImmigrationEvent event) {
        Packet packet = event.getIllegalEvent().getPacket();
        if (packet instanceof MessageReceivedPacket messageReceived) {
            LOGGER.info("Illegal message, will not be process: {}", messageReceived.getMessage().toPlainText());
        }
    }
}
