package expo.modules.alipush.notification

import android.os.Parcel
import android.os.Parcelable
import java.util.Date
import java.util.UUID

open class AlipushNotification private constructor(
    open val title: String?,
    open val summary: String?,
    open val extra: AlipushNotificationExtra,
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
        title, summary, AlipushNotificationExtra(extraMap)
    )

    constructor(title: String?, summary: String?, extraMap: String?) : this(
        title,
        summary,
        AlipushNotificationExtra(extraMap),
    )

    protected constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<AlipushNotificationExtra>(CREATOR::class.java.classLoader)!!,
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

    companion object CREATOR : Parcelable.Creator<AlipushNotification> {
        override fun createFromParcel(parcel: Parcel): AlipushNotification {
            return AlipushNotification(parcel)
        }

        override fun newArray(size: Int): Array<AlipushNotification?> {
            return arrayOfNulls(size)
        }
    }

    fun getExtra(key: String) = extra.get(key)

}