package com.nextin.trellocloneapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.nextin.trellocloneapplication.R.id.mainActivity_ToolBar
import com.nextin.trellocloneapplication.activities.IntroActivity
import com.nextin.trellocloneapplication.activities.ReusableActivity
import com.nextin.trellocloneapplication.databinding.ActivityMainBinding
import com.nextin.trellocloneapplication.firestore.FirebaseClass
import com.nextin.trellocloneapplication.models.Users

class MainActivity : ReusableActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
   private val image : ImageView
        get() = findViewById(R.id.profile_image)

    private val userName : TextView
        get() = findViewById(R.id.userNameHeader)
    private val userEmail : TextView
        get()= findViewById(R.id.userEmailHeader)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        FirebaseClass().signUser(this)

        setActionBar()

        binding.navigationView.setNavigationItemSelectedListener(this)

    }

    fun updateNavigationUserDetails(users :Users){
        Glide
            .with(this)
            .load(users.profileImage)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(image)

        userName.text = users.name
        userEmail.text = users.email
    }
    private fun setActionBar(){
        val toolbar = findViewById<Toolbar>(mainActivity_ToolBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.baseline_menu)
        toolbar.setNavigationOnClickListener {
            openDrawer()
        }
    }
    private fun openDrawer(){
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else
        {
            drawer.openDrawer(GravityCompat.START)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_home ->{
                Toast.makeText(this, "Welcome  to Home Screen",
                    Toast.LENGTH_SHORT).show()
            }
            R.id.menu_help ->
            {
                Toast.makeText(this, "How Can I Help You ?",
                    Toast.LENGTH_SHORT).show()
            }
            R.id.menu_settings ->
            {
                Toast.makeText(this, "Make Changes According to you ",
                    Toast.LENGTH_SHORT).show()
            }
            R.id.menu_signout ->
            {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}