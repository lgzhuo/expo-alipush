package expo.modules.aliyun.push.notification.emitting

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.notifications.notifications.emitting.NotificationsEmitter
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.notification.NotificationTransformer
import expo.modules.aliyun.push.notification.interfaces.AliyunNotificationListener

class AliyunNotificationEmitter : Module(), AliyunNotificationListener {

    private lateinit var notificationManager: AliyunNotificationManager
    private val notificationEmitter: NotificationsEmitter?
        get() = appContext.registry.getModule() ?: appContext.legacyModule()

    private var notificationTransformer: NotificationTransformer? = null

    override fun definition() = ModuleDefinition {
        OnCreate {
            notificationManager = requireNotNull(
                appContext.legacyModuleRegistry.getSingletonModule(
                    AliyunNotificationManager.SINGLETON_NAME,
                    AliyunNotificationManager::class.java
                )
            )
            notificationManager.addListener(this@AliyunNotificationEmitter)

            notificationTransformer =
                NotificationTransformer(requireNotNull(appContext.reactContext))
        }

        OnDestroy {
            notificationManager.removeListener(this@AliyunNotificationEmitter)

            notificationTransformer = null
        }
    }

    override fun onNotificationReceived(notification: AliyunNotification) {
        // use expo notification emitter as the emitter delegate
        notificationTransformer?.toExpoNotification(notification)
            ?.also { notificationEmitter?.onNotificationReceived(it) }

    }
}