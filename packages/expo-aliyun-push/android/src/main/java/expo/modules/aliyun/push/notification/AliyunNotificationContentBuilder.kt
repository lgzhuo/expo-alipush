package expo.modules.aliyun.push.notification

import android.content.Context
import android.graphics.Color
import android.util.Log
import expo.modules.core.utilities.ifNull
import expo.modules.notifications.notifications.SoundResolver
import expo.modules.notifications.notifications.enums.NotificationPriority
import expo.modules.notifications.notifications.model.NotificationContent
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

open class AliyunNotificationContentBuilder(private val context: Context) :
    NotificationContent.Builder() {

    companion object {
        private const val TITLE_KEY = "title";
        private const val TEXT_KEY = "message";
        private const val SUBTITLE_KEY = "subtitle";
        private const val SOUND_KEY = "sound";
        private const val BODY_KEY = "body";
        private const val VIBRATE_KEY = "vibrate";
        private const val PRIORITY_KEY = "priority";
        private const val BADGE_KEY = "badge";
        private const val COLOR_KEY = "color";
        private const val AUTO_DISMISS_KEY = "autoDismiss";
        private const val CATEGORY_IDENTIFIER_KEY = "categoryId";
        private const val STICKY_KEY = "sticky";
    }

    private val mSoundResolver by lazy {
        SoundResolver(context)
    }

    fun setExtra(extra: AliyunNotificationExtra): AliyunNotificationContentBuilder {
        // don't override title with null
        getTitle(extra)?.also { setTitle(it) }
        // don't override text with null
        setSubtitle(getSubtitle(extra))
        getText(extra)?.also { setText(it) }
        setBody(getBody(extra))
        setPriority(getPriority(extra))
        setBadgeCount(getBadgeCount(extra))
        setColor(getColor(extra))
        getAutoDismiss(extra)?.also { setAutoDismiss(it) }
        setCategoryId(getCategoryId(extra))
        getSticky(extra)?.also { setSticky(it) }

        if (shouldPlayDefaultSound(extra)) {
            useDefaultSound()
        } else {
            setSound(getSound(extra))
        }

        if (shouldUseDefaultVibrationPattern(extra)) {
            useDefaultSound()
        } else {
            setVibrationPattern(getVibrationPattern(extra))
        }

        return this
    }

    protected fun getTitle(extra: AliyunNotificationExtra) = extra.get(TITLE_KEY)

    protected fun getSubtitle(extra: AliyunNotificationExtra) = extra.get(SUBTITLE_KEY)

    protected fun getText(extra: AliyunNotificationExtra) = extra.get(TEXT_KEY)

    protected fun getBody(extra: AliyunNotificationExtra) = extra.get(BODY_KEY)?.let {
        try {
            JSONObject(it)
        } catch (e: Exception) {
            null
        }
    }

    protected fun getPriority(extra: AliyunNotificationExtra) =
        extra.get(PRIORITY_KEY)?.let { NotificationPriority.fromEnumValue(it) }

    protected fun getBadgeCount(extra: AliyunNotificationExtra) =
        extra.get(BADGE_KEY)?.toIntOrNull()

    protected fun getColor(extra: AliyunNotificationExtra) = extra.get(COLOR_KEY)?.let {
        try {
            Color.parseColor(it)
        } catch (e: IllegalArgumentException) {
            Log.e("expo-aliyun-push", "Could not have parsed color passed in notification.")
        }
    }

    protected fun getAutoDismiss(extra: AliyunNotificationExtra) =
        extra.get(AUTO_DISMISS_KEY)?.toBooleanStrictOrNull()

    protected fun getCategoryId(extra: AliyunNotificationExtra) = extra.get(CATEGORY_IDENTIFIER_KEY)

    protected fun getSticky(extra: AliyunNotificationExtra) = extra.get(STICKY_KEY)?.toBooleanStrictOrNull()

    protected fun getSound(extra: AliyunNotificationExtra) = extra.get(SOUND_KEY)
        ?.let {
            it.takeIf { it.toBooleanStrictOrNull() != null }
                ?.let { value -> mSoundResolver.resolve(value) }
        }

    protected fun shouldPlayDefaultSound(extra: AliyunNotificationExtra) =
        extra.get(SOUND_KEY)
            .let { it?.toBooleanStrictOrNull().ifNull { getSound(extra) != null } }

    protected fun shouldUseDefaultVibrationPattern(extra: AliyunNotificationExtra) =
        extra.get(VIBRATE_KEY)?.toBooleanStrictOrNull() ?: true

    protected fun getVibrationPattern(extra: AliyunNotificationExtra) = extra.get(VIBRATE_KEY)?.let {
        try {
            JSONArray(it).let { jsonArray ->
                LongArray(jsonArray.length()) { index ->
                    jsonArray.getLong(
                        index
                    )
                }
            }
        } catch (e: JSONException) {
            Log.w(
                "expo-aliyun-push",
                "Failed to set custom vibration pattern from the notification: ${e.message}"
            );
            null
        }
    }
}