package expo.modules.aliyun.push

import android.app.Application
import android.content.pm.PackageManager
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister
import com.alibaba.sdk.android.push.register.MiPushRegister
import expo.modules.core.interfaces.ApplicationLifecycleListener

class AliyunPushThirdSetup : ApplicationLifecycleListener {

    override fun onCreate(application: Application) {
        if (BuildConfig.XIAOMI_PUSH_ENABLED) {
            val applicationInfo = application.packageManager.getApplicationInfo(
                application.packageName,
                PackageManager.GET_META_DATA
            )
            val appID = applicationInfo.metaData.getString("com.xiaomi.app.id")!!
            val appKey = applicationInfo.metaData.getString("com.xiaomi.app.key")!!
            MiPushRegister.register(application, appID, appKey)
        }

        if (BuildConfig.HUAWEI_PUSH_ENABLED) {
            HuaWeiRegister.register(application)
        }
    }
}