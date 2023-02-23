# 数据包
杏发送数据包需要使用ApricotProxy，这通常可以在事件发生时通过 ``` event.getProxy() ```得到\
如果你不知道什么是事件，请看：[Event](/doc/zh_cn/develop/event/README.md)

使用 ``` proxy.send(Packet packet) ``` 来发送数据包：

```java
// 发送数据包
proxy.send(
    // 数据包，此处数据包为退群
    new SendLeaveGroupPacket(
        // 群号
        114514
    )
)
```

# 发送数据包
以下是所有杏目前支持的数据包，用户也可以自己实现 ``` WritablePacket ``` 来达到写自定义数据包的目的

## 消息
```
暂时不支持频道的消息发送
```

发送信息：SendMessagePacket\
（以上数据包需要传入MessageType来确定发到哪里）

发给私聊：SendPrivateMessagePacket\
发给群聊：SendGroupMessagePacket

## 撤回
撤回群消息：SendRecallMessagePacket

## 戳一戳
戳一戳：SendPokePacket

## 转发
发送聊天记录：SendMessagesForwardPacket\
（以上数据包需要传入MessageType来确定发到哪里）

发给私聊：SendPrivateMessagesForwardPacket\
发给群聊：SendGroupMessagesForwardPacket

## 退群
退群：SendLeaveGroupPacket

## 踢人
踢人：SendGroupKickPacket

## 禁言
禁言：SendGroupMutePacket

禁言所有人：SendGroupMuteAllPacket

禁言正常人：SendGroupMuteNormalPacket\
禁言匿名：SendGroupMuteAnonymousPacket

## 同意入群
同意入群：SendGroupApprovalPacket\
（以上数据包需要传入ApprovalType来确定发到哪里，通常从申请入群的事件中获取）

同意主动加群：SendAddGroupApprovalPacket\
同意邀请入群：SendInviteGroupApprovalPacket

## 同意加好友
同意加好友：SendFriendApprovalPacket

## 群打卡
群打卡：SendGroupSignPacket

## 修改群名片
修改某人的群名片：SetGroupNameCardPacket

## 修改群名
修改群名：SendGroupNameSetPacket