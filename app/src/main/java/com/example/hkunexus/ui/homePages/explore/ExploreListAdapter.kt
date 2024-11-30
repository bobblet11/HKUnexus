package com.example.hkunexus.ui.homePages.explore

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
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExploreListAdapter(private val dataSet: ArrayList<ClubDto>, private val context: Context) :
    RecyclerView.Adapter<ClubInExploreListViewHolder>() {
    private var lastPosition = -1
    private var goToLandingPage: (String) -> Unit = {
        clubId: String -> Log.d("exploreListAdapter", clubId)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClubInExploreListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ClubInExploreListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ClubInExploreListViewHolder, position: Int) {
        val club = dataSet[position]
        viewHolder.clubName.text = club.clubName
        viewHolder.clubDescription.text = club.clubDesc
        val placeholderImage = R.drawable.placeholder_view_vector


        if (club.clubImage == null){
//            viewHolder.clubImageContainer.visibility = View.GONE // Hide if no image URL
            Log.d("Glide", "Image URL is null")
        } else {

            // Load image asynchronously
                CoroutineScope(Dispatchers.Main).launch {

                    val imageURL = club.clubImage
                    Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL

                    if (imageURL != null) {
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
                                    viewHolder.clubImageContainer.visibility = View.GONE
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
                                    viewHolder.clubImageContainer.visibility = View.VISIBLE
                                    Log.d("Glide", "Image loaded successfully")
                                    return false // Allow Glide to handle the resource
                                }
                            })
                            .into(viewHolder.clubBannerImage) // Set the ImageView
                    } else {
//                        viewHolder.clubImageContainer.visibility = View.GONE // Hide if no image URL
                        Log.d("Glide", "Image URL is null")
                    }
                }

        }
        viewHolder.cardView.setOnClickListener {
            goToLandingPage(club.clubId.toString())
        }
        setAnimation(viewHolder.itemView, position)
    }
    override fun getItemCount() = dataSet.size

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.fade)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<ClubDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun setLandingCallback(callback: (String) -> Unit) { goToLandingPage = callback }
}
