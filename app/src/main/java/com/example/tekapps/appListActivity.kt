package com.example.tekapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

class appListActivity : AppCompatActivity() {
    lateinit var lst:MutableList<catogry>
//    lateinit var iv_prof: CircleImageView
    lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)

        rv = findViewById(R.id.rec_app_list)
//        iv_prof = findViewById(R.id.iv_profile)
        lst = ArrayList()
        rv.layoutManager= LinearLayoutManager(this)
        rv.adapter = home_Adapter(lst,this,1)


        intent.getStringExtra("path")?.let {
            FirebaseDatabase.getInstance().reference.child("Catogery").child(it).child("App").addChildEventListener(object :
                ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    lst.add(catogry(snapshot.child("name").value.toString(),snapshot.child("image").value.toString(),snapshot.child("download_url").value.toString(),1))
                    rv.adapter?.notifyDataSetChanged()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}