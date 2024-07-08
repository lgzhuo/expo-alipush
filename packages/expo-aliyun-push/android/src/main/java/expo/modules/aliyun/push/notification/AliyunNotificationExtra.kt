package expo.modules.aliyun.push.notification

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class AliyunNotificationExtra(private val extra: Map<String, String>?) : Parcelable {

    constructor(extra: String?) : this(extra?.let {
        try {
            JSONObject(it).let { json ->
                mutableMapOf<String, String>().also { map ->
                    json.keys().forEach { key -> map[key] = json.getString(key) }
                }
            }
        } catch (e: JSONException) {
            Log.e(
                "expo-aliyun-push",
                "Could not have parsed extra passed in notification: ${e.message}"
            )
            null
        }
    })

    constructor(parcel: Parcel) : this(mutableMapOf<String, String>().also {
        parcel.readMap(
            it,
            CREATOR::class.java.classLoader
        )
    }) {
    }

    fun get(key: String) = extra?.get(key)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeMap(extra)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AliyunNotificationExtra> {
        override fun createFromParcel(parcel: Parcel): AliyunNotificationExtra {
            return AliyunNotificationExtra(parcel)
        }

        override fun newArray(size: Int): Array<AliyunNotificationExtra?> {
            return arrayOfNulls(size)
        }
    }
}