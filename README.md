**[ENGLISH](README_EN_US.md)**

# 杏

杏是一个为Chatting Bot设计的封装框架

对于QQ，暂时仅支持 [OneBot](https://github.com/botuniverse/onebot) 协议，对 [Mirai](https://github.com/mamoe/mirai) 协议的支持还并未开始

\
杏的目的是为了高效(?)、易于开发、易于部署

\
杏使用Java开发，可以部署在任何支持Java的平台上

杏的各方面占用并不高，在仅使用自带插件的前提下仅需120MB内存、50MB存储空间

# 要求

标记星号(*)为必要，标记斜杠(/)为建议

~~~
* Java 19
* 100MB内存
* 50MB存储空间
服务类型(可同时使用多种)
|___
    -作为QQ Bot
    * 任何支持OneBot协议的代理
      (如CQ-Http、Mirai)
    - 作为Telegram Bot
    ~ 计划中
    -作为聊天服务器
    ~ 计划中

~~~

# 效率

通常来说，杏每秒可以处理数万计的信息和事件<sup>(好像没什么用)</sup>

但杏启动速度堪忧，即便在仅有内置插件时也需要至少1~2秒<sup>[[详情]](doc/zh_cn/performance/bootstrap/bootstrap_time.md)</sup>时间完成启动

# 插件

要为杏编写一个最简单插件只需要一个 ``` @AutoPlugin ``` 注解以及实现 ``` Plugin ```

详见 **[Plugins](doc/zh_cn/develop/plugins/README.md)**

# 技术信息

## 协议

杏使用WebSocket协议

在使用内网穿透方式从外部接受信息时，需要将端口开启为TCP类型

## 信息格式

杏使用 JSON 格式交换数据

## 内置插件

杏的几乎任何自带功能，包括存储信息、信息导出等，均是由"内置插件"的处理器(Handler)实现的

由内置的插件 ``` 生草机(Lawn) ``` 提供这些功能

如果若杏自带的一些功能是多余、不需要的，那么应该找到其对应的处理器进行删除，而非直接删除此插件

直接删除此插件可能导致信息数据库找不到某些历史信息，或是一些其他奇奇怪怪的问题

\
杏使用 ``` 生草机巴士(LawnBus) ``` 提供快速的事件处理器编写<sup>[[详情]](/doc/zh_cn/develop/event/README.md#匿名处理器)</sup>

删除此插件可能导致其他插件无法被成功注册

## 热更新

杏暂时不支持热更新插件，未来会对此功能进行支持

## 兼容性

~~~
此部分尚不完善，处于极前期概念设计阶段
~~~

杏使用"PacketFactor"提供数据包的创建和处理

理论上来说任何使用WebSocket协议、JSON传输格式的连接，杏都可以为其提供信息处理

但是需要为这些数据包单独设计反序列化Factor并将其注册
