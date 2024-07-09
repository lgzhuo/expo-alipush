package expo.modules.aliyun.push.notification

import android.content.Context
import expo.modules.aliyun.push.notification.trigger.AliyunNotificationTrigger
import expo.modules.notifications.notifications.model.Notification
import expo.modules.notifications.notifications.model.NotificationAction
import expo.modules.notifications.notifications.model.NotificationRequest
import expo.modules.notifications.notifications.model.NotificationResponse

class NotificationTransformer(private val context: Context) {
    fun toExpoNotificationResponse(response: AliyunNotificationResponse) = NotificationResponse(
        toExpoNotificationAction(response.action),
        toExpoNotification(response.notification)
    )

    fun toExpoNotification(notification: AliyunNotification) = Notification(
        toExpoNotificationRequest(notification), notification.date
    )

    fun toExpoNotificationRequest(notification: AliyunNotification) = NotificationRequest(
        notification.identifier,
        toExpoNotificationContent(notification),
        AliyunNotificationTrigger(notification)
    )

    fun toExpoNotificationContent(notification: AliyunNotification) =
        AliyunNotificationContentBuilder(context).also {
            it.setTitle(notification.title)
            it.setText(notification.summary)
            it.setExtra(notification.extra)
        }.build()

    fun toExpoNotificationAction(action: AliyunNotificationAction) = NotificationAction(
        action.identifier,
        null,
        false
    )
}