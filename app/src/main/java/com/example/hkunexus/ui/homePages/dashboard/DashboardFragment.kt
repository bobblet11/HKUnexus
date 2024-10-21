package com.example.hkunexus.ui.homePages.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hkunexus.MainActivity
import com.example.hkunexus.data.getSupabase
import com.example.hkunexus.data.model.UserProfile
import com.example.hkunexus.databinding.FragmentDashboardBinding
import com.example.hkunexus.ui.login.LoginActivity
import com.example.hkunexus.ui.login.LoginActivity.DemoRowDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    var accessToken : String? = null;
    var mainActivity : Activity? = null;

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val usernameField: TextView = binding.usernameField
        val bioField: TextView = binding.bioField
        val logoutButton : Button = binding.logoutButton
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val supabase = getSupabase()
                var uuid = "";

                Log.d("at", accessToken!!)
                try {
                    val user = supabase.auth.retrieveUser(accessToken!!)
                    uuid = user.id;
                    Log.d("FragSign", "Frag sign success")
                }
                catch(e: Exception){
                    Log.e("FragSign", "Frag sign fail")
                }


                val profile: UserProfile = supabase.from("profile").select() {
                    filter{
                        eq("user_uuid", uuid)
                    }
                }.decodeSingle<UserProfile>()

                usernameField.text = profile.username
                bioField.text = profile.bio

            }
        }

        logoutButton.setOnClickListener{
            mainActivity!!.finish();
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}