package expo.modules.aliyun.push.service

import android.content.Context
import com.alibaba.sdk.android.push.notification.CPushMessage
import expo.modules.aliyun.push.notification.AliyunForegroundNotification
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationAction
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.AliyunNotificationResponse
import expo.modules.aliyun.push.notification.NotificationTransformer
import expo.modules.notifications.service.NotificationsService

open class AliyunMessageDelegate(protected val context: Context) {

    companion object {
        private val listeners get() = AliyunNotificationManager.instances
    }

    private val transformer: NotificationTransformer by lazy { NotificationTransformer(context) }

    fun onNotificationOpened(context: Context, title: String?, summary: String?, extra: String?) {
        val notification = AliyunNotification(title, summary, extra)
        val response = AliyunNotificationResponse(notification, AliyunNotificationAction.Default)
        NotificationsService.createNotificationResponseIntent(
            context,
            transformer.toExpoNotification(notification),
            response.action.createExpoNotificationAction()
        ).send()
        listeners.forEach { it.onNotificationResponseReceived(response) }
    }

    fun onNotificationRemoved(p0: Context?, p1: String?) {
        TODO("Not yet implemented")
    }

    fun onNotification(
        context: Context,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?
    ) {
        val notification = AliyunNotification(title, summary, extraMap)
        listeners.forEach {
            it.onNotificationReceived(notification)
        }
    }

    fun onMessage(p0: Context?, p1: CPushMessage?) {
        TODO("Not yet implemented")
    }

    fun onNotificationClickedWithNoAction(
        context: Context,
        title: String?,
        summary: String?,
        extra: String?
    ) {
        val notification = AliyunNotification(title, summary, extra)
        val response = AliyunNotificationResponse(notification, AliyunNotificationAction.NoAction)
        NotificationsService.createNotificationResponseIntent(
            context,
            transformer.toExpoNotification(notification),
            response.action.createExpoNotificationAction()
        ).send()
        listeners.forEach {
            it.onNotificationResponseReceived(response)
        }
    }

    fun onNotificationReceivedInApp(
        context: Context?,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?,
        openType: Int,
        openActivity: String?,
        openUrl: String?
    ) {
        val notification = AliyunForegroundNotification(
            title, summary, extraMap, openType, openActivity, openUrl
        )
        // foreground notification behavior same as expo notification
        // it will handle by notification handling callback in js side
        NotificationsService.receive(context!!, transformer.toExpoNotification(notification))
        listeners.forEach { it.onForegroundNotificationReceived(notification) }
    }
}