package expo.modules.alipush

import android.content.Context
import expo.modules.alipush.popup.PopupPushDelegate
import expo.modules.core.interfaces.ApplicationLifecycleListener
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener

class AlipushPackage : Package {
    override fun createApplicationLifecycleListeners(context: Context?): List<ApplicationLifecycleListener> {
        return listOf(AlipushSetup(), AlipushThirdSetup())
    }

    override fun createReactActivityLifecycleListeners(activityContext: Context): List<ReactActivityLifecycleListener> {
        return listOf(PopupPushDelegate(activityContext))
    }
}