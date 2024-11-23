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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClubLandingFragment : Fragment() {

    private val viewModel: ClubLandingViewModel by viewModels()
    private var _binding: FragmentGroupLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupLandingBinding.inflate(inflater, container, false)

        val groupLandingPostsRecycler = binding.groupLandingPostsRecycler
        val clubNameView = binding.clubName
        val clubDescriptionView = binding.clubDescription
        val joinButton = binding.clubJoinButton
        val leaveButton = binding.clubLeaveButton
        val numberOfMembers = binding.clubMemberCount
        val image = binding.clubBanner

        val postListAdapter = PostInClubListAdapter(arrayListOf(), requireContext())
        //set the clubID and fetch required data using clubID

        viewModel.setClubID(arguments?.getString("clubID"), context)

        groupLandingPostsRecycler.adapter = postListAdapter

        postListAdapter.setPostPageCallBack ({ postId: String ->
            val b = Bundle()
            b.putString("postID", postId)

            findNavController().navigate(R.id.navigation_post_page, b)
        })


        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Update UI based on the new state
                clubNameView.text = state.name
                clubDescriptionView.text = state.description
                joinButton.visibility = if (state.joined) View.GONE else View.VISIBLE
                leaveButton.visibility = if (state.joined) View.VISIBLE else View.GONE
                numberOfMembers.text = state.numberOfMembers.toString() + " members"

                if (state.image == null || state.image.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                    image.visibility = View.VISIBLE
                    image.setImageResource(R.drawable.ic_launcher_background)
                } else
                {
                    image.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = state.image
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(image.context)
                            .load(imageURL) // Load the image from the URL
                            .placeholder(placeholderImage) // Placeholder while loading
                            .error(placeholderImage) // Error image if loading fails
                            .override(300, 200) // Resize to desired size (adjust as needed)
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable caching
                            .thumbnail(0.1f) // Load a smaller thumbnail first
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    // Hide the image container on error
                                    image.visibility = View.VISIBLE
                                    image.setImageResource(R.drawable.ic_launcher_background)
                                    Log.d("Glide", "Image load failed: ${e?.message}")
                                    return false // Allow Glide to handle the error placeholder
                                }
                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    // Show the image container when image is loaded successfully
                                    image.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(image) // Set the ImageView
                    }
                }
            }

        }

        joinButton.setOnClickListener {
            viewModel.joinClub()
        }

        leaveButton.setOnClickListener {
            viewModel.leaveClub()
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->
                postListAdapter.updateDataSet(state.posts.toCollection(ArrayList()))
            }
        }

        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{

            // Your code goes here
            // In this code, we are just changing the text in the
            // textbox

            viewModel.fetchClubData()
            viewModel.fetchPosts()
            viewModel.fetchImage(requireContext(), image)
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