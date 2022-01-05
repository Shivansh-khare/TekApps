package com.example.tekapps
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.app.DownloadManager
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.Toast
import androidx.core.content.FileProvider
//import com.example.tekapps.ForegroundService.Companion.APP_INSTALL_PATH
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ForegroundService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"


    companion object {
        fun startService(context: Context, message: String, url: String,app: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            startIntent.putExtra("url",url)
            startIntent.putExtra("app",app)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
        lateinit var FILE_NAME :String
        val FILE_BASE_PATH = "file://"
        val MIME_TYPE = "application/vnd.android.package-archive"
        val PROVIDER_PATH = ".provider"
        val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
        lateinit var url: String

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread



        url = intent?.getStringExtra("url").toString()
        if (intent != null) {
            FILE_NAME = intent.getStringExtra("app").toString()+".apk"
        }else{
            FILE_NAME="TekUnknown.apk"
        }
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, AppActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TekApp $FILE_NAME Downloading ")
                .setContentText(input)
                .setSmallIcon(R.drawable.gaotek_logo)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)


            Toast.makeText(this,"download started",Toast.LENGTH_SHORT).show()
            var destination =
                    this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
            destination += FILE_NAME
//
            val uri = Uri.parse("${FILE_BASE_PATH}$destination")
            val file = File(destination)
            if (file.exists()) file.delete()
            val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)
            request.setMimeType(MIME_TYPE)
            request.setTitle("app is downloading")
            request.setDescription("downloading ...")
            // set destination
            request.setDestinationUri(uri)

            showInstallOption(destination, uri)
            // Enqueue a new download and same the referenceId
            downloadManager.enqueue(request)

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
//        val url = intent?.getStringExtra("url")

    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }


    public fun showInstallOption(
            destination: String,
            uri: Uri
    ) {
        val i = Intent(this,MainActivity::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        // set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(
                    context: Context,
                    intent: Intent
            ) {

                startActivity(i)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val contentUri = FileProvider.getUriForFile(
                            context,
                            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                            File(destination)
                    )
                    val install = Intent(Intent.ACTION_VIEW)
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    install.data = contentUri
                    startActivity(install)
                    context.unregisterReceiver(this)
                    // finish()
                    stopSelf()
                } else {
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.setDataAndType(
                            uri,
                            APP_INSTALL_PATH
                    )
                    context.startActivity(install)
                    context.unregisterReceiver(this)
//                     finish()
                    stopSelf()
                }
            }
        }
        this.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}
