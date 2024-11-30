package com.example.hkunexus.ui.homePages.clubLanding.users

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
import com.example.hkunexus.data.model.dto.UserProfileWithRoleDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListListAdapter(private val dataSet: ArrayList<UserProfileWithRoleDto>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var goToRolePage: (String, String, String, String) -> Unit = { userId: String, role: String, display_name: String, clubId:String -> }
    private var lastPosition = -1
    private var clubId: String? = null
    private var isAdmin: Boolean= false

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //creates the PostInCLubViewHolder i.e. a post card/event post card.
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.users_user_card, viewGroup, false)
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder: UserViewHolder = holder as UserViewHolder
        viewHolder.username.text = """${dataSet[position].displayName}  (${dataSet[position].role} )"""

        viewHolder.background.setOnClickListener {
            if (isAdmin){
                goToRolePage(dataSet[position].id, dataSet[position].role, dataSet[position].displayName, clubId!!)
            }
        }

        setAnimation(viewHolder.itemView, position)


        val userPfp = dataSet[position].profilePicture
        if (userPfp == null || userPfp.isEmpty()){
            Log.d("Glide", "Image URL is null")
        } else
        {
            viewHolder.userPfp.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.Main).launch {
                val imageURL = userPfp
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
                            viewHolder.userPfp.visibility = View.VISIBLE
                            Log.d("Glide", "Image loaded successfully")
                            return false // Allow Glide to handle the resource
                        }
                    })
                    .into(viewHolder.userPfp) // Set the ImageView
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

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (String, String,String,String) -> Unit) {
        goToRolePage = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<UserProfileWithRoleDto>){
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun setClubId(clubId:String){
        this.clubId = clubId
    }
    fun setIsAdmin(isAdmin: Boolean){
        this.isAdmin = isAdmin
    }
}
