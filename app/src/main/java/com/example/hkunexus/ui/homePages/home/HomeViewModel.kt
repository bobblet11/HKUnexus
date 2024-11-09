package com.example.hkunexus.ui.homePages.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class PostInHomeUiState(
    val posts: Array<PostDto> = arrayOf(),
)



// Taken from MyEventsViewModel
class HomeViewModel : ViewModel() {

    private val _uiStatePosts = MutableStateFlow(PostInHomeUiState())
    val uiStatePosts: StateFlow<PostInHomeUiState> = _uiStatePosts.asStateFlow()

    init {
        fetchPosts()
    }
    
    private fun updateHomePosts(newPosts: Array<PostDto>){
        _uiStatePosts.update {
            it.copy(
                posts = newPosts
            )
        }
    }

    private fun fetchPosts() {
        val tempList: Array<PostDto> = SupabaseSingleton.getPostsFromHome().toTypedArray()
        Log.d("homeViewModel", tempList.toString())
        updateHomePosts(tempList)
    }
}