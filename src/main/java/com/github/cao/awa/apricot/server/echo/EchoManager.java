package com.github.cao.awa.apricot.server.echo;

import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import it.unimi.dsi.fastutil.objects.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class EchoManager {
    private static final Logger LOGGER = LogManager.getLogger("EchoManager");
    private final Map<String, Consumer<EchoResultPacket>> echos = new Object2ObjectOpenHashMap<>();
    private final Executor executor;

    public EchoManager(Executor executor) {
        this.executor = executor;
    }

    public void echo(String identifier, Consumer<EchoResultPacket> action) {
        if (this.echos.containsKey(identifier)) {
            LOGGER.warn("Duplicated identifier, it repeated echo or real duplicate?");
        } else {
            this.echos.put(
                    identifier,
                    action
            );
        }
    }

    public void echo(String identifier, EchoResultPacket packet) {
        if (this.echos.containsKey(identifier)) {
            this.executor.execute(() -> this.echos.get(identifier)
                                                  .accept(packet));
            this.echos.remove(identifier);
        } else {
            LOGGER.warn("Echo identifier not found, ignored this echo");
        }
    }
}
