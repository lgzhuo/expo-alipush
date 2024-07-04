package expo.modules.aliyun.push.service

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage

class AliyunMessageReceiver : MessageReceiver() {

    companion object {
        const val TAG = "expo-aliyun-push"
    }

    private val delegate: AliyunMessageDelegate by lazy {
        AliyunMessageDelegate()
    }

    override fun onNotificationOpened(
        context: Context,
        title: String?,
        summary: String?,
        extraMap: String?
    ) {
        Log.d(TAG, "onNotificationOpened title:$title summary:$summary extraMap:$extraMap")
        delegate.onNotificationOpened(context, title, summary, extraMap)
    }

    override fun onNotificationRemoved(context: Context?, messageId: String?) {
        Log.d(TAG, "onNotificationRemoved messageId:$messageId")
    }

    override fun onNotification(
        context: Context,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?
    ) {
        Log.d(TAG, "onNotification title:$title summary:$summary extraMap:$extraMap")
        delegate.onNotification(context, title, summary, extraMap)
    }

    override fun onMessage(context: Context?, message: CPushMessage?) {
        Log.d(TAG, "onMessage message:$message")
    }

    override fun onNotificationClickedWithNoAction(
        context: Context,
        title: String?,
        summary: String?,
        extraMap: String?
    ) {
        Log.d(
            TAG,
            "onNotificationClickedWithNoAction title:$title summary:$summary extraMap:$extraMap"
        )
        delegate.onNotificationClickedWithNoAction(context, title, summary, extraMap)
    }

    override fun onNotificationReceivedInApp(
        context: Context?,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?,
        openType: Int,
        openActivity: String?,
        openUrl: String?
    ) {
        Log.d(
            TAG,
            "onNotificationReceivedInApp title:$title summary:$summary extraMap:$extraMap openType:$openType openActivity:$openActivity openUrl:$openUrl"
        )
        delegate.onNotificationReceivedInApp(
            context,
            title,
            summary,
            extraMap,
            openType,
            openActivity,
            openUrl
        )
    }


}