package com.example.hkunexus.ui.homePages.mygroups
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
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
import android.view.ViewGroup as ViewGroup

class GroupListAdapter(private val dataSet: ArrayList<ClubDto>, private val context: Context) :
    RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    private var goToPostPage: (String) -> Unit = { clubID: String -> }
    private var lastPosition = -1
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var clubBannerImage: ImageView = view.findViewById<ImageView>(R.id.club_banner_image)
        var clubName: TextView = view.findViewById<TextView>(R.id.club_name)
        var clubDescription: TextView = view.findViewById<TextView>(R.id.club_description)
        var cardView: CardView = view.findViewById<CardView>(R.id.club_card)
        val clubImageContainer: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.club_banner_container)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val club = dataSet[position]
        viewHolder.clubName.text = club.clubName
        viewHolder.clubDescription.text = club.clubDesc
        setAnimation(viewHolder.itemView, position)
        if (club.clubImage == null){

            Log.d("Glide", "Image URL is null")
        } else
        {
            CoroutineScope(Dispatchers.Main).launch {
                val imageURL = club.clubImage
                Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
                if (imageURL != null) {
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
                    Log.d("Glide", "Image URL is null")
                }
            }
        }


        viewHolder.cardView.setOnClickListener {
            goToPostPage(dataSet[position].clubId!!)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<ClubDto>) {
        //call when the data changes.
        this.dataSet.clear()
        Log.d("DWAD", newData.toString())
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (String) -> Unit) {
        goToPostPage = callback
    }
}
