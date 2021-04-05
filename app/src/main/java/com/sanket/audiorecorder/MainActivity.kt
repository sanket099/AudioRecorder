package com.sanket.audiorecorder

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sanket.audiorecorder.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var myAudioRecorder: MediaRecorder? = null


    private lateinit var _binding: ActivityMainBinding
    lateinit var context: Context
    private lateinit var audioFile:String
    private lateinit var filePath : String
    var countDownTimer: CountDownTimer? = null
    var second = -1
    var minute:Int = 0
    var hour:Int = 0
    private lateinit var myDirectory : File

    private val RECORD_REQUEST_CODE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        context = this@MainActivity

        if (checkPermssion()) {
            _binding.btnRecord.isEnabled = true
            _binding.btnList.isEnabled = true
            _binding.btnStop.isEnabled = true
            setAudioRecorder()

        } else {
            _binding.btnRecord.isEnabled = false
            _binding.btnList.isEnabled = false
            _binding.btnStop.isEnabled = false

            requestPermission()

        }

        _binding.btnList.setOnClickListener(View.OnClickListener {


            startActivity(Intent(context, AudioFilesActivity::class.java))
        })

        _binding.btnStop.setOnClickListener(View.OnClickListener { //cancel count down timer
            countDownTimer!!.cancel()
            _binding.btnRecord.isEnabled = true
            _binding.btnList.isEnabled = true
            _binding.btnStop.isEnabled = false

            second = -1
            minute = 0
            hour = 0
            _binding.tvDuration.text = "00:00:00"
            if (myAudioRecorder != null) {
                try {
                    //stop myAudioRecorder
                    myAudioRecorder!!.stop()
                    myAudioRecorder!!.reset()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            }

            //creating content resolver and put the values
            val values = ContentValues()
            // values.put(MediaStore.Audio.Media._ID, 12)
            values.put(MediaStore.Audio.Media.DATA, filePath)

            values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp")
            values.put(MediaStore.Audio.Media.TITLE, audioFile)
            //store audio recorder file in the external content uri
            contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        })

        _binding.btnRecord.setOnClickListener(View.OnClickListener {
            _binding.btnRecord.isEnabled = false
            _binding.btnList.isEnabled = false
            _binding.btnStop.isEnabled = true

            try {
                // Create folder to store recordingss

                val dateFormat = SimpleDateFormat("dd_mm_yyyy")
                val date = dateFormat.format(Date())
                audioFile = "REC$date.mp3"
                filePath = myDirectory.absolutePath + File.separator + audioFile
                startAudioRecorder()
            } catch (e: Exception) {
                e.printStackTrace()
                println("error ${e.message}")
            }
            showTimer()
        })


        /*builder.setPositiveButton("Yes"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
            myAudioRecorder!!.stop();
            myAudioRecorder!!.release();
            myAudioRecorder = null;
            isRecording = false
            _binding.btnRecord.setImageResource(R.drawable.mic_24dp)
        }
        //performing negative action
        builder.setNegativeButton("No"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
            isRecording = false
            _binding.btnRecord.setImageResource(R.drawable.mic_24dp)
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()*/
    }

    private fun setAudioRecorder() {

        myAudioRecorder = MediaRecorder()
        myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        myAudioRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//        myAudioRecorder!!.setAudioEncoder(MediaRecorder.getAudioSourceMax());
        myAudioRecorder!!.setAudioEncodingBitRate(16)
        myAudioRecorder!!.setAudioSamplingRate(44100)


        myDirectory = File(Environment.getExternalStorageDirectory(), "recorder_app_hello")
        if (!myDirectory.exists()) {
            myDirectory.mkdirs()
        }
    }



    private fun showTimer() {
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                second++
                _binding.tvDuration.text = recorderTime()
                // recorderTime()
            }

            override fun onFinish() {}
        }
        (countDownTimer as CountDownTimer).start()
    }

    private fun recorderTime(): String {
        if (second == 60) {
            minute++
            second = 0
        }
        if (minute == 60) {
            hour++
            minute = 0
        }
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    private fun startAudioRecorder() {
        setAudioRecorder()
        try {
            myAudioRecorder!!.setOutputFile(filePath)
            myAudioRecorder!!.prepare()
            myAudioRecorder!!.start()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun checkPermssion(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        )

        val permissionStorage = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionRead = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )


        if (permission != PackageManager.PERMISSION_GRANTED
                || permissionStorage != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty()
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED
                        || grantResults[2] != PackageManager.PERMISSION_GRANTED) {

                    requestPermission()
                } else {
                    _binding.btnRecord.isEnabled = true
                    _binding.btnList.isEnabled = true
                    _binding.btnStop.isEnabled = true
                    setAudioRecorder()

                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (myAudioRecorder != null) {
            myAudioRecorder!!.release()
        }
    }


}