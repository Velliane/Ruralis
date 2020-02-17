package com.menard.ruralis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.R
import com.menard.ruralis.knowsit.HomeActivity
import com.menard.ruralis.login.LoginActivity

class DispatcherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatcher)

        Handler().postDelayed({
            kotlin.run {
                val user = FirebaseAuth.getInstance().currentUser
                if(user != null){
                    startActivity(Intent(this, HomeActivity::class.java))
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }, 2000)
    }
}
