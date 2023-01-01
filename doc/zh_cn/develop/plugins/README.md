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
public class InternalPlugin extends AccomplishPlugin {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public void onInitialize() {
        // ...
    }

    @Override
    public UUID getUuid() {
        return ID;
    }
    
    @NotNull
    @Override
    public String getName() {
        return "Example Plugin";
    }
}
```