package expo.modules.aliyun.push.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alibaba.sdk.android.push.popup.PopupNotifyClick
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener
import expo.modules.core.interfaces.ReactActivityLifecycleListener
import expo.modules.notifications.service.NotificationsService
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.AliyunNotificationResponse
import expo.modules.aliyun.push.notification.AliyunNotificationAction
import expo.modules.aliyun.push.notification.NotificationTransformer

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
        Log.d("expo-aliyun-push", "onSysNoticeOpened title:$title summary:$summary extra:$extraMap")
        val response = AliyunNotificationResponse(
            AliyunNotification(title, summary, extraMap),
            AliyunNotificationAction.Sys
        )
        NotificationsService.createNotificationResponseIntent(
            context,
            transformer.toExpoNotification(response.notification),
            response.action.createExpoNotificationAction()
        ).send()
        AliyunNotificationManager.receive(response)
    }

}