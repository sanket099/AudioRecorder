package com.sanket.audiorecorder

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanket.audiorecorder.databinding.ActivityAudioFilesBinding
import java.io.File

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
                    val audio = AudioFileClass(file.name,file.length().toString(),
                            file.lastModified().toString(), Uri.parse(file.path), file.totalSpace.toString())
                    audioList.add(audio)

                }
            }

        }

        adapter = AudioFileAdapter(context, audioList)
        linearLayoutManager = LinearLayoutManager(this)
        _binding.rvAudioFiles.adapter = adapter
        _binding.rvAudioFiles.layoutManager = linearLayoutManager

    }

    override fun onItemClick(itemId: Long , v : View?) {

        Toast.makeText(context, itemId.toString(), Toast.LENGTH_SHORT).show()

    }
}