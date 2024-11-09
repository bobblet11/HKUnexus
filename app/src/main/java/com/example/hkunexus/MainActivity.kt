package com.example.hkunexus

import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import androidx.annotation.GravityInt
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.databinding.ActivityMainBinding
import com.example.hkunexus.ui.homePages.create.CreateFragment
import com.example.hkunexus.ui.homePages.explore.ExploreFragment
import com.example.hkunexus.ui.homePages.home.HomeFragment
import com.example.hkunexus.ui.homePages.myevents.MyEventsFragment
import com.example.hkunexus.ui.homePages.mygroups.MyGroupsFragment
import com.example.hkunexus.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var bottomNavView: BottomNavigationView

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

        drawerLayout = binding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener(this)



        bottomNavView = binding.bottomNavView
        bottomNavView.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.navigation_home ->openFragment(HomeFragment())
                R.id.navigation_explore ->openFragment(ExploreFragment())
                R.id.navigation_create ->openFragment(CreateFragment())
                R.id.navigation_my_events ->openFragment(MyEventsFragment())
                R.id.navigation_my_groups ->openFragment(MyGroupsFragment())
            }
            true
        }

        /*val navController = findNavController(R.id.nav_host_fragment_activity_main)
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

        actionBar?.setDisplayHomeAsUpEnabled(true)*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                SupabaseSingleton.logout()
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
       manager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
    }

   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }*/

}