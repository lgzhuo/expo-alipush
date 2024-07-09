package expo.modules.alipush.notification

enum class AlipushNotificationAction(val identifier: String) {

    Default("expo.modules.alipush.actions.DEFAULT"),
    NoAction("expo.modules.alipush.actions.NO_ACTION"),
    Sys("expo.modules.alipush.actions.SYSTEM");

    fun createExpoNotificationAction(): expo.modules.notifications.notifications.model.NotificationAction {
        return expo.modules.notifications.notifications.model.NotificationAction(
            identifier,
            null,
            false
        )
    }
}