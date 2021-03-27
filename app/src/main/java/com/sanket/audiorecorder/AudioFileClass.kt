package com.sanket.audiorecorder

import android.net.Uri

class AudioFileClass {
    private var title: String? = null
    private var duration: String? = null
    private var date: String? = null
    private var uri: Uri? = null

    constructor(title: String?, duration: String?, date: String?, uri: Uri?) {
        this.title = title
        this.duration = duration
        this.date = date
        this.uri = uri
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

}