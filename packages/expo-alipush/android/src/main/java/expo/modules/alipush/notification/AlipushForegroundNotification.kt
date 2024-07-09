package expo.modules.alipush.notification

import android.os.Parcel
import android.os.Parcelable

open class AlipushForegroundNotification : AlipushNotification {

    val openType: Int
    val openActivity: String?
    val openUrl: String?

    constructor(
        title: String?,
        summary: String?,
        extraMap: Map<String, String>?,
        openType: Int,
        openActivity: String?,
        openUrl: String?
    ) : super(title, summary, extraMap) {
        this.openType = openType
        this.openActivity = openActivity
        this.openUrl = openUrl
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.openType = parcel.readInt()
        this.openActivity = parcel.readString()
        this.openUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(openType)
        parcel.writeString(openActivity)
        parcel.writeString(openUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlipushForegroundNotification> {
        override fun createFromParcel(parcel: Parcel): AlipushForegroundNotification {
            return AlipushForegroundNotification(parcel)
        }

        override fun newArray(size: Int): Array<AlipushForegroundNotification?> {
            return arrayOfNulls(size)
        }
    }


}