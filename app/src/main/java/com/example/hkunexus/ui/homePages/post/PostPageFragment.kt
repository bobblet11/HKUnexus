package com.example.hkunexus.ui.homePages.post

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        viewModel.fetchPosts()
        val eventParent = binding.eventWidgetParent

        val title = binding.postEventTitle
        val description = binding.eventPostDescription
        val postImage = binding.eventBannerImage
        val timeSincePosted = binding.root.findViewById<TextView>(R.id.timeSincePosted)

        val postersGroup= binding.root.findViewById<TextView>(R.id.clubNamePost)
        val postersUsername = binding.root.findViewById<TextView>(R.id.postersUsername)

        val eventName: TextView = binding.root.findViewById<TextView>(R.id.eventName)
        val eventDate: TextView = binding.root.findViewById<TextView>(R.id.eventName3)
        val eventTime: TextView = binding.root.findViewById<TextView>(R.id.eventName5)
        val eventLocation: TextView = binding.root.findViewById<TextView>(R.id.eventName6)
        val groupPfp: ImageView = binding.root.findViewById(R.id.groupProfileImage)

        var eventButtonUpdater: (() -> Unit)? = null

        binding.deletePostButton.setOnClickListener {
            val canDelete : Boolean = SupabaseSingleton.getPostById(requireArguments().getString("postID")!!)!!.userId == UserSingleton.userID

            if(!canDelete){
                Toast.makeText(context, "You cannot delete this post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("Delete Post")
                .setTitle("Are you sure you want to delete this post?")
                .setPositiveButton("Delete Post") { dialog, which ->

                    SupabaseSingleton.removePost(requireArguments().getString("postID")!!)
                    findNavController().popBackStack()
                }
                .setNegativeButton("Go Back") { dialog, which ->

                }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->
//                viewModel.fetchPostImage(requireContext(), postImage)
//                viewModel.fetchGroupPfp(requireContext(), groupPfp)
                if (viewModel.uiStatePosts.value.post != null) {
                    // Call this updater function if you want to update the states of buttons
                    eventButtonUpdater = EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
                        binding.root.findViewById(R.id.event_action_buttons),
                        viewModel.uiStatePosts.value.post!!.eventId
                    )
                }




                title.text = state.post?.title
                description.text = state.post?.body
                timeSincePosted.text = state.post?.createdAt
                postersUsername.text = "posted by" + state.post?.displayName
                postersGroup.text = state.post?.clubName

                val b = Bundle()
                b.putString("clubID", state.post?.clubId)
                groupPfp.setOnClickListener{
                    findNavController().navigate(R.id.navigation_group_landing, b)
                }

                eventName.text = state.post?.eventTitle
                eventDate.text = state.post?.createdAt
                eventTime.text = state.post?.eventTimeStart
                eventLocation.text = state.post?.eventLocation

//                if (eventButtonUpdater != null) {
//                    eventButtonUpdater()
//                }

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