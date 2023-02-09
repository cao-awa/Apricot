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
     * @param event
     *         event
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
        } else {
            String plain = MessageProcess.plain(packet.getMessage());
            if (plain.length() < 3 || plain.length() > 17) {
                return;
            }
            Wordle wordle = this.playingGroup.get(packet.getGroupId());
            if (wordle != null) {
                try {
                    if (wordle.getWord()
                              .equals("")) {
                        wordle = new Wordle(randomWord(plain.length()));
                        this.playingGroup.put(
                                packet.getGroupId(),
                                wordle
                        );
                        System.out.println(wordle.getWord());
                    }
                    if (wordle.getWord()
                              .length() != plain.length()) {
                        return;
                    }
                    if (verify.get(plain.getBytes()) == null) {
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
        for (int i = 3; i < 18; i++) {
            try {
                File file = getFile(i);
                FileUtil.mkdirsParent(file);
                file.createNewFile();
                IOUtil.write(
                        new FileOutputStream(file),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/words_" + i + ".lang")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (! new File("configs/plugins/ext/Grass/Wordle/Words/verify/LOCK").isFile()) {
                FileUtil.mkdirs(new File("configs/plugins/ext/Grass/Wordle/Words/verify/"));
                IOUtil.write(
                        new FileOutputStream("configs/plugins/ext/Grass/Wordle/Words/verify/000004.log"),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/verify/000004.log")
                );
                IOUtil.write(
                        new FileOutputStream("configs/plugins/ext/Grass/Wordle/Words/verify/000005.ldb"),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/verify/000005.ldb")
                );
                IOUtil.write(
                        new FileOutputStream("configs/plugins/ext/Grass/Wordle/Words/verify/CURRENT"),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/verify/CURRENT")
                );
                IOUtil.write(
                        new FileOutputStream("configs/plugins/ext/Grass/Wordle/Words/verify/LOCK"),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/verify/LOCK")
                );
                IOUtil.write(
                        new FileOutputStream("configs/plugins/ext/Grass/Wordle/Words/verify/MANIFEST-000002"),
                        ResourcesLoader.get("assets/plugins/Grass/Wordle/Words/verify/MANIFEST-000002")
                );
            }
            this.verify = new Iq80DBFactory().open(
                    new File("configs/plugins/ext/Grass/Wordle/Words/verify"),
                    new Options().createIfMissing(true)
                                 .writeBufferSize(1048560)
                                 .compressionType(CompressionType.SNAPPY)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
