package com.sanket.audiorecorder

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanket.audiorecorder.databinding.ActivityAudioFilesBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AudioFilesActivity : AppCompatActivity(), AudioFileAdapter.OnItemClickListener {
    private lateinit var _binding : ActivityAudioFilesBinding
    lateinit var context: Context
    private lateinit var adapter: AudioFileAdapter
    private lateinit var audioList: ArrayList<AudioFileClass>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mediaStorage : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAudioFilesBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        context = this@AudioFilesActivity

        audioList = ArrayList<AudioFileClass>()


        // mydir = this.getDir("mydir", Context.MODE_PRIVATE);
        mediaStorage = File(Environment.getExternalStorageDirectory(), "recorder_app_hello")

        getSomeMusic()

    }
    private fun getSomeMusic() {
        if (mediaStorage.listFiles() == null) {
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show()
        } else {
            if(mediaStorage.listFiles().isNotEmpty()){
                val files: Array<File> = mediaStorage.listFiles()
                for (file in files) {

                    val audio = AudioFileClass(file.name,
                            getDuration(file),
                            getNiceDate(file),
                            Uri.parse(file.path),
                            getStorage(file.length().toString()))
                    audioList.add(audio)

                }
            }

        }

        adapter = AudioFileAdapter(context, audioList)
        linearLayoutManager = LinearLayoutManager(this)
        _binding.rvAudioFiles.adapter = adapter
        _binding.rvAudioFiles.layoutManager = linearLayoutManager

    }

    private fun getDuration(file: File): String? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.absolutePath)
        val durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return formateMilliSeccond(durationStr!!.toLong())
    }



    override fun onItemClick(item: AudioFileClass, v: View?) {

        Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show()
        startActivity(Intent(context, PlayActivity::class.java)
            .putExtra("AudioFile", item)
            .putParcelableArrayListExtra("Array", audioList))

    }

    private fun getStorage(fileSize: String): String {
        val size = fileSize.toDouble().div(1024.0 * 1024.0)
        val sizeString = String.format("%.2f", size)
        val sizeKB = fileSize.toDouble().div(1024.0)
        val sizeKBString = String.format("%.2f", sizeKB)
        return if(size < 1){

            "$sizeKBString KB"
        } else{
            "$sizeString MB"
        }


    }
    private fun getNiceDate(file : File):String{
        //val lastModDate = Date(file.lastModified())
        val formatter = SimpleDateFormat("dd/MM/yyyy , hh:mm")
        return formatter.format(file.lastModified())
    }

    private fun formateMilliSeccond(milliseconds: Long): String? {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString
    }
}