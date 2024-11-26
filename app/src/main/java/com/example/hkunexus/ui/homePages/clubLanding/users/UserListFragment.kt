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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentGroupLandingBinding
import com.example.hkunexus.databinding.FragmentUserListBinding
import com.example.hkunexus.ui.homePages.clubLanding.users.UserListListAdapter
import com.example.hkunexus.ui.homePages.clubLanding.users.UserListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        val userRecycler = binding.userRecycler
        viewModel.updateClubID(arguments?.getString("clubID"))
        val userListAdapter = UserListListAdapter(arrayListOf(), requireContext())
        viewModel.fetchMembers(userListAdapter)
        Log.d("wdadawd",viewModel.uiState.value.isAdmin.toString())
        userRecycler.adapter = userListAdapter
        userListAdapter.setClubId(arguments?.getString("clubID")!!)
        userListAdapter.setPostPageCallBack ({ userID: String, role: String, username: String, clubID: String ->
            val b = Bundle()
            b.putString("userId", userID)
            b.putString("role", role)
            b.putString("display_name", username)
            b.putString("clubId", clubID)
            findNavController().navigate(R.id.action_userListFragment_to_setRoleFragment, b)
        })

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                userListAdapter.updateDataSet(state.members.toCollection(ArrayList()))
            }
        }

        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{
            viewModel.fetchMembers(userListAdapter)
            swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}