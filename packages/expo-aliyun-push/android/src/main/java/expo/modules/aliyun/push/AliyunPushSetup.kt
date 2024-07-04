package expo.modules.aliyun.push

import android.app.Application
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import expo.modules.core.interfaces.ApplicationLifecycleListener

class AliyunPushSetup: ApplicationLifecycleListener {

    override fun onCreate(application: Application?) {
        PushServiceFactory.init(application);
    }
}