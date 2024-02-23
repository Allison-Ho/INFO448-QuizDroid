package edu.uw.ischool.qnho.quizdroid

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// ============================================================
// UI layer
class MainActivity : AppCompatActivity() {
    private val bgThread = Threading(this)
    private val fragmentManager = supportFragmentManager
    private val homePage = HomePage()
    private val popup = Popup()
    private val PERMISSION_REQUEST_CODE = 234

    var allQuizzes: List<Topic> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storagePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }

        val fragmentTransaction = fragmentManager.beginTransaction()

        if(checkInternetConnectivity(this)) {
            bgThread.start()
        } else {
            // airplane mode is on
            if(Settings.System.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
                Toast.makeText(this, "Airplane mode is on. Wanna turn it off?", Toast.LENGTH_SHORT).show()
                fragmentTransaction.replace(R.id.app, popup).commit()
            } else {
                Toast.makeText(this, "You don't have access to the internet", Toast.LENGTH_SHORT).show()
            }
        }


        val quizApp = (application as QuizApp)
        val repo = quizApp.quizzes
        allQuizzes = repo.getQuiz()
        fragmentTransaction.replace(R.id.app, homePage).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgThread.stopThread()
    }

    private fun checkInternetConnectivity(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val networkInfo = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}