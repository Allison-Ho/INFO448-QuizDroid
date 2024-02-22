package edu.uw.ischool.qnho.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val urlPref: EditTextPreference? = findPreference("url")
        urlPref?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if(text.isNullOrEmpty()) {
                ""
            } else {
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
                "Check new downloads every ${text} minutes"
            }
        }

        delay?.setOnPreferenceClickListener{ preference ->
            val delayTime = delay.text
            startNagging(delayTime!!)
            true
        }
    }

    fun startNagging(delay: String) {
        Log.i("TEST", delay)

        if(receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context? , p1: Intent?) {
                    Log.i("WHY", "calling")
                    Toast.makeText(activity, "check new downloads", Toast.LENGTH_SHORT).show()
                }
            }

            val filter = IntentFilter(ALARM_ACTION)
            registerReceiver(receiver, filter)

            val intent = Intent(ALARM_ACTION)
            val pendingIntent = PendingIntent.getBroadcast(this.activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager : AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (delay.toInt() * 60000).toLong(), pendingIntent)
        }
    }
}