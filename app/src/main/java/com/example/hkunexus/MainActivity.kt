package com.example.hkunexus

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hkunexus.databinding.ActivityMainBinding
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import androidx.lifecycle.lifecycleScope
import com.example.hkunexus.ui.homePages.dashboard.DashboardFragment
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var accessToken : String? = null;
    private lateinit var superbase : SupabaseClient;
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

        val bundle: Bundle? = intent.extras
        bundle?.let{
            bundle.apply{
                accessToken = getString("AccessToken");
            }
        }

        val dfrag = DashboardFragment()
        dfrag.accessToken = accessToken
        dfrag.mainActivity = this;
        addFragment( dfrag, false, "Dashboard")


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
                R.id.navigation_my_events,
//                R.id.navigation_my_groups,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)




    }









}