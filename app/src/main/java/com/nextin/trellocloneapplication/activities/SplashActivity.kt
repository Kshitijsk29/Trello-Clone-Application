package com.nextin.trellocloneapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nextin.trellocloneapplication.MainActivity
import com.nextin.trellocloneapplication.databinding.ActivitySplashBinding
import com.nextin.trellocloneapplication.firestore.FirebaseClass

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
   private val binding :ActivitySplashBinding by lazy{
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       window.setFlags(
           WindowManager.LayoutParams.FLAG_FULLSCREEN,
           WindowManager.LayoutParams.FLAG_FULLSCREEN
       )

        Handler().postDelayed({
            
            val currentUserId = FirebaseClass().getCurrentUserId()
            if (currentUserId.isNotEmpty()){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,IntroActivity::class.java))
            }
            finish()
        },3000)
    }
}