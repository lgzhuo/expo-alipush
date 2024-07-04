package expo.modules.aliyun.push.notification

enum class AliyunNotificationAction(val identifier: String) {

    Default("expo.modules.aliyun.push.actions.DEFAULT"),
    NoAction("expo.modules.aliyun.push.actions.NO_ACTION"),
    Sys("expo.modules.aliyun.push.actions.SYSTEM");

    fun createExpoNotificationAction(): expo.modules.notifications.notifications.model.NotificationAction {
        return expo.modules.notifications.notifications.model.NotificationAction(
            identifier,
            null,
            false
        )
    }
}