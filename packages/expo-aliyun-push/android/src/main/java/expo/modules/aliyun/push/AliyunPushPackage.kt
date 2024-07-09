package expo.modules.aliyun.push

import android.content.Context
import expo.modules.aliyun.push.popup.PopupPushDelegate
import expo.modules.core.interfaces.ApplicationLifecycleListener
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener

class AliyunPushPackage : Package {
    override fun createApplicationLifecycleListeners(context: Context?): List<ApplicationLifecycleListener> {
        return listOf(AliyunPushSetup(), AliyunPushThirdSetup())
    }

    override fun createReactActivityLifecycleListeners(activityContext: Context): List<ReactActivityLifecycleListener> {
        return listOf(PopupPushDelegate(activityContext))
    }
}