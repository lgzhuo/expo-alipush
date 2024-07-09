package expo.modules.alipush

import android.app.Application
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import expo.modules.core.interfaces.ApplicationLifecycleListener

class AlipushSetup: ApplicationLifecycleListener {

    override fun onCreate(application: Application?) {
        PushServiceFactory.init(application);
    }
}