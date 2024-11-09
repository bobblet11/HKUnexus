package com.example.hkunexus

import MyEventsSlide
import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.databinding.ActivityMainBinding
import com.example.hkunexus.ui.homePages.create.CreateSlideFragment
import com.example.hkunexus.ui.homePages.explore.ExploreFragment
import com.example.hkunexus.ui.homePages.home.HomeFragment

import com.example.hkunexus.ui.homePages.mygroups.MyGroupsFragment
import com.example.hkunexus.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        toolbar = findViewById<Toolbar>(R.id.toolbar)
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

        val headerView : View = navView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.usernameAside)
        val navUserEmail : TextView = headerView.findViewById(R.id.emailAside)

        navUsername.text = UserSingleton.display_name
        navUserEmail.text = UserSingleton.email


        bottomNavView = binding.bottomNavView
        bottomNavView.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.navigation_home ->openFragment(HomeFragment(), "Home")
                R.id.navigation_explore ->openFragment(ExploreFragment(), "Explore")
                R.id.navigation_create ->openFragment(CreateSlideFragment(), "Create")
                R.id.navigation_my_events ->openFragment(MyEventsSlide(), "My Events")
                R.id.navigation_my_groups ->openFragment(MyGroupsFragment(), "My Groups")
            }
            true
        }
        openFragment(HomeFragment(),"Home")

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment, name: String){
        getSupportActionBar()?.setTitle(name);
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