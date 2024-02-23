package edu.uw.ischool.qnho.quizdroid

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileWriter
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


interface Downloader {
    fun download(url: String): Long

    class FileDownloader(val context: Context): Downloader {
        override fun download(url: String): Long {
            Log.i("URL", "curr quiz url in download: $url")
            val downloadManager = context.getSystemService(DownloadManager::class.java)
            val file = "questions.json"
            val dir = File(
                context.applicationContext.filesDir, "/$file")

            Log.i("TEST", "download folder: ${dir.toString()}")

            if (dir.exists()) {
                Log.i("HELP", "delete old version")
                dir.delete()
            }

            val req = DownloadManager.Request(url.toUri())
                .setMimeType("application/json")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(file)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file)

            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("http://tednewardsandbox.site44.com/questions.json")
                    .get()
                    .addHeader("accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                writeFileOnInternalStorage(context, "questions.json", response.body()?.string())
            }

            return downloadManager.enqueue(req)
        }

        fun writeFileOnInternalStorage(mcoContext: Context, sFileName: String?, sBody: String?) {
            val dir = File(mcoContext.filesDir, "questions.json")
            Log.i("TEST", "in write file: ${dir.toString()}")
            if (!dir.exists()) {
                dir.mkdir()
            }
            try {
                val gpxfile = File(dir, sFileName)
                val writer = FileWriter(gpxfile)
                writer.append(sBody)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
