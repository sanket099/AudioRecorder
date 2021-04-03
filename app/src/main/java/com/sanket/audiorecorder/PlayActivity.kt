package com.sanket.audiorecorder



import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler

import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sanket.audiorecorder.databinding.ActivityPlayBinding



class PlayActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityPlayBinding
    private var audioFile : AudioFileClass? = null
    private lateinit var audioArray : ArrayList<AudioFileClass>


   /* private lateinit var runnable:Runnable
    private var handler: Handler = Handler()*/
    private var pause:Boolean = false
    private var filePath : Uri? = null
    private var mp : MediaPlayer? = null
    private var position : Int = 0


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

            this.position = intent.getIntExtra("Position",0)
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
            /* File videoFile = new File(path);
            final Uri[] uri = {Uri.fromFile(videoFile)};
            MediaScannerConnection.scanFile(this,
                    new String[] { videoFile.getAbsolutePath() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri2) {
                            uri[0] = uri2;
                        }
                    });*/
            //  Uri uri = Uri.parse("file:///" + path);
            //  System.out.println(uri.toString());

                if (mp == null) {
                    // player = new MediaPlayer();


                    mp = MediaPlayer.create(this, filePath)
                    //System.out.println("uri[0].toString() = " + uri[0].toString());

                    //  setUri(this,id);
                   mp?.setOnCompletionListener {
                       /*if (repeatOn) {
                            playThisSong()
                        } else if (shuffleOn) {
                            playAnySongShuffle()
                        } else nextSong()*/
                       nextSong()
                   }
                }
                mp?.start()
                pause = false
                showPlay(false)


        } catch (e: Exception) {

            // Toast.makeText(this, "Not Found" + path, Toast.LENGTH_LONG).show();
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

    private fun recorderTime(time : Int): String {
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

    private fun setTextViews(date : String, name : String, duration : String){
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




}