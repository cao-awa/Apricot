# 事件处理器
见 [Event](/doc/zh_cn/develop/event/README.md)

## 收到消息
收到任何消息：MessageReceivedEventHandler

收到群消息：GroupMessageReceivedEventHandler\
收到群正常消息：GroupNormalMessageReceivedEventHandler\
收到群匿名消息：GroupAnonymousMessageReceivedEventHandler

收到私聊消息：PrivateMessageReceivedEventHandler\
收到好友私聊消息：PrivateFriendMessageReceivedEventHandler\
收到临时会话消息：还没做\
收到其他消息：还没做

# 消息被撤回
任何消息被撤回：MessageRecallEventHandler

群消息被撤回：GroupMessageRecallEventHandler\
私聊消息被撤回：PrivateMessageRecallEventHandler

# 被戳一戳
被戳：PokeReceivedEventHandler

在群内被戳：GroupPokeReceivedEventHandler

在私聊被戳：PrivatePokeReceivedEventHandler

# 群人数变化
群人数变化：GroupMemberChangedEventHandler

群人数增加：GroupMemberIncreasedEventFilter\
有人被同意加入：GroupMemberApprovedEventFilter\
有人被邀请加入：GroupMemberInvitedEventHandler

群人数减少：GroupMemberDecreasedEventFilter\
有人退群：GroupMemberLeavedEventHandler\
有人被踢：GroupMemberKickedEventHandler\
Bot被踢：BotDiedFromGroupEventHandler

# 群成员头衔变更
变更：GroupTitleChangedReceivedEventHandler

# 群名片变更
```
此事件已被 @Waring("NOT_REAL_TIME") 警告此非实时性
此 @Warning 仅针对 CQ-HTTP，Mirai可以忽略此
```
变更：GroupNameChangedReceivedEventHandler

# 禁言
```
禁言时间结束后不会触发解除禁言事件
```
被禁言：IssueGroupMuteEventHandler

全员禁言：IssueGroupAllMuteEventHandler\
单独禁言：IssueGroupPersonalMuteEventHandler

被解除禁言：LiftGroupMuteEventHandler

全员禁言被解除： LiftGroupAllMuteEventHandler\
单独禁言被解除：LiftGroupPersonalMuteEventHandler

# 申请入群
申请入群：AddGroupEventHandler\
邀请入群：InviteGroupEventHandler

# 申请加好友
申请加好友：AddFriendEventHandler
