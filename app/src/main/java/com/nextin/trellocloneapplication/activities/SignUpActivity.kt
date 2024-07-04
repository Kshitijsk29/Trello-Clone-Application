package com.nextin.trellocloneapplication.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nextin.trellocloneapplication.databinding.ActivitySignUpBinding
import com.nextin.trellocloneapplication.firestore.FirebaseClass
import com.nextin.trellocloneapplication.models.Users
import org.w3c.dom.Text

class SignUpActivity : ReusableActivity() {

    private val binding : ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
    }

    fun getRegisterUserSeccess(){
        hideProgressBar()
        Toast.makeText(this, "You have successfully register",
            Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
   private fun registerUser(){
        val nameUser = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(nameUser)|| TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(this, "Pease enter the details "
            , Toast.LENGTH_SHORT).show()
        }else{
            showProgressBar()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        val firebaseUser : FirebaseUser= task.result!!.user!!
                        val registeredEmail = firebaseUser.email
                        val user = Users(firebaseUser.uid,nameUser,registeredEmail!!,password)
                        FirebaseClass().registerUser(this,user)
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, task.exception.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    task ->
                    Toast.makeText(this, task.message,
                        Toast.LENGTH_SHORT).show()
                }
        }
    }
}