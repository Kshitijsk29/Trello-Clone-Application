package com.nextin.trellocloneapplication.activities

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nextin.trellocloneapplication.R
import com.nextin.trellocloneapplication.databinding.ActivityReusableBinding

open class ReusableActivity : AppCompatActivity() {

    lateinit var mProgressBar : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reusable)

    }

    fun showProgressBar(){
        mProgressBar =Dialog(this)
        mProgressBar.setContentView(R.layout.progress_bar_layout)
        mProgressBar.show()
    }
    fun hideProgressBar()
    {
        mProgressBar.dismiss()
    }

    fun getCurrentuserId() :String{

        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun setUpActionbar(){
        val toolbar = findViewById<Toolbar>(R.id.sign_up_screen_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        }
    }

    fun snackBarMessage(message : String){
        val snackBar = Snackbar.make(findViewById(com.google.android.material.R.id.content)
        ,message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.
        getColor(this,R.color.intro_heading_color))
        snackBar.show()
    }
}