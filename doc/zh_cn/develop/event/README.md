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
public void onInitialize() {
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
        PokeReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        if (packet.getTargetId() == packet.getBotId()) {
            proxy.send(new SendPokePacket(
                    packet.getType(),
                    packet.getCauserId(),
                    packet.getBotId(),
                    packet.getResponseId()
            ));
        }
    }
}
```
在 ``` InternalPlugin.java ```

```java
@Override
public void onInitialize() {
    registerHandler(new PokeReciprocity());
}
```

随后使用其他账号对bot进行戳一戳，bot将会反馈一次戳一戳

# 事件
见：[Handler](/doc/zh_cn/develop/event/handler/README.md)