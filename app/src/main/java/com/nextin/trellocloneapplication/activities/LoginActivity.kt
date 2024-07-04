package com.nextin.trellocloneapplication.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.nextin.trellocloneapplication.MainActivity
import com.nextin.trellocloneapplication.R
import com.nextin.trellocloneapplication.databinding.ActivityLoginBinding
import com.nextin.trellocloneapplication.firestore.FirebaseClass
import com.nextin.trellocloneapplication.models.Users

class LoginActivity : ReusableActivity() {
   private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.registerHere.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.btnSignIN.setOnClickListener {
            loginIntoTheApplication()
        }
    }
    fun onSignSuccess(user :Users){
        hideProgressBar()
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun loginIntoTheApplication(){

        val emailId = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(emailId)|| TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter the Credentials",
                Toast.LENGTH_SHORT).show()
        }
        else{
            showProgressBar()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId,pass).addOnCompleteListener {
                    task ->
                if (task.isSuccessful){
                    FirebaseClass().signUser(this)
                }
                else{
                    Toast.makeText(this, "Login Failed due to ${task.exception}",
                        Toast.LENGTH_SHORT).show()
                }
            }
                .addOnFailureListener{
                    Toast.makeText(this, it.message,
                        Toast.LENGTH_SHORT).show()
                }
        }
    }
}