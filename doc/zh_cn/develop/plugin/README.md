# 示例
以下是一个最简单化的插件，它会自动注册但不会处理任何事件

要处理事件，请看： [Event](/doc/zh_cn/develop/event/README.md)\
要发送数据包，情看：[Packet](/doc/zh_cn/develop/packet/README.md)
```java
/**
 * An example plugin.
 *
 * @since 1.0.0
 * @author cao_awa
 */
@AutoPlugin
public class ExamplePlugin extends Plugin {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    // 可忽略
    @Override
    public UUID uuid() {
        return ID;
    }

    // 可忽略
    @Override
    public String version() {
        return "1.0.0";
    }
    
    // 必要
    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Example",
                "生草机-示例"
        );
    }

    // 必要
    @Override
    public void initialize() {
        // 在此处初始化插件
        // 执行各种需要的代码
    }
}

```

忽略任何可忽略的重写：
```java
/**
 * An example plugin.
 *
 * @since 1.0.0
 * @author cao_awa
 */
@AutoPlugin
public class ExamplePlugin extends Plugin {
    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Example",
                "生草机-示例"
        );
    }

    @Override
    public void initialize() {
        System.out.println("初始化插件...");
    }
}

```
