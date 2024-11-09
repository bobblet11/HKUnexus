package com.example.hkunexus.ui.homePages.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hkunexus.databinding.FragmentHomeBinding
import com.example.hkunexus.ui.homePages.clubLanding.ClubLandingViewModel
import com.example.hkunexus.ui.homePages.clubLanding.PostInClubListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val homePostsRecycler = binding.homeRecycler

        val postListAdapter = PostInHomeListAdapter(arrayListOf())

        homePostsRecycler.adapter = postListAdapter

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->
                postListAdapter.updateDataSet(state.posts.toCollection(ArrayList()))
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}