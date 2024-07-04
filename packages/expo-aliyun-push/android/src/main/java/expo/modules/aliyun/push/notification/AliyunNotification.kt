package expo.modules.aliyun.push.notification

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import expo.modules.notifications.notifications.interfaces.NotificationTrigger
import expo.modules.notifications.notifications.model.Notification
import expo.modules.notifications.notifications.model.NotificationContent
import expo.modules.notifications.notifications.model.NotificationRequest
import expo.modules.aliyun.push.notification.trigger.AliyunNotificationTrigger
import org.json.JSONObject
import java.util.Date
import java.util.UUID

open class AliyunNotification private constructor(
    protected val title: String?,
    protected val summary: String?,
    protected val extra: Bundle?
) : Parcelable {

    private val date = Date()
    private val fallbackIdentifier: String by lazy {
        UUID.randomUUID().toString()
    }

    constructor(title: String?, summary: String?, extraMap: Map<String, String>?) : this(
        title,
        summary,
        extraMap?.entries?.let {
            Bundle().also { extra ->
                it.forEach { entity ->
                    extra.putString(
                        entity.key,
                        entity.value
                    )
                }
            }
        }) {
    }

    protected constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readBundle(CREATOR::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(summary)
        parcel.writeBundle(extra)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AliyunNotification> {
        override fun createFromParcel(parcel: Parcel): AliyunNotification {
            return AliyunNotification(parcel)
        }

        override fun newArray(size: Int): Array<AliyunNotification?> {
            return arrayOfNulls(size)
        }
    }

    fun getChannel(): String? {
        return extra?.getString("channel")
    }

    fun getIdentifier(): String {
        return this.extra?.getString("_ALIYUN_NOTIFICATION_ID_") ?: this.fallbackIdentifier
    }

    fun getDate(): Date {
        return this.date
    }

    fun getBody(): JSONObject {
        return JSONObject().also {
            extra?.keySet()?.forEach { key ->
                it.put(key, extra.getString(key))
            }
        }
    }

    fun createExpoNotificationContentBuilder(): NotificationContent.Builder {
        return NotificationContent.Builder().setTitle(title).setText(summary).setBody(getBody())
    }

    fun createExpoNotificationTrigger(): NotificationTrigger {
        return AliyunNotificationTrigger(this)
    }

    fun createExpoNotificationRequest(): NotificationRequest {
        return NotificationRequest(
            getIdentifier(),
            createExpoNotificationContentBuilder().build(),
            createExpoNotificationTrigger()
        )
    }

    fun createExpoNotification(): Notification {
        return Notification(createExpoNotificationRequest(), getDate())
    }
}