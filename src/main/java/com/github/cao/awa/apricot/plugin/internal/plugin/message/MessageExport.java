package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.util.digger.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;

public class MessageExport extends MessageReceivedEventHandler {
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


        try {
            if (packet.getMessage()
                      .toPlainText()
                      .equals(".exports raws")) {
                exportJson(event);
            } else if (packet.getMessage()
                             .toPlainText()
                             .equals(".exports bin")) {
                exportBin(event);
            } else if (packet.getMessage()
                             .toPlainText()
                             .equals(".exports")) {
                exportJson(event);
                exportBin(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exportBin(MessageReceivedEvent<?> event) throws Exception {
        File raw = new File("export/json/exports.bin");
        File sha = new File("export/json/exports.bin.sha");
        FileUtil.mkdirsParent(raw);
        final BufferedOutputStream rawOutput = new BufferedOutputStream(new FileOutputStream(raw));
        event.getProxy()
             .server()
             .getMessagesHeadOffice()
             .forEach((k, v) -> {
                 EntrustEnvironment.trys(
                         () -> {
                             IOUtil.write0(
                                     rawOutput,
                                     Base256.longToBuf(k)
                             );
                             IOUtil.write0(
                                     rawOutput,
                                     v.toBin()
                             );
                         },
                         Throwable::printStackTrace
                 );
             });
        rawOutput.close();

        final BufferedOutputStream shaOutput = new BufferedOutputStream(new FileOutputStream(sha));
        IOUtil.write(
                shaOutput,
                MessageDigger.digestFile(
                        raw,
                        MessageDigger.Sha3.SHA_512
                )
        );
        shaOutput.close();
    }

    private static void exportJson(MessageReceivedEvent<?> event) throws Exception {
        File raw = new File("export/json/exports.jsons");
        File sha = new File("export/json/exports.jsons.sha");
        FileUtil.mkdirsParent(raw);
        final BufferedOutputStream rawOutput = new BufferedOutputStream(new FileOutputStream(raw));
        event.getProxy()
             .server()
             .getMessagesHeadOffice()
             .forEach((k, v) -> {
                 JSONObject json = new JSONObject();
                 json.put(
                         "m",
                         v.toJSONObject()
                 );
                 json.put(
                         "i",
                         k
                 );
                 EntrustEnvironment.trys(
                         () -> {
                             IOUtil.write0(
                                     rawOutput,
                                     json.toString()
                             );
                             IOUtil.write0(
                                     rawOutput,
                                     "\n"
                             );
                         },
                         Throwable::printStackTrace
                 );
             });
        rawOutput.close();

        final BufferedOutputStream shaOutput = new BufferedOutputStream(new FileOutputStream(sha));
        IOUtil.write(
                shaOutput,
                MessageDigger.digestFile(
                        raw,
                        MessageDigger.Sha3.SHA_512
                )
        );
        shaOutput.close();
    }
}
