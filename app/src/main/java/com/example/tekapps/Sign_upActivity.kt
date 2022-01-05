package com.example.tekapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class Sign_upActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        supportActionBar?.hide()

        findViewById<Button>(R.id.btn_signup).setOnClickListener(View.OnClickListener {
            var email = findViewById<EditText>(R.id.ET_mail_signup).text.toString()
            var pass = findViewById<EditText>(R.id.ET_pass_signup).text.toString()
            var cnfrm = findViewById<EditText>(R.id.ET_pass_cnfrm_signup).text.toString()
            if(!email.isEmpty() && !pass.isEmpty() && pass == cnfrm)
            {
                auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,"Regester Successfully !!!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,MainActivity::class.java))
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this,"signin falied", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener{
                            Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
            }
            else{
                Toast.makeText(this,"fields not correct", Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<TextView>(R.id.Tv_logIn).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        })
    }
}