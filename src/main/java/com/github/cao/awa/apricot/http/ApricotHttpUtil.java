package com.github.cao.awa.apricot.http;

import com.github.cao.awa.apricot.http.receptacle.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApricotHttpUtil {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final MediaType JSON_MEDIA = MediaType.parse("application/json;charset=utf-8");

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

    public static ResponseReceptacle post(String url, Object body) {
        try {
            return new ResponseReceptacle(CLIENT.newCall(new Request.Builder().url(url)
                                                                              .post(
                                                                                      RequestBody.create(
                                                                                              JSON_MEDIA,
                                                                                              body.toString()
                                                                                                  .getBytes()
                                                                                      )
                                                                              )
                                                                              .build())
                                                .execute());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseReceptacle(new Response.Builder().build());
        }
    }

    public static ResponseReceptacle post(String url) {
        try {
            return new ResponseReceptacle(CLIENT.newCall(new Request.Builder().url(url)
                                                                              .post(RequestBody.create("".getBytes()))
                                                                              .build())
                                                .execute());
        } catch (Exception e) {
            e.printStackTrace();
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
