package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.name.title.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.title.*;
import org.apache.logging.log4j.*;

public class TappingTitleChanges extends GroupTitleChangedReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalPrivate");

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
    public void onChanged(GroupTitleChangedReceivedEvent event) {
        LOGGER.info("Name title of '{}' has been changed to '{}'",
                    event.getPacket()
                         .getUserId(),
                    event.getPacket()
                         .getTitle()
        );
    }
}
