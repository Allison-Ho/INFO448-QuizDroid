package edu.uw.ischool.qnho.quizdroid

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Popup: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.back)
        val settingBtn = view.findViewById<Button>(R.id.setting)

        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        settingBtn.setOnClickListener { goToSetting() }
    }

    private fun goToSetting() {
        try {
            val intentAirplaneMode = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            intentAirplaneMode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentAirplaneMode)
        } catch (e: ActivityNotFoundException) {
            Log.e("exception", e.toString() + "")
        }
    }
}