package expo.modules.aliyun.push.notification

import android.os.Parcel
import android.os.Parcelable

open class AliyunForegroundNotification : AliyunNotification {

    private val openType: Int
    private val openActivity: String?
    private val openUrl: String?

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

    companion object CREATOR : Parcelable.Creator<AliyunForegroundNotification> {
        override fun createFromParcel(parcel: Parcel): AliyunForegroundNotification {
            return AliyunForegroundNotification(parcel)
        }

        override fun newArray(size: Int): Array<AliyunForegroundNotification?> {
            return arrayOfNulls(size)
        }
    }


}