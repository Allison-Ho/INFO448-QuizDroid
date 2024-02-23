package edu.uw.ischool.qnho.quizdroid

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File

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

            return downloadManager.enqueue(req)
        }
    }
}
