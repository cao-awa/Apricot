# 前言

目前杏的事件并不完全，因为是后端代理，所以一些OneBot或Mirai不支持的行为杏也不会支持

杏的开发并不复杂，大部分人都可以在阅读和部分学习后进行简单的插件开发

# 开发

杏会内置一些事件处理器<sup>未实现</sup>，用户可以借此使用JSON来定义一些行为以避免编写Java代码

用户也可以基于插件的事件处理器注册来自定义一些特殊的操作

# 结构

Xxx通常是一个事件的名称

```java
public class NameOfHandler extends XxxEventHandler {
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
    public void onXxx(XxxEvent event) {
        // ...
    }
}
```

以下是提前准备好的插件的onInitialize方法(见[Plugins](/doc/zh_cn/develop/plugins/README.md))

```java
@Override
public void onInitialize(){
    registerHandler(new NameOfHandler());
}
```

只需要这样，编写一个事件处理器以及进行一次注册即可

随后只需要等待这个事件发生，就会触发处理器中的onXxx方法

# 示例

``` PokeReciprocity.java ```

以下示例为戳一戳事件，向戳bot的用户回戳一次

```java
public class PokeReciprocity extends PokeReceivedEventHandler {
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
    public void onPoke(PokeReceivedEvent event) {
        // 获取数据包，以便处理信息
        PokeReceivedPacket packet = event.getPacket();
        // 获取代理，用于发送数据包
        ApricotProxy proxy = event.getProxy();
        if (
            // 判断戳一戳的目标是此bot
                packet.getTargetId() == packet.getBotId() &&
                // 判断不是bot的账号戳的自己
                // 缺少这一条件可能导致无限自己戳自己直到次数用尽
                ! (packet.getCauserId() == packet.getBotId())
        ) {
            // 发送数据包
            proxy.send(
                    // PokePacket 即戳一戳的数据包
                    new SendPokePacket(
                            // 回复类型，通常从接收的数据包获取
                            packet.getType(),
                            // 要戳谁？
                            // 因为是回戳，所以直接使用戳了bot的id
                            packet.getCauserId(),
                            // bot的id，通常直从接收的数据包接获
                            packet.getBotId(),
                            // 发给谁
                            packet.getResponseId()
                    )
            );
        }
    }
}

```

在 ``` InternalPlugin.java ```

```java
@Override
public void onInitialize(){
        // 注册回戳事件处理器
        registerHandler(new PokeReciprocity());
        }
```

随后使用其他账号对bot进行戳一戳，bot将会反馈一次戳一戳

# 匿名处理器

```
匿名处理器不支持事件独占(exclusive)
```

匿名处理器允许不创建类，通过lambda来处理事件

例如：

```java
/**
 * An example plugin.
 *
 * @since 1.0.0
 * @author cao_awa
 */
@AutoPlugin
public class InternalPlugin extends Plugin {
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public void onInitialize() {
        // 注册到戳一戳事件
        getServer().eventBus.poke(event -> {
            // 获取数据包，以便处理信息
            PokeReceivedPacket packet = event.getPacket();
            // 获取代理，用于发送数据包
            ApricotProxy proxy = event.getProxy();
            if (
                // 判断戳一戳的目标是此bot
                    packet.getTargetId() == packet.getBotId() &&
                    // 判断不是bot的账号戳的自己
                    // 缺少这一条件可能导致无限自己戳自己直到次数用尽
                    ! (packet.getCauserId() == packet.getBotId())
            ) {
                // 发送数据包
                proxy.send(
                        // PokePacket 即戳一戳的数据包
                        new SendPokePacket(
                                // 回复类型，通常从接收的数据包获取
                                packet.getType(),
                                // 要戳谁？
                                // 因为是回戳，所以直接使用戳了bot的id
                                packet.getCauserId(),
                                // bot的id，通常直从接收的数据包接获
                                packet.getBotId(),
                                // 发给谁
                                packet.getResponseId()
                        )
                );
            }
        });
    }

    @Override
    public String version() {
        return "0.0.1";
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

这样可以在不创建额外类的情况下也处理事件

# 事件

见：[Handler](/doc/zh_cn/develop/event/handler/README.md)