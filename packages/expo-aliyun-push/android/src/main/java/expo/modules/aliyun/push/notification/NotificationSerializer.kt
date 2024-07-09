package expo.modules.aliyun.push.notification

import android.os.Bundle
import expo.modules.aliyun.push.notification.trigger.AliyunNotificationTrigger
import expo.modules.notifications.notifications.NotificationSerializer
import expo.modules.notifications.notifications.model.Notification
import expo.modules.notifications.notifications.model.NotificationRequest
import expo.modules.notifications.notifications.model.NotificationResponse
import expo.modules.notifications.notifications.model.TextInputNotificationResponse

object NotificationSerializer {
    fun toBundle(response: NotificationResponse) = Bundle().also {
        it.putString("actionIdentifier", response.actionIdentifier)
        it.putBundle("notification", toBundle(response.notification))
        if (response is TextInputNotificationResponse) {
            it.putString("userText", response.userText)
        }
    }

    fun toBundle(notification: Notification) = Bundle().also {
        it.putBundle("request", toBundle(notification.notificationRequest))
        it.putLong("date", notification.date.time)
    }

    fun toBundle(request: NotificationRequest) = NotificationSerializer.toBundle(request).also {
        if (request.trigger is AliyunNotificationTrigger) {
            it.putBundle("trigger", toBundle(request.trigger as AliyunNotificationTrigger))
        }
    }

    fun toBundle(trigger: AliyunNotificationTrigger) = Bundle().also {
        it.putString("type", "push")
        it.putBundle("aliyunNotification", toBundle(trigger.notification))
    }

    fun toBundle(notification: AliyunNotification) = Bundle().also {
        it.putString("title", notification.title)
        it.putString("summary", notification.summary)
        it.putBundle("extra", toBundle(notification.extra))
        it.putLong("date", notification.date.time)
        if (notification is AliyunForegroundNotification) {
            it.putBoolean("isForeground", true)
            it.putInt("openType", notification.openType)
            it.putString("openActivity", notification.openActivity)
            it.putString("openUrl", notification.openUrl)
        } else {
            it.putBoolean("isForeground", false)
        }
    }

    fun toBundle(extra: AliyunNotificationExtra) = Bundle().also {
        extra.toMap().entries.forEach { (key, value) -> it.putString(key, value) }
    }

}