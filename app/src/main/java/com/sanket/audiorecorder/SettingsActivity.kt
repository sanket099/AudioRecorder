package com.sanket.audiorecorder


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanket.audiorecorder.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var _binding : ActivitySettingsBinding

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "myPref"
    private val REVERB = "REVERB"
    private val EQ = "EQ"
    private val BAND = "BAND"
    private val LEVELS = "LEVELS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        title = "Settings"

        initSeekBars()

        _binding.seekBand.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
                val editor = sharedPref.edit()
                editor.putInt(BAND, progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        }
        )

        _binding.seekLevels.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
                val editor = sharedPref.edit()
                editor.putInt(LEVELS, progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        }
        )

        //ids
        val reverbAdapter = ArrayAdapter.createFromResource(this,
                R.array.reverb, R.layout.spinner_text)
        reverbAdapter.setDropDownViewResource(R.layout.spinner_text)
        _binding.spinner1.adapter = reverbAdapter
        _binding.spinner1.setSelection(getPrefs())
        _binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setPref(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


        val eqAdapter = ArrayAdapter.createFromResource(this,
                R.array.eq, R.layout.spinner_text)
        eqAdapter.setDropDownViewResource(R.layout.spinner_text)
        _binding.spinner2.adapter = eqAdapter
        _binding.spinner2.setSelection(getEq())
        _binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setEq(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }

    private fun initSeekBars() {
        

        _binding.seekBand.max = 5
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            _binding.seekBand.min = 0
        }

        _binding.seekLevels.max = 15
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            _binding.seekLevels.min = -15
        }

        _binding.seekBand.progress = getBands()
        _binding.seekBand.progress = getLevels()

    }

    private fun setEq(value: Int){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putInt(EQ, value)
        editor.apply()
    }
    private fun getEq() : Int{
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        return sharedPref.getInt(EQ, 0)
    }

    private fun setPref(value: Int){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putInt(REVERB, value)
        editor.apply()
    }
    private fun getPrefs() : Int{
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        return sharedPref.getInt(REVERB, 0)
    }

    private fun getBands() : Int{
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        return sharedPref.getInt(BAND, 0)
    }
    private fun getLevels() : Int{
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        return sharedPref.getInt(LEVELS, 0)
    }




}