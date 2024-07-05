package com.nextin.trellocloneapplication.firestore

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nextin.trellocloneapplication.MainActivity
import com.nextin.trellocloneapplication.activities.LoginActivity
import com.nextin.trellocloneapplication.activities.MyProfileActivity
import com.nextin.trellocloneapplication.activities.SignUpActivity
import com.nextin.trellocloneapplication.models.Users
import com.nextin.trellocloneapplication.utils.Constants

class FirebaseClass {
    private val mFirebaseInstance = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: Users) {
        mFirebaseInstance.collection(Constants.USER)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.getRegisterUserSeccess()
            }.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
    }
    fun updateTheUserProfileDetails(activity: MyProfileActivity,
                                    userHashMap: HashMap<String,Any>){


        mFirebaseInstance.collection(Constants.USER)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity,"Profile updated successfully",Toast.LENGTH_SHORT).show()
                activity.afterUpdateSuccess()
            }.addOnFailureListener {
                e ->
                Log.e("Firebase Updation failed : ",e.message.toString())
                activity.hideProgressBar()
            }
    }
    fun loadingDataFromFireStore(activity: Activity) {
        mFirebaseInstance.collection(Constants.USER)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loadUser = document.toObject(Users::class.java)
                when(activity){
                    is LoginActivity ->{
                        if (loadUser != null) {
                            activity.onSignSuccess(loadUser)
                        }
                    }
                    is MainActivity ->{
                        if (loadUser != null) {
                            activity.updateNavigationUserDetails(loadUser)
                        }
                    }
                    is MyProfileActivity ->{
                        if (loadUser !=null){
                            activity.updateUserInformation(loadUser)
                        }

                    }
                }
            }
            .addOnFailureListener { errors ->
                when(activity){
                    is LoginActivity ->{
                        activity.hideProgressBar()
                    }
                    is MainActivity ->{
                        activity.hideProgressBar()
                    }
                }

                Log.e("SignInUser", "error writing message is :", errors)
            }
    }
    fun getCurrentUserId(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}