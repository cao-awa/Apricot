**[ENGLISH](README_EN_US.md)**

# 杏

杏是一个为Chatting Bot设计的封装框架

对于QQ，暂时仅支持 [OneBot](https://github.com/botuniverse/onebot) 协议，对 [Mirai](https://github.com/mamoe/mirai) 协议的支持还并未开始

# 声明

<details>
<summary>声明</summary>

## 承诺
完全开源、无偿开发、长期支持、不删仓库、零隐私收集，零植入程序
## 分发和使用
修改后分发需声明原仓库，完全开源，禁止收费，可以自行署名，可以自由分发

提供内容符合当地法律法规、符合意愿、禁止收费，可以自由使用
## 所有权
杏属于公共项目，并不属于任何人，且任何贡献者均享有杏的著作权

\
任何人也都可以试着修改或增删代码到杏的仓库，请见：[工作流程](doc/zh_cn/develop/working_stream/README.md)

\
目前 ``` 草 ``` 和 ``` 草二号机 ``` 为项目负责人及主要贡献者
</details>

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

# 插件

要为杏编写一个最简单插件只需要一个 ``` @AutoPlugin ``` 注解以及实现 ``` Plugin ```

详见 **[Plugins](doc/zh_cn/develop/plugin/README.md)**

# 技术信息

<details>
<summary>技术信息</summary>

## 协议

杏使用WebSocket协议

在使用内网穿透方式从外部接受信息时，需要将端口开启为TCP类型

## 信息格式

杏使用 JSON 格式交换数据

## 内置插件

杏的几乎任何自带功能，包括存储信息、信息导出等，均是由"内置插件"的处理器(Handler)实现的

由内置的插件 ``` 生草机核心(LawnCore) ``` 提供这些功能

若杏自带的一些功能是多余、不需要的，那么应该找到其对应的处理器进行删除，而非直接删除此插件

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

## 效率
在进行DOS(拒绝服务攻击)测试时，杏最高可以承受每秒29万数据包，且不会导致服务暂停或是与其他正常使用者断开连接

但这是有代价的，杏使用了30GB内存来抵御1亿次的请求的攻击

\
通过反馈的结果数量统计，杏的每秒处理速度为8400个请求

## 速度
杏的处理器响应速度通常在0.1ms~0.3ms之间(不算网络延迟)

</details>