package expo.modules.alipush.notification.interfaces

import expo.modules.alipush.notification.AlipushForegroundNotification
import expo.modules.alipush.notification.AlipushNotification
import expo.modules.alipush.notification.AlipushNotificationResponse

interface AlipushNotificationListener {
    fun onNotificationResponseReceived(response: AlipushNotificationResponse) {}

    fun onNotificationReceived(notification: AlipushNotification) {}

    fun onForegroundNotificationReceived(notification: AlipushForegroundNotification) {}
}