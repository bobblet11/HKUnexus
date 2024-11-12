package com.example.hkunexus.ui.homePages.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.SupabaseSingleton.getAllJoinedClubs
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CreateGroupViewModel: ViewModel() {

    data class MyGroupsUiState(
        val groupName: String = "",
        val groupDesc: String = "",
    )

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    fun createGroup(){

    }
}

