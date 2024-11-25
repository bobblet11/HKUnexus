package com.example.hkunexus.ui.homePages.post

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.ortiz.touchview.TouchImageView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.hkunexus.R
import com.example.hkunexus.data.EventInterface
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.databinding.FragmentEventPostPageBinding
import com.example.hkunexus.databinding.FragmentHomeBinding
import com.example.hkunexus.databinding.FragmentPostPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.hkunexus.BuildConfig

class PostPageFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()
    private var _binding: FragmentEventPostPageBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventPostPageBinding.inflate(inflater, container, false)
        viewModel.setPostID(arguments?.getString("postID"))

        viewModel.fetchPost()
        
        val eventParent = binding.eventWidgetParent

        val title = binding.postEventTitle
        val description = binding.eventPostDescription
        val postImage = binding.eventBannerImage
        val postImageContainer = binding.imageCard
        val timeSincePosted = binding.root.findViewById<TextView>(R.id.timeSincePosted)

        val postersGroup= binding.root.findViewById<TextView>(R.id.clubNamePost)
        val postersUsername = binding.root.findViewById<TextView>(R.id.postersUsername)

        val eventName: TextView = binding.root.findViewById<TextView>(R.id.eventName)
        val eventDate: TextView = binding.root.findViewById<TextView>(R.id.eventName3)
        val eventTime: TextView = binding.root.findViewById<TextView>(R.id.eventName5)
        val eventLocation: TextView = binding.root.findViewById<TextView>(R.id.eventName6)
        val clubPfp: ImageView = binding.root.findViewById(R.id.groupProfileImage)

        var eventMap: TouchImageView = binding.root.findViewById<TouchImageView>(R.id.map_view)

        var eventButtonUpdater: (() -> Unit)? = null
        val deletePost = binding.root.findViewById<ImageView>(R.id.deletePost)
        deletePost.setOnClickListener {
            val canDelete : Boolean = SupabaseSingleton.getPostById(requireArguments().getString("postID")!!)!!.userId == UserSingleton.userID

            if(!canDelete){
                Toast.makeText(context, "You cannot delete this post", Toast.LENGTH_SHORT).show()
            }
            else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setTitle("Delete Post")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Delete Post") { dialog, which ->

                        SupabaseSingleton.removePost(requireArguments().getString("postID")!!)
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Go Back") { dialog, which ->

                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->
                var eventCoordinates: String? = state.post?.eventCoordinate
//                viewModel.fetchPostImage(requireContext(), postImage)
//                viewModel.fetchGroupPfp(requireContext(), groupPfp)
                if (viewModel.uiStatePosts.value.post != null) {
                    // Call this updater function if you want to update the states of buttons
                    eventButtonUpdater = EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
                        binding.root.findViewById(R.id.event_action_buttons),
                        viewModel.uiStatePosts.value.post!!.eventId
                    )
                }

                if (eventCoordinates != null){
                    val zoom = 11
                    val size = "1080x1080"
                    val mapType = "roadmap"
                    val marker1 = "color:red|$eventCoordinates"
                    val apiKey = BuildConfig.API_KEY // Replace with your actual API key
                    val url = "https://maps.googleapis.com/maps/api/staticmap?center=$eventCoordinates&zoom=$zoom&size=$size&maptype=$mapType&markers=$marker1&key=$apiKey"

                    Log.d("Actual EventListAdapter", "Loading map from URL: $url")

                    // Load and cache the bitmap using Glide
                    Glide.with(eventMap.context)
                        .asBitmap()
                        .load(url)
                        .error(R.drawable.placeholder_view_vector) // Placeholder in case of error
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                eventMap.setImageBitmap(resource)
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)
                                eventMap.setImageDrawable(errorDrawable)
                            }
                        })
                }


                deletePost.visibility = if (state.canDelete) View.VISIBLE else View.GONE
                title.text = state.post?.title
                description.text = state.post?.body
                timeSincePosted.text = state.post?.createdAt
                postersUsername.text = "posted by " + state.post?.displayName
                postersGroup.text = state.post?.clubName

                val b = Bundle()
                b.putString("clubID", state.post?.clubId)
                clubPfp.setOnClickListener{
                    findNavController().navigate(R.id.navigation_group_landing, b)
                }

                eventName.text = state.post?.eventTitle
                eventDate.text = state.post?.createdAt
                eventTime.text = state.post?.eventTimeStart
                eventLocation.text = state.post?.eventLocation



                if (state.post == null || state.post.userPfp!!.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    clubPfp.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = state.post.userPfp
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(clubPfp.context)
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
                                    clubPfp.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(clubPfp) // Set the ImageView
                    }
                }


                if (state.post == null || state.post.media.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    postImageContainer.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = state.post.media
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(postImage.context)
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
                                    postImageContainer.visibility = View.GONE
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
                                    postImageContainer.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(postImage) // Set the ImageView
                    }
                }




                Log.d("tes", state.post.toString())
                eventParent.visibility = if (state.post?.eventId != null) View.VISIBLE else View.GONE
            }
        }

        Log.d("postPageFrag", arguments?.getString("postID").toString())


        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{

            // Your code goes here
            // In this code, we are just changing the text in the
            // textbox

            viewModel.fetchPost()

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