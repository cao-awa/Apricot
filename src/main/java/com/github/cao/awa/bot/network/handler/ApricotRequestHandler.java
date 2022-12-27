package com.github.cao.awa.bot.network.handler;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.writer.*;
import com.github.cao.awa.bot.server.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.*;

public class ApricotRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger LOGGER = LogManager.getLogger("ApricotRequestHandler");
    private final ApricotServer server;
    private final ApricotProxy proxy;
    private PacketJSONBufWriter writer;
    private Channel channel;
    private int processed = 0;

    public ApricotRequestHandler(ApricotServer server) {
        this.server = server;
        this.proxy = new ApricotProxy(this);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        this.channel = context.channel();
        this.writer = new PacketJSONBufWriter(this.channel);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame textFrame) {
            JSONObject request = JSONObject.parseObject(textFrame.text());
            LOGGER.info(
                    "processing packets {}: {}",
                    ++ processed,
                    request.toString()
            );
            ReadonlyPacket packet = this.server.createPacket(request);
            if (packet != null) {
                packet.fireEvent(
                        this.server,
                        this.proxy
                );
            }
        }
    }

    public void send(Packet packet) {
        packet.writeAndFlush(this.writer);
    }
}