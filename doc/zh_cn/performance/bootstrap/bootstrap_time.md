# 时间
杏的完全启动时间通常在1~2秒以内，基于以下测试平台：
```
OS: Windows 10 - 21H1 19043.2075
CPU: Intel Core i5-10600
Memory: DDR4 2666Mhz 64G
Disk: Intel 660P 1T
JDK: OpenJDK 64-Bit Server VM (build 19+36-2238, mixed mode, sharing)
Args: -Xmx200M -Xms200M
```

# 极限情况
杏在以上测试平台下，加载2.27G共185个插件<sup>没什么用</sup>的情况下耗时为11646ms，在12s内完成
