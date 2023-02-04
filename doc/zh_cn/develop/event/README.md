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

以下是提前准备好的插件的onInitialize方法(见[Plugins](/doc/zh_cn/develop/plugin/README.md))

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

# 事件独占

事件独占通常用来处理需要进一步收集信息的需求

\
例如进行"问答游戏"，如果用户的回答中可能存在触发其他插件响应的关键词

则可以用事件独占将此用户的此事件独占至"问答游戏"的处理器

\
使用服务器事件管理器提供的 ``` exclusive ``` API来使处理器独占一个事件

如以下示例，当用户发送awa时，处理器会请求独占三次"收到私聊信息"事件，随后无论用户说什么都给用户反馈"www..."

如果指定时间没完成这么多次的处理，则会触发超时，超时的时候向用户发送"Timeout..."

```java
public class ExclusiveSample extends PrivateMessageReceivedEventHandler {
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
    public void onMessageReceived(PrivateMessageReceivedEvent<?> event) {
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();
        PrivateMessageReceivedPacket packet = event.getPacket();

        if (packet.getMessage()
                  .toPlainText()
                  .equals("awa")) {
            // 请求独占"收到私聊信息"事件
            server.getEventManager()
                  .exclusive(
                          // 仅占此目标的事件，为了不影响其他人的使用
                          packet.target(),
                          // 独占时由哪个处理器来处理这个事件
                          this,
                          // 连续进行多少次的事件独占
                          3,
                          // 当指定时间（毫秒）后还没达到目标次数则超时
                          10000,
                          () -> {
                              // 在超时时执行一些操作
                              proxy.send(new SendMessagePacket(
                                      MessageType.PRIVATE,
                                      new TextMessageElement("Timeout...").toMessage(),
                                      packet.getResponseId()
                              ));
                          }
                  );
        }
    }

    @Override
    public void onExclusive(PrivateMessageReceivedEvent<?> event) {
        // 当此事件被处理器独占时使用此方法处理
        event.getProxy()
             // 回用户一个"www"
             .send(new SendMessagePacket(
                     MessageType.PRIVATE,
                     new TextMessageElement("www...").toMessage(),
                     event.getPacket()
                          .getResponseId()
             ));
    }
}

```

在事件被独占时，默认只会阻止同属当前插件的处理器，其他插件不受影响

在处理事件独占时，需要通过 ``` onExclusive ``` 方法而不是处理器的常规 ``` onXxx ```方法处理

## 强制处理
重写 EventHandler 的 ``` compulsory ``` 方法或是实现 ``` Compulsory ``` 都可以让事件处理器强制处理事件

以下两种方式均可，两种方式同时使用也行，但是没有必要这么做

```java
public class PokeReciprocity extends PokeReceivedEventHandler implements Compulsory {
    @Override
    public void onPoke(PokeReceivedEvent event) {
        
    }
}
```

```java
public class PokeReciprocity extends PokeReceivedEventHandler {
    @Override
    public boolean compulsory() {
        return true;
    }
    
    @Override
    public void onPoke(PokeReceivedEvent event) {

    }
}
```

如果一个插件内所有处理器都需要强制处理事件，可以对 ``` Plugin ``` 进行上面的操作

即可使此插件内所有处理器全部都会进行事件强制处理，此时处理器的 ``` compulsory ``` 已经被忽略，不需要再做出任何修改 

```java
@AutoPlugin
public class InternalPlugin extends Plugin implements Compulsory {
    // 其他必要方法在此处不多赘述，省略...
}
```

```java
@AutoPlugin
public class InternalPlugin extends Plugin {
    // 其他必要方法在此处不多赘述，省略...

    @Override
    public boolean compulsory() {
        return true;
    }
}
```


## 独占级别
杏对独占提供一个"level"，用于决定独占只针对当前插件或是选择其他插件也一起阻止

亦或是让所有杏已加载的插件都被此独占阻止处理

此"level"与"compulsory"判断不冲突，只是决定适用范围

## 注意

对同一个事件、同一个用户的独占，不能同时被两个处理器请求

这会导致直接这一独占请求被取消，两个处理器都不会成功请求到事件独占

并非是先来后到，或是后者覆盖，而是都别想用（）

# 密集型
在事件处理器中，可以设置任务密集类型，重写 ``` intensive ``` 方法以设置密集类型

默认情况下的密集类型为CPU密集型（``` IntensiveType.CPU ```）

\
此参数对处理器的性能影响非常巨大，修改前请确认类型是否正确

## 通常来说
通常来说，IO密集型指的是几乎都在等待网络、读写文件、读写数据库，CPU几乎没什么占用的任务

而CPU密集型并不是指不做上面那些事，而是CPU的占用非常高，通常会做到单核满载

\
基于这一准则设定密集类型即可
## 虚拟线程
在JDK19中引入了Preview "虚拟线程"，这个Preview提供了轻量级的软线程

基于JVM而非操作系统管理的线程使得虚拟线程的切换、创建开销降低，非常适合IO密集型任务

但是其在计算密集型任务并不如操作系统线程，因此杏并没有在任何地方都使用虚拟线程

\
这一Preview适合IO密集型场景，需要频繁创建大量线程且CPU任务较轻，用于大幅提高吞吐量

杏在对数据包的反序列化、事件创建和触发这两处使用虚拟线程，这会带来很大的性能提升

## 例子
内置插件中包含的"信息存储"即为IO密集型任务

因为其所有操作几乎都在下载、读写文件、读写数据库

因此，"信息存储"相关的处理器都使用IO密集型（``` IntensiveType.IO ```）

# 事件处理器

见：[Handler](/doc/zh_cn/develop/event/handler/README.md)