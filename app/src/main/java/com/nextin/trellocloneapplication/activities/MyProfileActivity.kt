package com.nextin.trellocloneapplication.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nextin.trellocloneapplication.R
import com.nextin.trellocloneapplication.databinding.ActivityMyProfileBinding
import com.nextin.trellocloneapplication.firestore.FirebaseClass
import com.nextin.trellocloneapplication.models.Users
import com.nextin.trellocloneapplication.utils.Constants
import java.io.IOException


class MyProfileActivity : ReusableActivity() {

    companion object{
        const val READ_STORAGE_REQUEST_CODE = 1
        const val PICK_IMAGE_REQUEST_CODE = 2
    }
    private var mImageExternalUri : Uri? =null

    private var mImageFileUri :String =""

    private lateinit var mUserDetails : Users
   private val binding : ActivityMyProfileBinding by lazy {
        ActivityMyProfileBinding.inflate(layoutInflater)
    }
    private val image : ImageView get() = findViewById<ImageView?>(R.id.user_profile_image)
    private val username : TextView get() = findViewById(R.id.et_profile_name)
    private val userEmail : TextView get() = findViewById(R.id.et_profile_email)
    private val contact : TextView get() = findViewById(R.id.et_profile_contact)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpActionBar()

        FirebaseClass().loadingDataFromFireStore(this)

        binding.myProfileUpdateBtn.setOnClickListener {
            if (mImageExternalUri != null){
                updateDataIntoStorage()
            }
            else{
                showProgressBar()
                updateTheUserProfileDetails()
            }
        }

        image.setOnClickListener {
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED){
//                chooseImageFromStorage()
//            }
//            else{
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    READ_STORAGE_REQUEST_CODE
//                )
//            }

            chooseImageFromStorage()
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode == READ_STORAGE_REQUEST_CODE){
//            if (grantResults.isNotEmpty() &&
//                grantResults[0]==PackageManager.PERMISSION_GRANTED)
//            {
//                chooseImageFromStorage()
//            }
//        }
//    }

    private fun chooseImageFromStorage(){
        val galleryImage = Intent(Intent.ACTION_PICK,
            Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryImage, PICK_IMAGE_REQUEST_CODE)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data !=null){
            mImageExternalUri = data.data

            try{
                Glide
                    .with(this)
                    .load(mImageExternalUri)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(image)

            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    fun updateUserInformation(user : Users){

        mUserDetails  = user

        Glide
            .with(this)
            .load(user.profileImage)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(image)
        username.text = user.name
        userEmail.text = user.email
        if (user.phoneNo != 0L){
            contact.text = user.phoneNo.toString()
        }
    }
    private fun setUpActionBar(){
        val toolbar = findViewById<Toolbar>(R.id.my_profile_Activity_Toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            actionBar.title = "Profile"
        }
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

  private fun updateDataIntoStorage(){
        showProgressBar()

        if (mImageExternalUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance()
                .reference.child("USER_IMAGE"+
                        System.currentTimeMillis()+"."
                        +getStorageFileUri(mImageExternalUri))

            sRef.putFile(mImageExternalUri!!).addOnSuccessListener {
                    snapshot ->
                    Log.i("Loaded Image URI :",
                        snapshot.metadata!!.reference!!.downloadUrl.toString())

                snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    hideProgressBar()
                    mImageFileUri = uri.toString()
                    updateTheUserProfileDetails()

                }.addOnFailureListener {
                    hideProgressBar()
                    Log.e("Failed Download URI :",it.message.toString())
                }
            }.addOnFailureListener {
                hideProgressBar()
                Log.e("Failed to Put :",it.message.toString())
            }

        }
    }

    private fun getStorageFileUri(uri :Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

   private fun updateTheUserProfileDetails(){

        val userHashMap = HashMap<String, Any>()

        if(mImageFileUri.isNotEmpty() && mImageFileUri != mUserDetails.profileImage){
            userHashMap[Constants.IMAGE] = mImageFileUri
        }
        if (username.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = username.text.toString()
        }

        if (contact.toString() != mUserDetails.phoneNo.toString()){
            userHashMap[Constants.PHONE] = contact.text.toString().toLong()

        }
        FirebaseClass().updateTheUserProfileDetails(this,userHashMap)
    }

    fun afterUpdateSuccess(){
        hideProgressBar()
        finish()
    }
}