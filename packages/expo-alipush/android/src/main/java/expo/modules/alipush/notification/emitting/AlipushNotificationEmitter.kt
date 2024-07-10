package expo.modules.alipush.notification.emitting

import android.os.Bundle
import android.util.Log
import expo.modules.alipush.notification.AlipushForegroundNotification
import expo.modules.alipush.notification.AlipushNotification
import expo.modules.alipush.notification.AlipushNotificationManager
import expo.modules.alipush.notification.AlipushNotificationResponse
import expo.modules.alipush.notification.NotificationSerializer
import expo.modules.alipush.notification.NotificationTransformer
import expo.modules.alipush.notification.interfaces.AlipushNotificationListener
import expo.modules.alipush.notification.trigger.AlipushNotificationTrigger
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.notifications.notifications.emitting.NotificationsEmitter
import expo.modules.notifications.notifications.interfaces.NotificationListener
import expo.modules.notifications.notifications.interfaces.NotificationManager
import expo.modules.notifications.notifications.model.NotificationResponse

private const val NEW_MESSAGE_EVENT_NAME = "onDidReceiveAlipushNotification"
private const val NEW_RESPONSE_EVENT_NAME = "onDidReceiveAlipushNotificationResponse"

class AlipushNotificationEmitter : Module(), AlipushNotificationListener, NotificationListener {

    private val notificationEmitter: NotificationsEmitter?
        get() = appContext.registry.getModule() ?: appContext.legacyModule()

    private lateinit var notificationTransformer: NotificationTransformer

    private lateinit var expoNotificationManager: NotificationManager

    private var lastNotificationResponseBundle: Bundle? = null

    override fun definition() = ModuleDefinition {
        Name("AlipushNotificationEmitter")

        Events(
            NEW_MESSAGE_EVENT_NAME,
            NEW_RESPONSE_EVENT_NAME
        )

        OnCreate {
            notificationTransformer =
                NotificationTransformer(requireNotNull(appContext.reactContext))

            AlipushNotificationManager.addListener(this@AlipushNotificationEmitter)

            expoNotificationManager = requireNotNull(
                appContext.legacyModuleRegistry.getSingletonModule(
                    "NotificationManager",
                    NotificationManager::class.java
                )
            )
            expoNotificationManager.addListener(this@AlipushNotificationEmitter)
        }

        OnDestroy {
            AlipushNotificationManager.removeListener(this@AlipushNotificationEmitter)

            expoNotificationManager.removeListener(this@AlipushNotificationEmitter)
        }

        AsyncFunction<Bundle?>("getLastAlipushNotificationResponse") {
            lastNotificationResponseBundle
        }
    }

    override fun onNotificationReceived(notification: AlipushNotification) {
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

    override fun onForegroundNotificationReceived(notification: AlipushForegroundNotification) {
        // foreground notification is also emit and handle by expo-notifications but with incorrect
        // trigger type, we send a correctly serialized event here and do replace in js side
        notificationTransformer.toExpoNotification(notification).also {
            sendEvent(
                NEW_MESSAGE_EVENT_NAME,
                NotificationSerializer.toBundle(it)
            )
        }
    }

    override fun onNotificationResponseReceived(response: AlipushNotificationResponse) {
        // this events are triggered by notification create by aliyun sdk
        // another event should has send by expo-notifications but with incorrect properties
        notificationTransformer.toExpoNotificationResponse(response).also {
            sendEvent(
                NEW_RESPONSE_EVENT_NAME,
                NotificationSerializer.toBundle(it)
                    .also { responseBundle -> lastNotificationResponseBundle = responseBundle }
            )
        }
    }

    /* Expo Notification Events */

    override fun onNotificationResponseReceived(response: NotificationResponse?): Boolean {
        return response.takeIf { it?.notification?.notificationRequest?.trigger is AlipushNotificationTrigger }
            ?.let {
                Log.d(
                    "expo-alipush",
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