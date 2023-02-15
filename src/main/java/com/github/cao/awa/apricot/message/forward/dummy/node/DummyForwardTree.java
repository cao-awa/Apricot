package com.github.cao.awa.apricot.message.forward.dummy.node;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.message.forward.dummy.DummyForwardMessage;
import com.github.cao.awa.apricot.server.ApricotServer;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.message.MessageUtil;

import java.util.List;
import java.util.function.Consumer;

public class DummyForwardTree {
    private final List<DummyForwardNode> nodes = ApricotCollectionFactor.newArrayList();

    public void add(DummyForwardNode node) {
        this.nodes.add(node);
    }

    public void forEach(Consumer<DummyForwardNode> consumer) {
        this.nodes.forEach(consumer);
    }

    public static DummyForwardTree create(ApricotServer server, JSONObject json) {
        return createTree(
                server,
                json.getJSONArray("messages")
        );
    }

    public static DummyForwardTree createTree(ApricotServer server, JSONArray array) {
        DummyForwardTree tree = new DummyForwardTree();
        array.toList(JSONObject.class)
             .forEach(obj -> {
                 Object content = obj.get("content");
                 if (content instanceof String str) {
                     JSONObject sender = obj.getJSONObject("sender");
                     tree.add(new DummyForwardNode(new DummyForwardMessage(
                             obj.containsKey("group_id") ? obj.getLong("group_id") : - 1,
                             sender.getLong("user_id"),
                             sender.getString("nickname"),
                             MessageUtil.process(
                                     server,
                                     str
                             )
                     )));
                 } else if (content instanceof JSONArray contents) {
                     tree.add(new DummyForwardNode(createTree(
                             server,
                             contents
                     )));
                 }
             });
        return tree;
    }
}
