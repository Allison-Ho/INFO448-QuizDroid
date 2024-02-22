package edu.uw.ischool.qnho.quizdroid

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import java.io.File

interface Downloader {
    fun download(url: String): Long

    class FileDownloader(val context: Context): Downloader {
        override fun download(url: String): Long {
            val downloadManager = context.getSystemService(DownloadManager::class.java)
            val file = "questions.json"
            val dir = File(
                Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS,
                "/$file")

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
