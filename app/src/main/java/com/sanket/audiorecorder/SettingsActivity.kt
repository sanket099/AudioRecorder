package com.sanket.audiorecorder

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sanket.audiorecorder.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var _binding : ActivitySettingsBinding

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "myPref"
    private val REVERB = "REVERB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)



        title = "Settings"

        _binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rvb_sr_1 -> {
                    setPref(1)
                }
                R.id.rvb_mr_2 -> {
                    setPref(2)
                }
                R.id.rvb_lr_3 -> {
                    setPref(3)
                }
                R.id.rvb_mh_4 -> {
                    setPref(4)
                }
                R.id.rvb_lh_5 -> {
                    setPref(5)
                }
                R.id.rvb_sp_6 -> {
                    setPref(6)
                }
            }

        }
    }
    private fun setPref(value : Int){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putInt(REVERB, value)
        editor.apply()
    }
}