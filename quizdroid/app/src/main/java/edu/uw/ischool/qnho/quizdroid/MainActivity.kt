package edu.uw.ischool.qnho.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val homePage = HomePage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.app, homePage).commit()
    }
}