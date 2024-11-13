package com.example.hkunexus.ui.homePages.post

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class PostInfoState(
    val postId: String? = null,
    val post: PostDto? = null,
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

    public fun fetchPosts() {
        val post: PostDto? = SupabaseSingleton.getPostById(uiStatePosts.value.postId!!)
        Log.d("homeViewModel", post.toString())
        updatePostInfo(post!!)
    }

    public fun setPostID(id:String?) {
        _uiStatePosts.update {
            it.copy(
                postId = id!!
            )
        }
    }

}