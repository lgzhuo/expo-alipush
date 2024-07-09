package expo.modules.aliyun.push.notification.emitting

import android.util.Log
import expo.modules.aliyun.push.notification.AliyunForegroundNotification
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.AliyunNotificationResponse
import expo.modules.aliyun.push.notification.NotificationSerializer
import expo.modules.aliyun.push.notification.NotificationTransformer
import expo.modules.aliyun.push.notification.interfaces.AliyunNotificationListener
import expo.modules.aliyun.push.notification.trigger.AliyunNotificationTrigger
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.notifications.notifications.emitting.NotificationsEmitter
import expo.modules.notifications.notifications.interfaces.NotificationListener
import expo.modules.notifications.notifications.interfaces.NotificationManager
import expo.modules.notifications.notifications.model.NotificationResponse

private const val NEW_MESSAGE_EVENT_NAME = "onDidReceiveAliyunNotification"
private const val NEW_RESPONSE_EVENT_NAME = "onDidReceiveAliyunNotificationResponse"

class AliyunNotificationEmitter : Module(), AliyunNotificationListener, NotificationListener {

    private val notificationEmitter: NotificationsEmitter?
        get() = appContext.registry.getModule() ?: appContext.legacyModule()

    private lateinit var notificationTransformer: NotificationTransformer

    private lateinit var expoNotificationManager: NotificationManager

    override fun definition() = ModuleDefinition {
        Name("AliyunNotificationEmitter")

        Events(
            NEW_MESSAGE_EVENT_NAME,
            NEW_RESPONSE_EVENT_NAME
        )

        OnCreate {

            AliyunNotificationManager.addListener(this@AliyunNotificationEmitter)

            notificationTransformer =
                NotificationTransformer(requireNotNull(appContext.reactContext))

            expoNotificationManager = requireNotNull(
                appContext.legacyModuleRegistry.getSingletonModule(
                    "NotificationManager",
                    NotificationManager::class.java
                )
            )
            expoNotificationManager.addListener(this@AliyunNotificationEmitter)
        }

        OnDestroy {
            AliyunNotificationManager.removeListener(this@AliyunNotificationEmitter)

            expoNotificationManager.removeListener(this@AliyunNotificationEmitter)
        }
    }

    override fun onNotificationReceived(notification: AliyunNotification) {
        notificationTransformer.toExpoNotification(notification)
            .also {
                // send the exactly notification event ourself, this will correctly serialize the trigger
                sendEvent(
                    NEW_MESSAGE_EVENT_NAME,
                    NotificationSerializer.toBundle(it)
                )
                // also send the notification by expo-notifications, but the serialized trigger type will be unknown
                notificationEmitter?.onNotificationReceived(it)
            }
    }

    override fun onForegroundNotificationReceived(notification: AliyunForegroundNotification) {
        // foreground notification is also emit and handle by expo-notifications but with incorrect
        // trigger type, we send a correctly serialized event here and do replace in js side
        notificationTransformer.toExpoNotification(notification).also {
            sendEvent(
                NEW_MESSAGE_EVENT_NAME,
                NotificationSerializer.toBundle(it)
            )
        }
    }

    override fun onNotificationResponseReceived(response: AliyunNotificationResponse) {
        // this events are triggered by notification create by aliyun sdk
        // another event should has send by expo-notifications but with incorrect properties
        notificationTransformer.toExpoNotificationResponse(response).also {
            sendEvent(
                NEW_RESPONSE_EVENT_NAME,
                NotificationSerializer.toBundle(it)
            )
        }
    }

    /* Expo Notification Events */

    override fun onNotificationResponseReceived(response: NotificationResponse?): Boolean {
        return response.takeIf { it?.notification?.notificationRequest?.trigger is AliyunNotificationTrigger }
            ?.let {
                Log.d(
                    "expo-aliyun-push",
                    "on aliyun notification response received by expo notification manager, action id: ${response!!.action.identifier}"
                )
                // this is the response triggered by aliyun notification that handle by expo-notifications
                // send the exactly notification response event ourself, this will correctly serialize the trigger
                // a same event will be send by expo-notifications but with "unknown" trigger type
                sendEvent(NEW_RESPONSE_EVENT_NAME, NotificationSerializer.toBundle(response))
                true
            } ?: false
    }
}