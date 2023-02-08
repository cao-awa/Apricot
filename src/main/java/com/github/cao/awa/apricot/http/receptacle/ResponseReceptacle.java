package com.github.cao.awa.apricot.http.receptacle;

import com.alibaba.fastjson2.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;
import okhttp3.*;
import org.jetbrains.annotations.*;

public record ResponseReceptacle(Response response) {
    @Nullable
    public JSONObject json() {
        return EntrustEnvironment.trys(() -> new JSONObject(JSONObject.parse(this.response.body()
                                                                                          .string())));
    }

    public void operation(ExceptingConsumer<Response> action) {
        EntrustEnvironment.operation(
                response,
                action
        );
    }

    @Nullable
    public String string() {
        return EntrustEnvironment.trys(() -> this.response.body()
                                                          .string());
    }

    public String message() {
        return this.response.message();
    }
}
