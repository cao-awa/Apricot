package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.mute.lift.*;
import com.github.cao.awa.apricot.event.receive.accomplish.mute.lift.*;
import org.apache.logging.log4j.*;

public class ListenLiftMute extends LiftGroupMuteEventHandler {
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
    public void onLift(LiftGroupMuteEvent<?> event) {
//        LOGGER.info("Lifted mute type is '{}'", event.getClass().toString());
    }
}