package expo.modules.alipush.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alibaba.sdk.android.push.popup.PopupNotifyClick
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener
import expo.modules.core.interfaces.ReactActivityLifecycleListener
import expo.modules.notifications.service.NotificationsService
import expo.modules.alipush.notification.AlipushNotification
import expo.modules.alipush.notification.AlipushNotificationManager
import expo.modules.alipush.notification.AlipushNotificationResponse
import expo.modules.alipush.notification.AlipushNotificationAction
import expo.modules.alipush.notification.NotificationTransformer

class PopupPushDelegate(private val context: Context) : ReactActivityLifecycleListener,
    PopupNotifyClickListener {

    private val notifyClick: PopupNotifyClick by lazy {
        PopupNotifyClick(this)
    }

    private val transformer: NotificationTransformer by lazy {
        NotificationTransformer(context)
    }

    override fun onCreate(activity: Activity?, savedInstanceState: Bundle?) {
        notifyClick.onCreate(activity, activity?.intent);
    }

    override fun onNewIntent(intent: Intent?): Boolean {
        notifyClick.onNewIntent(intent)
        return false
    }

    override fun onSysNoticeOpened(
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?
    ) {
        Log.d("expo-alipush", "onSysNoticeOpened title:$title summary:$summary extra:$extraMap")
        val response = AlipushNotificationResponse(
            AlipushNotification(title, summary, extraMap),
            AlipushNotificationAction.Sys
        )
        NotificationsService.createNotificationResponseIntent(
            context,
            transformer.toExpoNotification(response.notification),
            response.action.createExpoNotificationAction()
        ).send()
        AlipushNotificationManager.receive(response)
    }

}