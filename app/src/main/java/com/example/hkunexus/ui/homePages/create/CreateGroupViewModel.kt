package com.example.hkunexus.ui.homePages.create

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID

class CreateGroupViewModel: ViewModel() {
    var imageFile: File? = File.createTempFile("lol","jpg");
    val BUCKET_URL_PREFIX = "https://ctiaasznssbnyizmglhv.supabase.co/storage/v1/object/public/"

    data class MyGroupsUiState(
        val clubName: String = "",
        val clubDesc: String = "",
        val bannerImage: Uri? = null

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
                bannerImage = uri
            )
        }
    }

    fun hasBannerImage(): Boolean {
        return uiState.value.bannerImage != null
    }

    fun canCreate(): Boolean {
        return uiState.value.clubName.trim().isNotEmpty() && hasBannerImage() && uiState.value.clubDesc.length > 20
    }

    fun create(context: Context?): Boolean {
        if (canCreate()) {
            val bannerImage = uiState.value.bannerImage
            if (bannerImage != null) {
                var mediaArg = ""
                val postIdArg = UUID.randomUUID().toString()
                try {
                    Log.d("POST", imageFile.toString())
                    val result = SupabaseSingleton.uploadImageToBucket(
                        imageFile!!,
                        "user_profiles",
                        filepathArg = "images/attachment_$postIdArg.jpg",
                        register = true
                    )
                    Log.d("POST", result.toString())
                    mediaArg = (BUCKET_URL_PREFIX + result) ?: "";
                    SupabaseSingleton.insertOrUpdateClub(UUID.randomUUID().toString(), uiState.value.clubName, uiState.value.clubDesc, mediaArg)
                } catch (ex : Exception){
                    Log.e("POST", ex.stackTraceToString())

                }
            }
            return true
        }
        return false
    }

    fun reset() {
        _uiState.update {
            it.copy(
                clubName = "",
                clubDesc = "",
                bannerImage = null,
            )
        }
    }
}

