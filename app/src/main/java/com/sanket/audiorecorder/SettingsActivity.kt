package com.sanket.audiorecorder


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanket.audiorecorder.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var _binding : ActivitySettingsBinding

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "myPref"
    private val REVERB = "REVERB"
    private val EQ = "EQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        title = "Settings"



        //ids
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.reverb, R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        _binding.spinner1.adapter = adapter
        _binding.spinner1.onItemSelectedListener = SpinnerReverb()

        _binding.spinner1.setSelection(getPrefs())


        val adapter2 = ArrayAdapter.createFromResource(this,
                R.array.eq, R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        _binding.spinner2.adapter = adapter2
        _binding.spinner2.onItemSelectedListener = SpinnerEq()

        _binding.spinner2.setSelection(getEq())



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


     internal class SpinnerReverb : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, v: View, position: Int, id: Long) {

            SettingsActivity().setPref(position)

        }


         override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    internal class SpinnerEq : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, v: View, position: Int, id: Long) {

            SettingsActivity().setEq(position)

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

}