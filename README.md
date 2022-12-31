# 其他语言
**[ENGLISH](README_EN_US.md)**  
# 杏
杏是一个为QQ机器人设计的封装框架，暂时仅支持 [OneBot](https://github.com/botuniverse/onebot) 协议，对 [Mirai](https://github.com/mamoe/mirai) 协议的支持还并未开始

杏的目的是为了高效(?)、易于开发、易于部署

<br>
杏使用Java开发，可以部署在任何支持Java的平台上

杏的内存占用并不高，在2G物理内存的Linux机器上也可以部署杏

# 效率
通常来说，杏每秒可以处理数万计的信息和事件<sup>(好像没什么用)</sup>，但启动速度堪忧，即便在无插件时也需要至少1~2秒<sup>[[1]](doc/zh_cn/performance/bootstrap/bootstrap_time.md)</sup>时间完成启动

# 插件
要为杏编写一个最简单插件只需要一个 ``` @AutoPlugin ``` 注解以及实现 ``` AccomplishPlugin ```

详见 **[Plugins](doc/zh_cn/develop/plugins/README.md)**
