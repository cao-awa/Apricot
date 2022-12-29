package com.github.cao.awa.apricot.server.service.echo;

import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.service.*;
import it.unimi.dsi.fastutil.objects.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class EchoManager implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("EchoManager");
    private final Map<String, Consumer<EchoResultPacket>> echos = new Object2ObjectOpenHashMap<>();
    private final Executor executor;
    private boolean active = true;

    public EchoManager(Executor executor) {
        this.executor = executor;
    }

    public void echo(String identifier, Consumer<EchoResultPacket> action) {
        if (this.active) {
            if (this.echos.containsKey(identifier)) {
                LOGGER.warn("Duplicated identifier, it repeated echo or real duplicate?");
            } else {
                this.echos.put(
                        identifier,
                        action
                );
            }
        }
    }

    public void echo(String identifier, EchoResultPacket packet) {
        if (this.active) {
            if (this.echos.containsKey(identifier)) {
                Consumer<EchoResultPacket> action = this.echos.get(identifier);
                this.executor.execute(() -> action.accept(packet));
                this.echos.remove(identifier);
            } else {
                LOGGER.warn("Echo identifier not found, ignored this echo");
            }
        }
    }

    @Override
    public void shutdown() {
        this.active = false;
        if (this.executor instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }
}
