package expo.modules.aliyun.push.notification.interfaces

import expo.modules.aliyun.push.notification.AliyunForegroundNotification
import expo.modules.aliyun.push.notification.AliyunNotification
import expo.modules.aliyun.push.notification.AliyunNotificationResponse

interface AliyunNotificationListener {
    fun onNotificationResponseReceived(response: AliyunNotificationResponse) {}

    fun onNotificationReceived(notification: AliyunNotification) {}

    fun onForegroundNotificationReceived(notification: AliyunForegroundNotification) {}
}