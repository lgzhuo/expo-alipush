package expo.modules.alipush.service

import android.content.Context
import com.alibaba.sdk.android.push.notification.CPushMessage
import expo.modules.alipush.notification.AlipushForegroundNotification
import expo.modules.alipush.notification.AlipushNotification
import expo.modules.alipush.notification.AlipushNotificationAction
import expo.modules.alipush.notification.AlipushNotificationManager
import expo.modules.alipush.notification.AlipushNotificationResponse
import expo.modules.alipush.notification.NotificationTransformer
import expo.modules.core.utilities.ifNull
import expo.modules.notifications.service.NotificationsService

open class AlipushMessageDelegate {

    private var transformer: NotificationTransformer? = null;
    private fun getTransformer(context: Context): NotificationTransformer =
        transformer.ifNull {
            NotificationTransformer(context).also { transformer = it }
        }

    fun onNotificationOpened(context: Context, title: String?, summary: String?, extra: String?) {
        val notification = AlipushNotification(title, summary, extra)
        val response = AlipushNotificationResponse(notification, AlipushNotificationAction.Default)
        // expo-notifications response all initial and new intent of activity as push notification
        // so we don't send notification response by expo NotificationsService, or the response event
        // will triggered twice.
        AlipushNotificationManager.receive(response)
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
        val notification = AlipushNotification(title, summary, extraMap)
        AlipushNotificationManager.receive(notification)
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
        val notification = AlipushNotification(title, summary, extra)
        val response = AlipushNotificationResponse(notification, AlipushNotificationAction.NoAction)
        AlipushNotificationManager.receive(response)
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
        val notification = AlipushForegroundNotification(
            title, summary, extraMap, openType, openActivity, openUrl
        )
        AlipushNotificationManager.receive(notification)

        // foreground notification behavior same as expo notification
        // it will handle by expo-notifications handling callback in js side
        NotificationsService.receive(context, getTransformer(context).toExpoNotification(notification))
    }
}