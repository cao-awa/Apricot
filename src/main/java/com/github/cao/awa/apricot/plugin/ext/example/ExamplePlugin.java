package com.github.cao.awa.apricot.plugin.ext.example;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.ext.example.disinformation.*;
import com.github.cao.awa.apricot.plugin.ext.example.encrypt.*;
import com.github.cao.awa.apricot.plugin.ext.example.forward.*;
import org.jetbrains.annotations.*;

import java.util.*;

@AutoPlugin
public class ExamplePlugin extends Plugin {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39b");

    @Override
    public UUID getUuid() {
        return ID;
    }

    @Override
    public @NotNull String getName() {
        return "生草机-示例";
    }

    @Override
    public void onInitialize() {
        //        registerHandler(new DrawTest());
        registerHandler(new Disinformation());
        registerHandler(new CqCodeReproduce());
        registerHandler(new ForwardGetTest());
        registerHandlers(
                new Encrypt(),
                new Decrypt()
        );
    }

    @Override
    public String version() {
        return "1.0.1";
    }
}