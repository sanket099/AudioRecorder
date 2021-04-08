package com.sanket.audiorecorder

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.NoiseSuppressor
import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanket.audiorecorder.databinding.ActivityPlayBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlayActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityPlayBinding
    private var audioFile : AudioFileClass? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "myPref"
    private val REVERB = "REVERB"

    private lateinit var audioArray : ArrayList<AudioFileClass>

   /* private lateinit var runnable:Runnable
    private var handler: Handler = Handler()*/
    private var pause:Boolean = false
    private var filePath : String? = null
    private var mp : MediaPlayer? = null
    private var position : Int = 0
    private var bass : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        audioArray = ArrayList<AudioFileClass>()
        getObject()
        showPlay(false)

        playMahSong()
        seekBarInit()



        _binding.ivNext.setOnClickListener(View.OnClickListener {
            nextSong()
            println("Next SOng")

        })

        _binding.ivPlay.setOnClickListener {

            if (!pause) {
                if (mp != null) {
                    mp?.pause()
                    showPlay(true)
                    pause = true
                }

            } else {
                pause = false
                showPlay(false)
                playMahSong()
            }

        }


        _binding.ivPrev.setOnClickListener {
            prevSong()

        }

        _binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                if (mp != null && fromUser) {
                    mp?.seekTo(progress * 1000)
                }
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
        audioArray = intent.getParcelableArrayListExtra<AudioFileClass>("Array")!!
        if(audioFile != null){
            _binding.tvDate.text = audioFile?.getDate()
            _binding.tvTitle.text = audioFile?.getTitle()
             filePath = audioFile?.getUri()!!
            _binding.tvDur.text = audioFile?.getDuration()

            this.position = intent.getIntExtra("Position", 0)
            println("Pos get $position")

        }

        else{
            _binding.ivPlay.isEnabled = false
            _binding.ivNext.isEnabled = false
            _binding.ivPrev.isEnabled = false
            _binding.seekBar.isEnabled = false
        }
    }

    private fun nextSong() {
        println("POSITION : $position")
        println("SIZE : ${audioArray.size}")
        if (position < audioArray.size - 1 ) {
            /*if (shuffleOn) {
                position = Random().nextInt(musicClasses.size() - 1)
            } else {
                position++
            }*/
                position++


            //path = musicClasses.get(position).getPath();
                filePath = audioArray[position].getUri()
            setTextViews(audioArray[position].getDate()!!, audioArray[position].getTitle()!!, audioArray[position].getDuration()!!)
            stopPlayer()
            playMahSong()
        } else {
            pause = true
            showPlay(true)
            Toast.makeText(this, "Last song :|", Toast.LENGTH_SHORT).show()
            stopPlayer()
        }
    }

    private fun prevSong() {
        if (position > 0) {
            position--

            //path = musicClasses.get(position).getPath();
            filePath = audioArray[position].getUri()
            setTextViews(audioArray[position].getDate()!!, audioArray[position].getTitle()!!, audioArray[position].getDuration()!!)
            stopPlayer()
            playMahSong()
        } else {
            Toast.makeText(this, "No song :|", Toast.LENGTH_SHORT).show()
        }
    }

  /*  fun getSongUri(songId: Int): Uri {
        println("ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId).toString() = " + ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toLong()).toString())
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toLong())
    }*/

    /*fun setUri(appContext: Context?, songId: Int) {
        try {
            // player.reset();
            val s = getSongUri(songId).toString()
            AudioPlayer.player.setDataSource(appContext, Uri.parse(Uri.encode(s)))
            println("songId = $songId")
        } catch (e: IOException) {
            e.printStackTrace()
            System.out.println("song" + e.getMessage())
        }
    }*/





    private fun seekBarInit() {
        val mHandler = Handler()
        //Make sure you update Seekbar on UI thread
        this.runOnUiThread(object : Runnable {
            override fun run() {
                if (mp != null) {
                    _binding.seekBar.max = mp?.duration!! / 1000
                    val mCurrentPosition: Int = mp?.currentPosition!! / 1000
                    _binding.seekBar.progress = mCurrentPosition
                    _binding.tvProg.text = recorderTime(mCurrentPosition)
                }
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun playMahSong() {
        try {

                if (mp == null) {
                    // player = new MediaPlayer();
                    mp = MediaPlayer()
                    mp?.setDataSource(filePath)

                    reverb()
                    //setUpBooster()

                    /*val suppressor = NoiseSuppressor.create(
                            mp!!.audioSessionId)
                    suppressor.enabled = true
*/

                    mp?.prepare()

                }
                //mp?.prepare()
            mp!!.setOnPreparedListener {

                mp?.start()
            }

            mp!!.setOnCompletionListener {
                println("completed")
                nextSong()
            }



                pause = false
                showPlay(false)

        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            println("e.getMessage() = " + e.message)
        }
    }

   /* private fun playAnySongShuffle() {
        val random = Random()
        val randomPos: Int = random.nextInt(musicClasses.size() - 1)
        position = randomPos
        id = musicClasses.get(position).getId()
        setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist())
        stopPlayer()
        playMahSong()
    }*/

   /* private fun playThisSong() {
       *//* id = musicClasses.get(position).getId()
        setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist())*//*
        stopPlayer()
        playMahSong()
    }*/

    private fun showPlay(b: Boolean) {
        if (b) _binding.ivPlay.setImageResource(R.drawable.play_24) else _binding.ivPlay.setImageResource(R.drawable.pause_24dp)
    }

    /* public void play(View v) {
        String path = getIntent().getStringExtra(MainActivity.PATH);
        Uri uri= Uri.parse("file:///"+path);
        System.out.println("path = " + path);
        if (player == null) {
            player = MediaPlayer.create(this, uri);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }
    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }
    public void stop(View v) {
        stopPlayer();
    }*/
    private fun stopPlayer() {
        if (mp != null) {
            mp?.release()
            mp = null
            // Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayer()
    }

    private fun recorderTime(time: Int): String {
        var second : Int = time
        var minute : Int = 0
        if (second == 60) {
            minute++
            second = 0
        }

        return String.format("%02d:%02d", minute, second)
    }

   /* fun stopPlaying(view: View?) {
        stopPlayer()
    }
*/
    /*override fun run() {
        var currentPosition: Int = mp!!.currentPosition
        val total: Int = mp!!.duration
        while (mp != null && mp!!.isPlaying && currentPosition < total) {
            currentPosition = try {
                Thread.sleep(1000)
                mp!!.currentPosition
            } catch (e: InterruptedException) {
                return
            } catch (e: java.lang.Exception) {
                return
            }
            _binding.seekBar.progress = currentPosition

        }
    }*/

    /*private fun getMyTime(prog : Int) : S{
        var min : Int = 0
        if(prog < 60 ){

        }else{

        }
    }
*/

    private fun setTextViews(date: String, name: String, duration: String){
        _binding.tvDate.text = date
        _binding.tvTitle.text = name

        _binding.tvDur.text = duration

    }

    override fun onPause() {
        super.onPause()
        stopPlayer()
    }

    override fun onResume() {
        super.onResume()
        playMahSong()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    private fun setUpBooster() {

        val booster = BassBoost(0, 0)
        mp?.attachAuxEffect(booster.id)
        booster.setStrength(1000.toShort())
        booster.enabled = true

        mp?.setAuxEffectSendLevel(1.0f)

       /* try {
            //mp?.prepare()
            bass = true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            println("bass error : ${e.message}")
            bass = false
        } catch (e: IOException) {
            e.printStackTrace()
            println("bass error : ${e.message}")
            bass = false*/
        }


    private fun reverb(){
        /*val reverb = PresetReverb(1, 0)
        reverb.preset = PresetReverb.PRESET_LARGEHALL
        reverb.enabled = true
        mp!!.attachAuxEffect(reverb.id)
        mp!!.setAuxEffectSendLevel(1.0f)*/
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val pReverb = PresetReverb(1, 0)
        mp!!.attachAuxEffect(pReverb.id)
        if (sharedPref.getInt(REVERB, 0) != 0) {
            pReverb.preset = sharedPref.getInt(REVERB, 0).toShort()
        }
        else {
            pReverb.preset = PresetReverb.PRESET_SMALLROOM
        }

        pReverb.enabled = true
        mp!!.setAuxEffectSendLevel(1.0f)
    }

    private fun saveAudioAfterEffects(){
        //creating content resolver and put the values
        val myDirectory = File(Environment.getExternalStorageDirectory(), "recorder_app_hello")
        var title : String = ""

        try {
            // Create folder to store recordingss

            val dateFormat = SimpleDateFormat("dd_mm_yyyy")
            val date = dateFormat.format(Date())
            title = "REC_withReverb$date.mp3"
            filePath = myDirectory.absolutePath + File.separator + title

        } catch (e: Exception) {
            e.printStackTrace()
            println("error ${e.message}")
        }

        val values = ContentValues()
        // values.put(MediaStore.Audio.Media._ID, 12)
        values.put(MediaStore.Audio.Media.DATA, filePath)

        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp")
        values.put(MediaStore.Audio.Media.TITLE, title)
        //store audio recorder file in the external content uri
        contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)

    }







}