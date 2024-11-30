package com.example.hkunexus.ui.homePages.clubLanding

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClubLandingUiState(
    val name: String = "Club Name",
    val description: String = "Club Description",
    val joined: Boolean = false,
    val tags: Array<String> = arrayOf(),
    val numberOfMembers: Int = 0,
    val image: String? = null,
)

data class PostInClubLandingUiState(
    val posts: Array<PostDto> = arrayOf(),
)



// Taken from MyEventsViewModel
class ClubLandingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClubLandingUiState())
    val uiState: StateFlow<ClubLandingUiState> = _uiState.asStateFlow()

    private val _uiStatePosts = MutableStateFlow(PostInClubLandingUiState())
    val uiStatePosts: StateFlow<PostInClubLandingUiState> = _uiStatePosts.asStateFlow()

    private var clubID:String = ""

    private val MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION = 80;

    fun setClubID(newClubID: String?, context: Context?){
        if (newClubID == null){
            Toast.makeText(
                context,
                "Please pass a club ID while opening this page. Defaulting to 0...",
                Toast.LENGTH_SHORT
            ).show()

            //render some error data using data associated with clubID 0
            clubID = ""
        }else{
            clubID = newClubID
        }

        fetchClubData()
        fetchPosts()
    }

    fun joinClub(){

        viewModelScope.launch {

            try{
                val result = SupabaseSingleton.insertOrUpdateCurrentUserToClub(clubID, "Member")
                Log.d("clubLandingViewModel", "Club joining success, $result")

            }catch(e : Exception){
                Log.e("clubLandingViewModel", "Club joining failed , $e")
            }

            val numOfMembers = SupabaseSingleton.getNoOfMembersOfClubAsync(clubID)

            _uiState.update {
                it.copy(
                    joined = true,
                    numberOfMembers = numOfMembers
                )
            }
        }

    }

    fun leaveClub(){

        viewModelScope.launch {
            try{
                val result = SupabaseSingleton.removeCurrentUserToClub(clubID)
                Log.d("clubLandingViewModel", "Club leaving success, $result")
            }catch(e : Exception){
                Log.e("clubLandingViewModel", "Club leaving failed , $e")
            }

            val numOfMembers = SupabaseSingleton.getNoOfMembersOfClubAsync(clubID)

            _uiState.update {
                it.copy(
                    joined = false,
                    numberOfMembers = numOfMembers
                )
            }
        }
    }

    private fun updateClubPosts(newPosts: Array<PostDto>){
        _uiStatePosts.update {
            it.copy(
                posts = newPosts
            )
        }
    }

    private fun updateClubInfo( newInfo: ClubDto){
        _uiState.update {
            it.copy(
                name = newInfo.clubName!!,
                description = newInfo.clubDesc!!,
                joined = newInfo.joined,
                tags = newInfo.tags,
                numberOfMembers = newInfo.numberOfMembers,
                image = newInfo.clubImage
            )
        }
    }

    fun fetchClubData() {
        viewModelScope.launch {
            Log.d("clubLandingViewModel", clubID)
            val tempClub: ClubDto = SupabaseSingleton.getClubByIdAsync(clubID)!!
            tempClub.numberOfMembers =SupabaseSingleton.getNoOfMembersOfClubAsync(clubID)
            tempClub.joined =SupabaseSingleton.checkIsJoinedAsync(clubID, UserSingleton.userID)
            updateClubInfo(tempClub)

        }
    }

    fun fetchImage(context:Context, image:ImageView){
        val placeholderImage = R.drawable.placeholder_view_vector

        viewModelScope.launch {
            val imageURL = uiState.value.image
            Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL
            if (imageURL != null) {
                // Load image using Glide with RequestListener
                Glide.with(context)
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
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
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
                            image.visibility = View.VISIBLE
                            Log.d("Glide", "Image loaded successfully")
                            return false // Allow Glide to handle the resource
                        }
                    })
                    .into(image) // Set the ImageView
            } else {
    //                        viewHolder.clubImageContainer.visibility = View.GONE // Hide if no image URL
                Log.d("Glide", "Image URL is null")
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            val tempList: Array<PostDto> = SupabaseSingleton.getPostsFromClubAsync(clubID).toTypedArray()
            updateClubPosts(tempList)
        }
    }
}