package com.github.cao.awa.apricot.plugin.ext.grass.handler.wordle;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.image.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.digger.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.cao.awa.apricot.util.message.*;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.*;

import java.io.*;
import java.util.*;

public class WordleGame extends GroupMessageReceivedEventHandler {
    private static final Random RANDOM = new Random();
    private final Map<Long, Wordle> playingGroup = ApricotCollectionFactor.newConcurrentHashMap();
    private DB verify;

    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public synchronized void onMessageReceived(GroupMessageReceivedEvent<?> event) {
        GroupMessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();

        // Do not response commands.
        if (MessageProcess.plain(packet.getMessage())
                          .equals("/wordle")) {
            try {
                this.playingGroup.put(
                        packet.getGroupId(),
                        new Wordle("")
                );
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new ReplyMessageElement(packet.getMessageId()))
                                              .participate(new TextMessageElement("游戏开始啦！")),
                        packet.getResponseId()
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (MessageProcess.plain(packet.getMessage())
                                 .equals("/wordle stop")) {
            Wordle wordle = this.playingGroup.get(packet.getGroupId());
            if (wordle != null && ! wordle.getWord()
                                          .equals("")) {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new ReplyMessageElement(packet.getMessageId()))
                                              .participate(new TextMessageElement("不想玩啦？那就结束叭，答案是：" + wordle.getWord()
                                                                                                                        .toLowerCase())),
                        packet.getResponseId()
                ));
            } else {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new ReplyMessageElement(packet.getMessageId()))
                                              .participate(new TextMessageElement("还没有正在进行中的猜字谜哦")),
                        packet.getResponseId()
                ));
            }
            this.playingGroup.remove(packet.getGroupId());
        } else {
            String plain = MessageProcess.plain(packet.getMessage());
            if (plain.length() < 3 || plain.length() > 17) {
                return;
            }
            Wordle wordle = this.playingGroup.get(packet.getGroupId());
            if (wordle != null) {
                try {
                    if (! plain.matches("^[a-zA-Z]*")) {
                        return;
                    }
                    if (wordle.getWord()
                              .equals("")) {
                        wordle = new Wordle(randomWord(plain.length()));
                        this.playingGroup.put(
                                packet.getGroupId(),
                                wordle
                        );
                    }
                    if (wordle.getWord()
                              .length() != plain.length()) {
                        return;
                    }
                    if (this.verify.get(plain.getBytes()) == null) {
                        proxy.send(new SendMessagePacket(
                                packet.getType(),
                                new AssembledMessage().participate(new ReplyMessageElement(packet.getMessageId()))
                                                      .participate(new TextMessageElement(plain + "不在词库里，试试其他的单词吧")),
                                packet.getResponseId()
                        ));
                        return;
                    }
                    if (wordle.draw(plain)) {
                        proxy.send(new SendMessagePacket(
                                packet.getType(),
                                new AssembledMessage().participate(new ImageMessageElement(
                                        MessageDigger.digestFile(
                                                wordle.current(),
                                                MessageDigger.Sha3.SHA_224
                                        ),
                                        wordle.current()
                                              .toURI()
                                              .toURL()
                                              .toString(),
                                        ""
                                )),
                                packet.getResponseId()
                        ));
                        if (wordle.isDone()) {
                            proxy.send(new SendMessagePacket(
                                    packet.getType(),
                                    new AssembledMessage().participate(new ReplyMessageElement(packet.getMessageId()))
                                                          .participate(new TextMessageElement("猜对啦，答案是" + wordle.getWord()
                                                                                                                      .toLowerCase() + "！所以..." + wordle.getWord()
                                                                                                                                                          .toLowerCase() + "是什么呢？好吃吗？")),
                                    packet.getResponseId()
                            ));
                            this.playingGroup.remove(packet.getGroupId());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String randomWord(int length) throws IOException {
        RandomAccessFile file = new RandomAccessFile(
                getFile(length),
                "r"
        );
        long target = RANDOM.nextLong(file.length() / length);
        file.seek(target * length);
        byte[] bytes = new byte[length];
        file.read(bytes);
        return new String(bytes);
    }

    private File getFile(int index) {
        return new File("configs/plugins/ext/Grass/Wordle/Words/" + index + ".lang");
    }

    @Override
    public void reload() {
        // should not be load
    }
}
