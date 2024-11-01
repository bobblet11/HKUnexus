package com.example.hkunexus

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hkunexus.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


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
//        val dfrag = DashboardFragment()
//        dfrag.accessToken = SupabaseSingleton.getAccessToken()
//        dfrag.mainActivity = this;
//        addFragment( dfrag, false, "Dashboard")


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

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
        navView.setupWithNavController(navController)

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