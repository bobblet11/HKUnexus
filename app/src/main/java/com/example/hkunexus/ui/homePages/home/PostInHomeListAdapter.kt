package com.example.hkunexus.ui.homePages.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hkunexus.R
import com.example.hkunexus.data.EventInterface
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostInHomeListAdapter(private val dataSet: ArrayList<PostDto>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lastPosition = -1

    private var goToPostPage: (String) -> Unit = { postID: String -> }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return if (dataSet[position].eventId == null) 1 else 0
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            //EVENT POST
            0 -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_event_card_home, viewGroup, false)
                return EventPostInHomeViewHolder(view)
            }
            //NORMAL POST
            1 -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_card_home, viewGroup, false)
                return PostInHomeViewHolder(view)
            }
            //DEFAULT IS NORMAL POST
            else -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_card_home, viewGroup, false)
                return PostInHomeViewHolder(view)
            }
        }

    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.fade)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            //EVENT POST
            0 -> {
                val viewHolder: EventPostInHomeViewHolder = holder as EventPostInHomeViewHolder
//                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)

                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.eventLocation.text = dataSet[position].eventLocation
                viewHolder.eventTime.text = dataSet[position].eventTimeStart
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName

                val eventId = dataSet[position].eventId

                // Call this updater function if you want to update the states of buttons
                val updater = EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
                    viewHolder.eventButtonsView, eventId
                )

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }

                setAnimation(viewHolder.itemView, position);

                if (dataSet[position].media.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    viewHolder.postImageContainer.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].media
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.postImageContainer.visibility = View.GONE
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
                                    viewHolder.postImageContainer.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.postImage) // Set the ImageView
                    }
                }

                if (dataSet[position].userPfp == null || dataSet[position].userPfp!!.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    viewHolder.clubPfp.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].userPfp
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.clubPfp.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.clubPfp) // Set the ImageView
                    }
                }


            }
            //NORMAL POST
            1 -> {

                Log.d("t", dataSet[position].media)
                val viewHolder: PostInHomeViewHolder = holder as PostInHomeViewHolder
//                viewHolder.postersUsername.text =  SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
                setAnimation(viewHolder.itemView, position);

                if (dataSet[position].media.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    viewHolder.postImageContainer.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].media
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.postImageContainer.visibility = View.GONE
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
                                    viewHolder.postImageContainer.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.postImage) // Set the ImageView
                    }
                }


                if (dataSet[position].userPfp == null || dataSet[position].userPfp!!.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    viewHolder.clubPfp.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].userPfp
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.clubPfp.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.clubPfp) // Set the ImageView
                    }
                }
            }
            //DEFAULT IS NORMAL POST
            else -> {
                val viewHolder: PostInHomeViewHolder = holder as PostInHomeViewHolder
                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }

                setAnimation(viewHolder.itemView, position);

                if (dataSet[position].media.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else {
                    viewHolder.postImageContainer.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].media
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.postImageContainer.visibility = View.GONE
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
                                    viewHolder.postImageContainer.visibility = View.GONE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.postImage) // Set the ImageView
                    }
                }


                if (dataSet[position].userPfp == null || dataSet[position].userPfp!!.isEmpty()){
                    Log.d("Glide", "Image URL is null")
                } else
                {
                    viewHolder.clubPfp.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        val imageURL = dataSet[position].userPfp
                        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                        val placeholderImage = R.drawable.placeholder_view_vector
                        // Load image using Glide with RequestListener
                        Glide.with(viewHolder.itemView.context)
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
                                    viewHolder.clubPfp.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.clubPfp) // Set the ImageView
                    }
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (String) -> Unit) {
        goToPostPage = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData: ArrayList<PostDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }



}
