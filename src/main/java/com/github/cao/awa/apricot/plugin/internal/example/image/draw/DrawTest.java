package com.github.cao.awa.apricot.plugin.internal.example.image.draw;

import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.carve.*;
import com.github.cao.awa.apricot.message.element.cq.image.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.digger.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.cao.awa.apricot.util.time.*;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.concurrent.*;

public class DrawTest extends MessageReceivedEventHandler {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

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
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        AssembledMessage message = packet.getMessage();

        CarvedMessage<TextMessageElement> texts = message.carver(TextMessageElement.class);

        try {
            if (message.get(0)
                       .toPlainText()
                       .startsWith(".draw")) {
                String target = message.get(0)
                                       .toPlainText();
                target = target.substring(target.indexOf(" ") + 1);
                BufferedImage image = new BufferedImage(
                        200,
                        200,
                        BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D graphics = image.createGraphics();

                graphics.setFont(new Font(
                        Font.SERIF,
                        Font.BOLD,
                        20
                ));

                graphics.setColor(Color.WHITE);
                graphics.fillRect(
                        0,
                        0,
                        200,
                        200
                );
                graphics.setPaint(Color.GREEN);
                graphics.drawString(
                        target,
                        10,
                        30
                );

                File file = new File("test/" + target + ".gif");
                FileUtil.mkdirsParent(file);

                ImageIO.write(
                        image,
                        "gif",
                        file
                );

                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new ImageMessageElement(
                                MessageDigger.digestFile(
                                        file,
                                        MessageDigger.Sha3.SHA_224
                                ),
                                file.toURI()
                                    .toURL()
                                    .toString(),
                                ""
                        )),
                        packet.getResponseId()
                ));

            } else if (message.get(0)
                              .toPlainText()
                              .equals(".exdraw")) {
                long start = TimeUtil.millions();
                int width = 2000;
                int height = 2000;

                BufferedImage image = new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D graphics = image.createGraphics();

                graphics.setFont(new Font(
                        Font.SERIF,
                        Font.BOLD,
                        20
                ));

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        graphics.setColor(new Color(
                                random.nextInt(255),
                                random.nextInt(255),
                                random.nextInt(255)
                        ));
                        graphics.fillRect(
                                x,
                                y,
                                1,
                                1
                        );
                    }
                }

                File file = new File("test/exdraw.gif");
                FileUtil.mkdirsParent(file);

                ImageIO.write(
                        image,
                        "gif",
                        file
                );

                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new AssembledMessage().participate(new ImageMessageElement(
                                MessageDigger.digestFile(
                                        file,
                                        MessageDigger.Sha3.SHA_224
                                ),
                                file.toURI()
                                    .toURL()
                                    .toString(),
                                ""
                        )),
                        packet.getResponseId()
                ));

                System.out.println("Done in " + TimeUtil.processMillion(start) + "ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
