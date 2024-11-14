package com.example.hkunexus.ui.homePages.create

import android.net.Uri
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
import java.util.Calendar
import java.util.UUID

class CreateGroupViewModel: ViewModel() {

    data class MyGroupsUiState(
        val clubName: String = "",
        val clubDesc: String = "",
        val image: Uri? = null
    )

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    fun setClubName(str: String) {
        _uiState.update {
            it.copy(
                clubName = str
            )
        }
    }

    fun setClubDesc(str: String) {
        _uiState.update {
            it.copy(
                clubDesc = str
            )
        }
    }

    fun setBannerImage(uri: Uri?) {
        _uiState.update {
            it.copy(
                image = uri
            )
        }
    }

    fun hasBannerImage(): Boolean {
        return uiState.value.image != null
    }

    fun canCreate(): Boolean {
        return uiState.value.clubName.trim().isNotEmpty()
    }

    fun create(): Boolean {
        if (canCreate()) {
            // TODO: OwO
            return true
        }
        return false
    }

    fun reset() {
        _uiState.update {
            it.copy(
                clubName = "",
                clubDesc = "",
                image = null,
            )
        }
    }
}

