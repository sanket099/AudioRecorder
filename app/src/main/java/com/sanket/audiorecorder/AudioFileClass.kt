package com.sanket.audiorecorder

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class AudioFileClass() : Parcelable {

    private var title: String? = null
    private var duration: String? = null
    private var date: String? = null
    private var uri: String? = null
    private var storage : String? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        duration = parcel.readString()
        date = parcel.readString()
        uri = parcel.readString()
        storage = parcel.readString()
    }

    constructor(title: String?, duration: String?, date: String?, uri: String?, storage: String?) : this() {
        this.title = title
        this.duration = duration
        this.date = date
        this.uri = uri
        this.storage = storage
    }


    fun getTitle() : String?{
        return title
    }
    fun getDuration() : String?{
        return duration
    }
    fun getDate() : String?{
        return date
    }

    fun getUri(): String? {
        return uri
    }

    fun setUri(uri: String?) {
        this.uri = uri
    }
    fun getStorage(): String? {
        return storage
    }

    fun setStorage(storage: String?) {
        this.storage = storage
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(duration)
        parcel.writeString(date)
        parcel.writeString(uri)
        parcel.writeString(storage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioFileClass> {
        override fun createFromParcel(parcel: Parcel): AudioFileClass {
            return AudioFileClass(parcel)
        }

        override fun newArray(size: Int): Array<AudioFileClass?> {
            return arrayOfNulls(size)
        }
    }


}