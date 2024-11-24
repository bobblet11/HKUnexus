package com.example.hkunexus

import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hkunexus.databinding.ActivityMainBinding
import com.example.hkunexus.ui.login.LoginActivity
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.asideNavView


//        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        // Setup the toolbar and bottom navigation
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_explore,
                R.id.navigation_create,
                R.id.navigation_my_events,
                R.id.navigation_my_groups,
            ),
            drawerLayout
        )

        onBackPressedDispatcher.addCallback(this) {
            logBackStack()
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (!navController.popBackStack()) {
                    super.onBackPressed() // Exit app if nothing to pop
                }
            }
        }

        val header = navigationView.getHeaderView(0)
        val usernameAside = header.findViewById<TextView>(R.id.usernameAside)
        val emailAside = header.findViewById<TextView>(R.id.emailAside)
        usernameAside.text = UserSingleton.display_name
        emailAside.text = UserSingleton.email
        val pfp = header.findViewById<ImageView>(R.id.pfp)

        UserSingleton.userPfp = SupabaseSingleton.getUserPfp(UserSingleton.userID)
        val imageURL = UserSingleton.userPfp
        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
        val placeholderImage = R.drawable.placeholder_view_vector
        // Load image using Glide with RequestListener
        Glide.with(pfp.context)
            .load(imageURL) // Load the image from the URL
            .placeholder(placeholderImage) // Placeholder while loading
            .error(placeholderImage) // Error image if loading fails
            .override(300, 200) // Resize to desired size (adjust as needed)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable caching
            .thumbnail(0.1f) // Load a smaller thumbnail first
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide the image container on error
                    Log.d("Glide", "Image load failed: ${e?.message}")
                    return false // Allow Glide to handle the error placeholder
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Show the image container when image is loaded successfully
                    pfp.visibility = View.VISIBLE
                    Log.d("Glide", "Image loaded successfully")
                    return false // Allow Glide to handle the resource
                }
            })
            .into(pfp) // Set the ImageView









        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener({ menuItem ->
            Log.d("MainActivity", "Logout clicked")
            SupabaseSingleton.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            true
        })

        navigationView.getMenu().findItem(R.id.nav_edit_profile).setOnMenuItemClickListener({ menuItem ->
            getEditProfileDialog(this).show();
            true
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("Navigation", "Navigated to ${destination.label}")
            logBackStack()
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.setupWithNavController(navController)
        navigationView.setupWithNavController(navController)

//        val usernameAside = binding.root.findViewById<TextView>(R.id.usernameAside)
//        val emailAside = binding.root.findViewById<TextView>(R.id.emailAside)
//        usernameAside.text = UserSingleton.display_name
//        emailAside.text = UserSingleton.email

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun logBackStack() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        Log.d("MainActivity", "Back Stack Entry Count: $backStackEntryCount")

        for (i in 0 until backStackEntryCount) {
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(i)
            Log.d("MainActivity", "Back Stack Entry $i: ${backStackEntry.name}")
        }
    }



}