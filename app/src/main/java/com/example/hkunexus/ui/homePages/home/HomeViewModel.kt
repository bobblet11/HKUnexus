package com.example.hkunexus.ui.homePages.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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

    private fun updateHomePosts(newPosts: Array<PostDto>) {
        _uiStatePosts.update {
            it.copy(
                posts = newPosts
            )
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            updateHomePosts(arrayOf())

            val tempList: Array<PostDto> = SupabaseSingleton.getPostsFromHomeAsync().toTypedArray()

            for (P in tempList){
                P.displayName = SupabaseSingleton.getDisplayNameAsync(P.userId)
                P.clubName = SupabaseSingleton.getClubNameAsync(P.clubId)
            }

            updateHomePosts(tempList)
        }
    }

}