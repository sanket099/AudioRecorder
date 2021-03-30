package com.sanket.audiorecorder

import android.net.Uri

class AudioFileClass {

    private var title: String? = null
    private var duration: String? = null
    private var date: String? = null
    private var uri: Uri? = null
    private var storage : String? = null

    constructor(title: String?, duration: String?, date: String?, uri: Uri?, storage: String?) {
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

    fun getUri(): Uri? {
        return uri
    }

    fun setUri(uri: Uri?) {
        this.uri = uri
    }
    fun getStorage(): String? {
        return storage
    }

    fun setStorage(storage: String?) {
        this.storage = storage
    }



}