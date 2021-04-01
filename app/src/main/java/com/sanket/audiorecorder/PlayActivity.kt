package com.sanket.audiorecorder

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.sanket.audiorecorder.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityPlayBinding
    private var audioFile : AudioFileClass? = null

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        getObject()



        _binding.ivNext.setOnClickListener(View.OnClickListener {

        })

        _binding.ivPlay.setOnClickListener(View.OnClickListener {


        })


        _binding.ivPrev.setOnClickListener(View.OnClickListener {

        })

        _binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

    }

    private fun getObject() {

        audioFile = intent.getParcelableExtra<AudioFileClass>("AudioFile")
        val audioArray = intent.getParcelableArrayListExtra<AudioFileClass>("Array")
        if(audioFile != null && audioArray!= null){
            _binding.tvDate.text = audioFile?.getDate()
            _binding.tvTitle.text = audioFile?.getTitle()

        }

        else{
            _binding.ivPlay.isEnabled = false
            _binding.ivNext.isEnabled = false
            _binding.ivPrev.isEnabled = false
            _binding.seekBar.isEnabled = false
        }
    }
}