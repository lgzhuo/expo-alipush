package expo.modules.alipush.notification

import android.content.Context
import expo.modules.alipush.notification.trigger.AlipushNotificationTrigger
import expo.modules.notifications.notifications.model.Notification
import expo.modules.notifications.notifications.model.NotificationAction
import expo.modules.notifications.notifications.model.NotificationRequest
import expo.modules.notifications.notifications.model.NotificationResponse

class NotificationTransformer(private val context: Context) {
    fun toExpoNotificationResponse(response: AlipushNotificationResponse) = NotificationResponse(
        toExpoNotificationAction(response.action),
        toExpoNotification(response.notification)
    )

    fun toExpoNotification(notification: AlipushNotification) = Notification(
        toExpoNotificationRequest(notification), notification.date
    )

    fun toExpoNotificationRequest(notification: AlipushNotification) = NotificationRequest(
        notification.identifier,
        toExpoNotificationContent(notification),
        AlipushNotificationTrigger(notification)
    )

    fun toExpoNotificationContent(notification: AlipushNotification) =
        AlipushNotificationContentBuilder(context).also {
            it.setTitle(notification.title)
            it.setText(notification.summary)
            it.setExtra(notification.extra)
        }.build()

    fun toExpoNotificationAction(action: AlipushNotificationAction) = NotificationAction(
        action.identifier,
        null,
        false
    )
}