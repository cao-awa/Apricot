package com.github.cao.awa.apricot.http;

import com.github.cao.awa.apricot.http.receptacle.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;
import okhttp3.*;

public class ApricotHttpUtil {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void get(String url, ExceptingConsumer<Response> callback, String... params) {
        EntrustEnvironment.trys(() -> {
            callback.accept(get(
                    url,
                    params
            ).response());
        });
    }

    public static ResponseReceptacle get(String url, String... params) {
        try {
            if (params.length > 0) {
                url += "?";
                StringBuilder urlBuilder = new StringBuilder(url);
                for (String param : params) {
                    urlBuilder.append(param)
                              .append("&");
                }
                urlBuilder.setLength(urlBuilder.length() - 1);
                url = urlBuilder.toString();
            }

            return new ResponseReceptacle(CLIENT.newCall(new Request.Builder().url(url)
                                                                              .get()
                                                                              .build())
                                                .execute());
        } catch (Exception e) {
            return new ResponseReceptacle(new Response.Builder().build());
        }
    }

    public static long ping(String url) {
        try {
            long start = TimeUtil.millions();
            CLIENT.newCall(new Request.Builder().url(url)
                                                .get()
                                                .build())
                  .execute();
            return TimeUtil.processMillion(start);
        } catch (Exception e) {
            return - 1;
        }
    }
}
