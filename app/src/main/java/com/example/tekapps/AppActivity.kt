package com.example.tekapps

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaCodec.MetricsConstants.MIME_TYPE
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class AppActivity : AppCompatActivity() {
    lateinit var iv: ImageView
    lateinit var TV_title: TextView
    lateinit var TV_desc: TextView
    lateinit var prof: CircleImageView
    lateinit var Durl: String
    lateinit var name: String
    public lateinit var btn_install: Button

    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var a = true
        setContentView(R.layout.activity_app)
        iv = findViewById(R.id.imageView)
        prof = findViewById(R.id.profile_image)
        btn_install = findViewById(R.id.button)
        TV_title = findViewById(R.id.textView)

        name = intent.getStringExtra("name").toString()
        val img = intent.getStringExtra("img")
        Durl = intent.getStringExtra("url").toString()



        Glide.with(this@AppActivity).load(img).into(iv);

        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/sr-app-396f6.appspot.com/o/IMG_20210228_033120.jpg?alt=media&token=ff1c437b-67d9-4b1e-8b07-6d300b16205e").into(prof)
        TV_title.setText(name)

        btn_install.setOnClickListener {
            download()
        }
    }
    private fun download() {
        checkStoragePermission()
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloading()
            } else {
                // Permission request was denied.
//                mainLayout.showSnackbar("permition denied", Snackbar.LENGTH_SHORT)
            }
        }
    }
    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading
            downloading()
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission()
        }
    }

    private fun downloading() {
            ForegroundService.startService(this,"downloading files",Durl,name)
    }

    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            extentionFile.showSnackbar(
//                    "permition denied",
//                    Snackbar.LENGTH_INDEFINITE, "ok"
//            ) {
//                requestPermissionsCompat(
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        PERMISSION_REQUEST_STORAGE
//                )
//            }
        } else {
            requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
            )
        }
    }

}