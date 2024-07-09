package expo.modules.alipush.notification.trigger

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import expo.modules.notifications.notifications.interfaces.NotificationTrigger
import expo.modules.alipush.notification.AlipushNotification

class AlipushNotificationTrigger(val notification: AlipushNotification) : NotificationTrigger {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable<AlipushNotification>(AlipushNotificationTrigger::class.java.classLoader)!!
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNotificationChannel(): String? {
        return notification.getExtra("channelId")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(notification, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlipushNotificationTrigger> {
        override fun createFromParcel(parcel: Parcel): AlipushNotificationTrigger {
            return AlipushNotificationTrigger(parcel)
        }

        override fun newArray(size: Int): Array<AlipushNotificationTrigger?> {
            return arrayOfNulls(size)
        }
    }
}