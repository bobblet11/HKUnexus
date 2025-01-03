package com.example.hkunexus.ui.homePages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentHomeBinding
import com.example.hkunexus.ui.homePages.mygroups.GroupListAdapter
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

        postListAdapter.setPostPageCallBack ({ postId: String ->
            val b = Bundle()
            b.putString("postID", postId)

            findNavController().navigate(R.id.navigation_post_page, b)

        })
        val nothingToSee = binding.nothingToSeeHere
        homePostsRecycler.adapter = postListAdapter
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->

                if (state.posts.isEmpty()){
                    nothingToSee.visibility = View.VISIBLE
                    homePostsRecycler.visibility = View.GONE
                }else{
                    nothingToSee.visibility = View.GONE
                    homePostsRecycler.visibility = View.VISIBLE
                }
                postListAdapter.updateDataSet(state.posts.toCollection(ArrayList()))
            }
        }

        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{

            // Your code goes here
            // In this code, we are just changing the text in the
            // textbox

            viewModel.fetchPosts()

            // This line is important as it explicitly refreshes only once
            // If "true" it implicitly refreshes forever
            swipeRefreshLayout.isRefreshing = false
        }



        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}