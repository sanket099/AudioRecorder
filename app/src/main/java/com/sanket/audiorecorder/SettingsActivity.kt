package com.sanket.audiorecorder


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.sanket.audiorecorder.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var _binding : ActivitySettingsBinding

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "myPref"
    private val REVERB = "REVERB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        title = "Settings"



        //ids
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        _binding.spinner1.adapter = adapter
        _binding.spinner1.onItemSelectedListener = this

        _binding.spinner1.setSelection(getPrefs())

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text = parent!!.getItemAtPosition(position).toString()
        println("POSITION $position $text")
        setPref(position)
//        Toast.makeText(this, position, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {



    }
}