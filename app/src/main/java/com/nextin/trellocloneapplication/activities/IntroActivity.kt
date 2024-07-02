package com.nextin.trellocloneapplication.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nextin.trellocloneapplication.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    val binding : ActivityIntroBinding by lazy{
        ActivityIntroBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}