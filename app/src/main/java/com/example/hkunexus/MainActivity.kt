package com.example.hkunexus

import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hkunexus.databinding.ActivityMainBinding
import com.example.hkunexus.ui.login.LoginActivity
import com.example.hkunexus.data.SupabaseSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    fun addFragment(fragment: Fragment?, addToBackStack: Boolean, tag: String?) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        if (fragment != null) {
            ft.replace(R.id.fragmentFrame, fragment, tag)
        }
        ft.commitAllowingStateLoss()
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //val dfrag = DashboardFragment()
        //dfrag.accessToken = SupabaseSingleton.getAccessToken()
        //dfrag.mainActivity = this;
        //addFragment(dfrag, false, "Dashboard")


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // Start LoginActivity instead of replacing a Fragment
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    // Optionally finish the current activity if you don't want to return
                    finish()
                    true // Indicate that the item selection was handled
                }

                else -> false // Allow other items to be handled if necessary
            }
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
//
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            toolbar,
//            R.string.open_nav,
//            R.string.close_nav
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        val bottomNavView: BottomNavigationView = binding.bottomNavView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_explore,
                R.id.navigation_create,
                R.id.navigation_my_events,
                R.id.navigation_my_groups
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

}