package edu.uw.ischool.qnho.quizdroid

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.util.logging.Handler

class Threading(val context: Context): Thread() {
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var isRunning = false
    private lateinit var fileUrl: String

    override fun run() {
        super.run()
        fileUrl =
            context.getSharedPreferences("quizdroid", Context.MODE_PRIVATE).getString("URL", "").toString()

        isRunning = true

        while(isRunning) {
            val downloader = Downloader.FileDownloader(context)
            downloader.download(fileUrl)

            handler.post{
                Toast.makeText(context, fileUrl, Toast.LENGTH_SHORT).show()
            }

            try {
                sleep(60000)
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