package expo.modules.aliyun.push.notification

import expo.modules.aliyun.push.notification.interfaces.AliyunNotificationListener
import java.lang.ref.WeakReference
import java.util.WeakHashMap

object AliyunNotificationManager {
    private val listenerReferenceMap =
        WeakHashMap<AliyunNotificationListener, WeakReference<AliyunNotificationListener>>()

    private val listeners get() = listenerReferenceMap.values.mapNotNull { it.get() }

    private val pendingNotificationResponses = mutableListOf<AliyunNotificationResponse>()

    fun addListener(listener: AliyunNotificationListener) {
        if (!listenerReferenceMap.containsKey(listener)) {
            listenerReferenceMap[listener] = WeakReference(listener)
            if (pendingNotificationResponses.isNotEmpty()) {
                pendingNotificationResponses.forEach(listener::onNotificationResponseReceived)
            }
        }
    }

    fun removeListener(listener: AliyunNotificationListener) {
        listenerReferenceMap.remove(listener)
    }

    fun receive(notification: AliyunNotification) {
        listeners.forEach { it.onNotificationReceived(notification) }
    }

    fun receive(notification: AliyunForegroundNotification) {
        listeners.forEach { it.onForegroundNotificationReceived(notification) }
    }

    fun receive(notificationResponse: AliyunNotificationResponse) {
        if (listenerReferenceMap.isEmpty()) {
            pendingNotificationResponses.add(notificationResponse)
        } else {
            listeners.forEach {
                it.onNotificationResponseReceived(notificationResponse)
            }
        }
    }
}