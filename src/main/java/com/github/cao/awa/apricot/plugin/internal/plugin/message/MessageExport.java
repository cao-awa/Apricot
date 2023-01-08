package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
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

        if (packet.getMessage()
                  .toPlainText()
                  .equals(".exports raws")) {
            try {
                File file = new File("export/json/exports.raws");
                FileUtil.mkdirsParent(file);
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
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
                                             output,
                                             json.toString()
                                     );
                                     IOUtil.write0(
                                             output,
                                             "\n"
                                     );
                                 },
                                 Throwable::printStackTrace
                         );
                     });
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}