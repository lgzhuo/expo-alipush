package expo.modules.aliyun.push.notification.trigger

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import expo.modules.notifications.notifications.interfaces.NotificationTrigger
import expo.modules.aliyun.push.notification.AliyunNotification

class AliyunNotificationTrigger(private val notification: AliyunNotification) : NotificationTrigger {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable<AliyunNotification>(AliyunNotificationTrigger::class.java.classLoader)!!
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNotificationChannel(): String? {
        return notification.getChannel() ?: super.getNotificationChannel()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(notification, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AliyunNotificationTrigger> {
        override fun createFromParcel(parcel: Parcel): AliyunNotificationTrigger {
            return AliyunNotificationTrigger(parcel)
        }

        override fun newArray(size: Int): Array<AliyunNotificationTrigger?> {
            return arrayOfNulls(size)
        }
    }
}