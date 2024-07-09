package expo.modules.aliyun.push.service

import android.content.Context
import com.alibaba.sdk.android.push.notification.CPushMessage
import expo.modules.aliyun.push.notification.AliyunForegroundNotification
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationAction
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.AliyunNotificationResponse
import expo.modules.aliyun.push.notification.NotificationTransformer
import expo.modules.core.utilities.ifNull
import expo.modules.notifications.service.NotificationsService

open class AliyunMessageDelegate {

    private var transformer: NotificationTransformer? = null;
    private fun getTransformer(context: Context): NotificationTransformer =
        transformer.ifNull {
            NotificationTransformer(context).also { transformer = it }
        }

    fun onNotificationOpened(context: Context, title: String?, summary: String?, extra: String?) {
        val notification = AliyunNotification(title, summary, extra)
        val response = AliyunNotificationResponse(notification, AliyunNotificationAction.Default)
        // expo-notifications response all initial and new intent of activity as push notification
        // so we don't send notification response by expo NotificationsService, or the response event
        // will triggered twice.
        AliyunNotificationManager.receive(response)
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
        // this notification has present by aliyun sdk, we emit it to js side but not handle by expo-notifications
        val notification = AliyunNotification(title, summary, extraMap)
        AliyunNotificationManager.receive(notification)
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
        AliyunNotificationManager.receive(response)
    }

    fun onNotificationReceivedInApp(
        context: Context,
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
        AliyunNotificationManager.receive(notification)

        // foreground notification behavior same as expo notification
        // it will handle by expo-notifications handling callback in js side
        NotificationsService.receive(context, getTransformer(context).toExpoNotification(notification))
    }
}