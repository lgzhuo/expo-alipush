package expo.modules.aliyun.push.notification

import expo.modules.core.interfaces.SingletonModule
import expo.modules.aliyun.push.notification.interfaces.AliyunNotificationListener
import java.lang.ref.WeakReference
import java.util.WeakHashMap

class AliyunNotificationManager() : SingletonModule {

    companion object {
        const val SINGLETON_NAME = "AliyunNotificationManager"

        private val instanceReferenceMap =
            WeakHashMap<AliyunNotificationManager, WeakReference<AliyunNotificationManager>>()

        val instances get() = instanceReferenceMap.values.mapNotNull { it.get() }

        fun addInstance(instance: AliyunNotificationManager) {
            if (!instanceReferenceMap.containsKey(instance)) {
                instanceReferenceMap[instance] = WeakReference(instance)
            }
        }
    }

    init {
        addInstance(this)
    }

    private val listenerReferenceMap =
        WeakHashMap<AliyunNotificationListener, WeakReference<AliyunNotificationListener>>()

    private val listeners get() = listenerReferenceMap.values.mapNotNull { it.get() }

    override fun getName(): String {
        return SINGLETON_NAME
    }

    fun addListener(listener: AliyunNotificationListener) {
        if (!listenerReferenceMap.containsKey(listener)) {
            listenerReferenceMap[listener] = WeakReference(listener)
        }
    }

    fun removeListener(listener: AliyunNotificationListener) {
        listenerReferenceMap.remove(listener)
    }

    fun onNotificationResponseReceived(response: AliyunNotificationResponse) {
        listeners.forEach {
            it.onNotificationResponseReceived(response)
        }
    }

    fun onNotificationReceived(notification: AliyunNotification) {
        listeners.forEach {
            it.onNotificationReceived(notification)
        }
    }

    fun onForegroundNotificationReceived(notification: AliyunForegroundNotification) {
        listeners.forEach {
            it.onForegroundNotificationReceived(notification)
        }
    }


}