package com.menard.ruralis.login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.R
import com.menard.ruralis.knowsit.HomeActivity
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var goatAnimation: AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModelFactory = Injection.provideViewModelFactory(this)
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        findViewById<ImageView>(R.id.login_logo).apply {
            setBackgroundResource(R.drawable.goat_animation)
            goatAnimation = background as AnimationDrawable
            goatAnimation.start()
        }

        login_connexion_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            login_connexion_btn -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.goat_animation)
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()))
            .setIsSmartLockEnabled(false, true)
            .build(),
            Constants.RC_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.RC_LOGIN){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                Snackbar.make(login_layout, "Connexion succeed" , Snackbar.LENGTH_SHORT).show()
                val currentUser = FirebaseAuth.getInstance().currentUser
                userViewModel.createUser(currentUser!!.uid, currentUser.displayName!!, currentUser.email!!, currentUser.photoUrl.toString())
                startActivity(Intent(this, HomeActivity::class.java))
                goatAnimation.stop()
            }else {
                when {
                    response == null -> Snackbar.make(login_layout, "Connexion canceled" , Snackbar.LENGTH_SHORT).show()
                    response.error?.errorCode == ErrorCodes.NO_NETWORK -> Snackbar.make(login_layout, "No Network", Snackbar.LENGTH_SHORT).show()
                    response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR -> Snackbar.make(login_layout, "Unknown error", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        //
    }
}
