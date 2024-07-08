package expo.modules.aliyun.push.notification

import android.os.Parcel
import android.os.Parcelable
import java.util.Date
import java.util.UUID

open class AliyunNotification private constructor(
    open val title: String?,
    open val summary: String?,
    open val extra: AliyunNotificationExtra,
    open val date: Date = Date()
) : Parcelable {

    open val identifier: String by lazy {
        extra.get("_ALIYUN_NOTIFICATION_ID_") ?: UUID.randomUUID().toString()
    }

    constructor(
        title: String?,
        summary: String?,
        extraMap: Map<String, String>?,
    ) : this(
        title, summary, AliyunNotificationExtra(extraMap)
    )

    constructor(title: String?, summary: String?, extraMap: String?) : this(
        title,
        summary,
        AliyunNotificationExtra(extraMap),
    )

    protected constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<AliyunNotificationExtra>(CREATOR::class.java.classLoader)!!,
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(summary)
        parcel.writeParcelable(extra, flags)
        parcel.writeLong(date.time)
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

    fun getExtra(key: String) = extra.get(key)

}