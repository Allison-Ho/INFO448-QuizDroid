package edu.uw.ischool.qnho.quizdroid

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.logging.Handler

class Threading(val context: Context): Thread() {
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var isRunning = false
    private lateinit var fileUrl: String
    private lateinit var time: String

    override fun run() {
        super.run()
        fileUrl =
            context.getSharedPreferences("quizdroid", Context.MODE_PRIVATE).getString("URL", "").toString()

        Log.i("URL", "curr quiz url in threading: $fileUrl")

        if(fileUrl == "") {
            fileUrl = "http://tednewardsandbox.site44.com/questions.json"
        }

        time =
            context.getSharedPreferences("quizdroid", Context.MODE_PRIVATE).getString("DELAY", "").toString()

        if(time == "") {
            time = "60"
        }

        isRunning = true

        while(isRunning) {
            val downloader = Downloader.FileDownloader(context)
            downloader.download(fileUrl.trim())

            handler.post{
                Toast.makeText(context, fileUrl, Toast.LENGTH_SHORT).show()
            }

            try {
                sleep(time.toLong() * 10000)
            } catch (err: InterruptedException) {
                Log.e("DEBUG", "thread interrupted", err)
            }
        }
    }

    fun stopThread() {
        isRunning = false
        interrupt()
    }
}