package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.add.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.add.group.*;
import org.apache.logging.log4j.*;

public class ListenAddGroup extends AddGroupEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("TappingMute");

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
    public void onAdd(AddGroupEvent event) {
//        LOGGER.info("User '{}' try add group, reason is '{}'", event.getPacket().getUserId(), event.getPacket().getComment());
    }
}
