package com.example.tekapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mail : EditText
    lateinit var pass : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        mail = findViewById(R.id.ET_mail_login)
        pass = findViewById(R.id.ET_pass_login)

        supportActionBar?.hide()


        findViewById<Button>(R.id.btn_login).setOnClickListener(View.OnClickListener {
            val email = mail.text.toString()
            val password = pass.text.toString()
            if(email.isEmpty() || password.isEmpty())
                Toast.makeText(this,"Please fill both field",Toast.LENGTH_SHORT).show()
            else
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                Toast.makeText(this,"Log in Successful !!!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }.addOnFailureListener{
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<TextView>(R.id.Tv_sign_up).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Sign_upActivity::class.java))
        })
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish();
    }
}