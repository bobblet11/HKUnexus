package com.example.hkunexus.ui.homePages.post

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PostInfoState(
    val postId: String? = null,
    val post: PostDto? = null,
    val canDelete : Boolean = false
)


// Taken from MyEventsViewModel
class PostViewModel : ViewModel() {

    private val _uiStatePosts = MutableStateFlow(PostInfoState())
    val uiStatePosts: StateFlow<PostInfoState> = _uiStatePosts.asStateFlow()

    private fun updatePostInfo(newPost: PostDto) {
        _uiStatePosts.update {
            it.copy(
                post = newPost
            )
        }
    }

    private fun updateCanDelete(canDelete: Boolean) {
        _uiStatePosts.update {
            it.copy(
                canDelete = canDelete
            )
        }
    }



    fun fetchPosts() {
        viewModelScope.launch {
            val post: PostDto? = SupabaseSingleton.getPostByIdAsync(uiStatePosts.value.postId!!)
            Log.d("homeViewModel", post.toString())
            updatePostInfo(post!!)
            val canDelete = SupabaseSingleton.getPostById(uiStatePosts.value.postId!!)!!.userId == UserSingleton.userID
            updateCanDelete(canDelete)
        }

    }

    public fun setPostID(id:String?) {
        _uiStatePosts.update {
            it.copy(
                postId = id!!
            )
        }
    }
 }


