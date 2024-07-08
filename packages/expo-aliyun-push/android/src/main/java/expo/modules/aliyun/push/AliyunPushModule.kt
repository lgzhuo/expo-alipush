package expo.modules.aliyun.push

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder
import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import java.util.Locale

class AliyunPushModule : Module() {

    companion object {
        private const val TAG = "Expo AliyunPush"

        // push register is effective in the application lifecycle
        private var registerStatus = RegisterStatus.None
    }

    private val context: Context
        get() = appContext.reactContext ?: throw Exceptions.ReactContextLost()

    override fun definition() = ModuleDefinition {
        Name("AliyunPush")

        Constants(
            "REMIND_TYPE" to mapOf(
                "SOUND" to BasicCustomPushNotification.REMIND_TYPE_SOUND,
                "VIBRATE" to BasicCustomPushNotification.REMIND_TYPE_VIBRATE,
                "SILENT" to BasicCustomPushNotification.REMIND_TYPE_SILENT,
                "VIBRATE_AND_SOUND" to BasicCustomPushNotification.REMIND_TYPE_VIBRATE_AND_SOUND
            ),
        )

        AsyncFunction("register") { promise: Promise ->
            if (registerStatus == RegisterStatus.Registering) {
                promise.reject(
                    "E_REGISTER_REPEAT",
                    "Another register call is in progress, await the previous call",
                    null
                )
                return@AsyncFunction
            } else if (registerStatus == RegisterStatus.Registered) {
                promise.resolve(null)
                return@AsyncFunction
            }
            registerStatus = RegisterStatus.Registering
            PushServiceFactory.getCloudPushService()
                .register(context, object : AsyncCallback(promise, "register") {
                    override fun onFailed(errorCode: String?, errorMessage: String?) {
                        super.onFailed(errorCode, errorMessage)
                        registerStatus = RegisterStatus.None
                    }

                    override fun onSuccess(response: String?) {
                        super.onSuccess(response)
                        registerStatus = RegisterStatus.Registered
                    }
                })
        }

        Function("getDeviceId") {
            PushServiceFactory.getCloudPushService().deviceId
        }

        AsyncFunction("bindAccount") { account: String, promise: Promise ->
            PushServiceFactory.getCloudPushService()
                .bindAccount(account, AsyncCallback(promise, "bindAccount", account))
        }

        AsyncFunction("unbindAccount") { promise: Promise ->
            PushServiceFactory.getCloudPushService()
                .unbindAccount(AsyncCallback(promise, "unbindAccount"))
        }

        AsyncFunction("setCustomNotificationConfig") { id: Int, config: CustomNotificationConfig, promise: Promise ->
            CustomNotificationBuilder.getInstance()
                .setCustomNotification(id, BasicCustomPushNotification().also { custom ->
                    config.isBuildWhenAppInForeground?.let {
                        custom.isBuildWhenAppInForeground = it
                    }
                    config.isServerOptionFirst?.let {
                        custom.isServerOptionFirst = it
                    }
                    config.remindType?.let {
                        custom.remindType = it
                    }
                    config.notificationFlags?.let {
                        custom.notificationFlags = it
                    }
                }).also {
                    if (it) {
                        promise.resolve()
                    } else {
                        promise.reject("E_SET_CUSTOM_NOTIFICATION_CONFIG", null, null)
                    }
                }
        }

    }

    class CustomNotificationConfig : Record {
        @Field
        val isBuildWhenAppInForeground: Boolean? = null

        @Field
        val isServerOptionFirst: Boolean? = null

        @Field
        val remindType: Int? = null

        @Field
        val notificationFlags: Int? = null
    }

    private open class AsyncCallback(
        val promise: Promise,
        val func: String,
        val stringifyArg: String? = null
    ) : CommonCallback {
        val mErrorCode: String
            get() = func.split("[A-Z]".toRegex())
                .joinToString("_", "E_") { it.uppercase(Locale.getDefault()) }

        override fun onSuccess(response: String?) {
            Log.i(TAG, "$func ${stringifyArg?.let { "[$it]" }} success $response")
            promise.resolve(null);
        }

        override fun onFailed(errorCode: String?, errorMessage: String?) {
            Log.i(
                TAG,
                "$func ${stringifyArg?.let { "[$it]" }} failed -- code:$errorCode --  message:$errorMessage"
            )
            promise.reject(mErrorCode, "$errorCode:$errorMessage", null)
        }

    }

    enum class RegisterStatus {
        None,
        Registering,
        Registered
    }
}
