package edu.uw.ischool.qnho.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.registerReceiver
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class Preferences: PreferenceFragmentCompat() {
    var receiver : BroadcastReceiver? = null
    var fileUrl: String = "http://tednewardsandbox.site44.com/questions.json"
    var delayTime: String = ""

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val urlPref: EditTextPreference? = findPreference("url")
        urlPref?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if(text.isNullOrEmpty()) {
                ""
            } else {
                fileUrl = text
                "Current questions URL: ${text}"
            }
        }

        val delay: EditTextPreference? = findPreference("check_download")
        delay?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        delay?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if(text.isNullOrEmpty()) {
                ""
            } else {
                delayTime = text
                "Check new downloads every ${text} minutes"
            }
        }

        saveToPreferences()
    }

    private fun saveToPreferences() {
        Log.i("URL", "curr quiz url in preferences: $fileUrl")
        val sharedPreferences: SharedPreferences =
            activity?.getSharedPreferences("quizdroid", Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL", fileUrl)
        editor.putString("DELAY", delayTime)
        editor.apply()
    }
}