package com.example.hkunexus.ui.homePages.clubLanding

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.databinding.FragmentAssignRoleBinding
import com.example.hkunexus.databinding.FragmentGroupLandingBinding
import com.example.hkunexus.databinding.FragmentUserListBinding
import com.example.hkunexus.ui.homePages.clubLanding.users.UserListListAdapter
import com.example.hkunexus.ui.homePages.clubLanding.users.UserListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class setRoleFragment : Fragment() {

//    private val viewModel: setRoleFragment by viewModels()
    private var _binding: FragmentAssignRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAssignRoleBinding.inflate(inflater, container, false)

        val username = arguments?.getString("display_name")
        val role = arguments?.getString("role")
        val userId = arguments?.getString("userId")
        val clubId = arguments?.getString("clubId")
        var selectedRole = if (binding.radioButtonAdmin.isChecked) "Admin" else "Member"
        binding.selectUser.text = "Selected User: $username"
        binding.currentRole.text = "Current Role: $role"

        if (role == "Member"){
            binding.radioButtonMember.isChecked = true
            binding.radioButtonAdmin.isChecked = false
        }else{
            binding.radioButtonMember.isChecked = false
            binding.radioButtonAdmin.isChecked = true
        }
        binding.createPostButton.isEnabled = binding.radioButtonAdmin.isChecked && (role == "Member") || binding.radioButtonMember.isChecked && (role == "Admin")
        binding.radioButtonMember.setOnClickListener{
            binding.createPostButton.isEnabled = binding.radioButtonAdmin.isChecked && (role == "Member") || binding.radioButtonMember.isChecked && (role == "Admin")
            selectedRole = if (binding.radioButtonAdmin.isChecked) "Admin" else "Member"
        }

        binding.radioButtonAdmin.setOnClickListener{
            binding.createPostButton.isEnabled = binding.radioButtonAdmin.isChecked && (role == "Member") || binding.radioButtonMember.isChecked && (role == "Admin")
            selectedRole = if (binding.radioButtonAdmin.isChecked) "Admin" else "Member"
        }


        binding.createPostButton.setOnClickListener {
            lifecycleScope.launch {
                Log.d("wdadw", selectedRole)
                SupabaseSingleton.setRoleByIdAsync(userId!!, clubId!!, selectedRole)
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}