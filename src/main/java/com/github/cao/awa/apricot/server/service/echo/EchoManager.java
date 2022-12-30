package com.github.cao.awa.apricot.server.service.echo;

import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.utils.collection.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class EchoManager implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("EchoManager");
    private final Map<String, Consumer<EchoResultPacket>> echos = ApricotCollectionFactor.newHashMap();
    private final Executor executor;
    private boolean active = true;

    public EchoManager(Executor executor) {
        this.executor = executor;
    }

    public synchronized void echo(@Nullable String identifier, @NotNull Consumer<EchoResultPacket> action) {
        if (this.active) {
            if (identifier == null) {
                return;
            }
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

    public synchronized void echo(@Nullable String identifier, @NotNull EchoResultPacket packet) {
        if (this.active) {
            if (identifier == null) {
                return;
            }
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
