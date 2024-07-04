package expo.modules.aliyun.push.service

import android.content.Context
import com.alibaba.sdk.android.push.notification.CPushMessage
import expo.modules.notifications.service.NotificationsService
import expo.modules.aliyun.push.notification.AliyunForegroundNotification
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.AliyunNotificationResponse
import expo.modules.aliyun.push.notification.AliyunNotificationAction
import java.lang.ref.WeakReference
import java.util.WeakHashMap

open class AliyunMessageDelegate() {

    companion object {
        private val listeners get() = AliyunNotificationManager.instances
    }

    fun onNotificationOpened(context: Context, title: String?, summary: String?, extra: String?) {
        val notification = AliyunNotification(title, summary, null)
        val response = AliyunNotificationResponse(notification, AliyunNotificationAction.Default)
        NotificationsService.createNotificationResponseIntent(
            context,
            response.notification.createExpoNotification(),
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
        val notification = AliyunNotification(title, summary, null)
        val response = AliyunNotificationResponse(notification, AliyunNotificationAction.NoAction)
        NotificationsService.createNotificationResponseIntent(
            context,
            response.notification.createExpoNotification(),
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
        NotificationsService.receive(context!!, notification.createExpoNotification())
        listeners.forEach { it.onForegroundNotificationReceived(notification) }
    }
}