package com.sanket.audiorecorder

import android.R
import android.R.attr.name
import android.R.id
import android.content.ContentUris
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanket.audiorecorder.databinding.ActivityPlayBinding



class PlayActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityPlayBinding
    private var audioFile : AudioFileClass? = null
    private var audioArray : ArrayList<AudioFileClass> = ArrayList<AudioFileClass>()


    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false
    private  var filePath : Uri? = null
    private var mp : MediaPlayer? = null
    private var position : Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        getObject()
        show_play(false)

        playMahSong()
        seekBarInit()



        _binding.ivNext.setOnClickListener(View.OnClickListener {
            nextSong()

        })

        _binding.ivPlay.setOnClickListener(View.OnClickListener {
            prevSong()

            if(!pause){
                if (mp != null) {
                    mp?.pause()
                    show_play(true)
                    pause = true
                }

            }
            else{
                pause = false
                show_play(false)
                playMahSong()
            }

        })


        _binding.ivPrev.setOnClickListener(View.OnClickListener {
            prevSong()

        })

        _binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                if(mp != null && fromUser){
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
        if(audioFile != null && audioArray!= null){
            _binding.tvDate.text = audioFile?.getDate()
            _binding.tvTitle.text = audioFile?.getTitle()
            filePath = audioFile?.getUri()!!

        }

        else{
            _binding.ivPlay.isEnabled = false
            _binding.ivNext.isEnabled = false
            _binding.ivPrev.isEnabled = false
            _binding.seekBar.isEnabled = false
        }
    }

    private fun nextSong() {
        if (position < audioArray.size) {
            /*if (shuffleOn) {
                position = Random().nextInt(musicClasses.size() - 1)
            } else {
                position++
            }*/


            //path = musicClasses.get(position).getPath();
                filePath = audioArray[position].getUri()
            stopPlayer()
            playMahSong()
        } else {
            Toast.makeText(this, "Last song :|", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prevSong() {
        if (position > 0) {
            position--

            //path = musicClasses.get(position).getPath();
            filePath = audioArray[position].getUri()
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
                   mp?.setOnCompletionListener(OnCompletionListener {
                        /*if (repeatOn) {
                            playThisSong()
                        } else if (shuffleOn) {
                            playAnySongShuffle()
                        } else nextSong()*/
                       nextSong()
                    })
                }
                mp?.start()
                pause = false
                show_play(false)

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

    private fun playThisSong() {
       /* id = musicClasses.get(position).getId()
        setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist())*/
        stopPlayer()
        playMahSong()
    }

    private fun show_play(b: Boolean) {
        if (b) _binding.ivPlay.setImageResource(R.drawable.ic_media_play) else _binding.ivPlay.setImageResource(R.drawable.ic_media_pause)
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

    fun stopPlaying(view: View?) {
        stopPlayer()
    }

}