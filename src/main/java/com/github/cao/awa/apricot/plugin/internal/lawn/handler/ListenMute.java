package com.github.cao.awa.apricot.plugin.internal.lawn.handler;

import com.github.cao.awa.apricot.event.handler.mute.issue.*;
import com.github.cao.awa.apricot.event.receive.mute.issue.*;
import org.apache.logging.log4j.*;

public class ListenMute extends IssueGroupMuteEventHandler {
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
    public void onMute(IssueGroupMuteEvent<?> event) {
//        LOGGER.info("Mute type is '{}'", event.getClass().toString());
//        LOGGER.info("Time is {} seconds", event.getPacket().getDuration());
    }
}
