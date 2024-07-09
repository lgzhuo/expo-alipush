package expo.modules.alipush.notification

import expo.modules.alipush.notification.interfaces.AlipushNotificationListener
import java.lang.ref.WeakReference
import java.util.WeakHashMap

object AlipushNotificationManager {
    private val listenerReferenceMap =
        WeakHashMap<AlipushNotificationListener, WeakReference<AlipushNotificationListener>>()

    private val listeners get() = listenerReferenceMap.values.mapNotNull { it.get() }

    private val pendingNotificationResponses = mutableListOf<AlipushNotificationResponse>()

    fun addListener(listener: AlipushNotificationListener) {
        if (!listenerReferenceMap.containsKey(listener)) {
            listenerReferenceMap[listener] = WeakReference(listener)
            if (pendingNotificationResponses.isNotEmpty()) {
                pendingNotificationResponses.forEach(listener::onNotificationResponseReceived)
            }
        }
    }

    fun removeListener(listener: AlipushNotificationListener) {
        listenerReferenceMap.remove(listener)
    }

    fun receive(notification: AlipushNotification) {
        listeners.forEach { it.onNotificationReceived(notification) }
    }

    fun receive(notification: AlipushForegroundNotification) {
        listeners.forEach { it.onForegroundNotificationReceived(notification) }
    }

    fun receive(notificationResponse: AlipushNotificationResponse) {
        if (listenerReferenceMap.isEmpty()) {
            pendingNotificationResponses.add(notificationResponse)
        } else {
            listeners.forEach {
                it.onNotificationResponseReceived(notificationResponse)
            }
        }
    }
}