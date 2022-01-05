package com.example.tekapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
lateinit var lst:MutableList<catogry>
lateinit var iv_prof:CircleImageView
lateinit var rv:RecyclerView
lateinit var pb:ProgressBar
lateinit var auth:FirebaseAuth
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rv_apps)
        iv_prof = findViewById(R.id.iv_profile)
        lst = ArrayList()
        pb = findViewById(R.id.pb_wait)


        supportActionBar?.hide()


        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/sr-app-396f6.appspot.com/o/IMG_20210228_033120.jpg?alt=media&token=ff1c437b-67d9-4b1e-8b07-6d300b16205e").into(iv_prof)

        FirebaseDatabase.getInstance().reference.child("Catogery").addChildEventListener(object :ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                pb.visibility=View.GONE
                lst.add(catogry(snapshot.child("name").value.toString(),snapshot.child("logo").value.toString(),snapshot.key,0))
                rv.adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })

        rv.layoutManager=LinearLayoutManager(this@MainActivity)
        rv.adapter = home_Adapter(lst,this,0)

    }


}