package com.menard.ruralis

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.knowsit.HomeActivity
import com.menard.ruralis.login.LoginActivity

class DispatcherActivity : AppCompatActivity() {

    private lateinit var goatAnimation: AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatcher)

        findViewById<ImageView>(R.id.dispatchers_logo).apply {
            setBackgroundResource(R.drawable.goat_animation)
            goatAnimation = background as AnimationDrawable
            goatAnimation.start()
        }

        Handler().postDelayed({
            kotlin.run {
                val user = FirebaseAuth.getInstance().currentUser
                if(user != null){
                    startActivity(Intent(this, HomeActivity::class.java))
                    goatAnimation.stop()
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    goatAnimation.stop()
                }
            }
        }, 2000)
    }
}
