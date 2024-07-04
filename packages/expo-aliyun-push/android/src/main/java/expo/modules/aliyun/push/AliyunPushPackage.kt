package expo.modules.aliyun.push

import android.content.Context
import expo.modules.core.interfaces.ApplicationLifecycleListener
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener
import expo.modules.core.interfaces.SingletonModule
import expo.modules.aliyun.push.notification.AliyunNotificationManager
import expo.modules.aliyun.push.popup.PopupPushDelegate

class AliyunPushPackage : Package {
    override fun createApplicationLifecycleListeners(context: Context?): List<ApplicationLifecycleListener> {
        return listOf(AliyunPushSetup(), AliyunPushThirdSetup())
    }

    override fun createReactActivityLifecycleListeners(activityContext: Context): List<ReactActivityLifecycleListener> {
        return listOf(PopupPushDelegate(activityContext))
    }

    override fun createSingletonModules(context: Context?): List<SingletonModule> {
        return listOf(AliyunNotificationManager())
    }
}