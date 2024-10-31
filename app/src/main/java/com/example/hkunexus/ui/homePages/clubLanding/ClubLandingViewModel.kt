package com.example.hkunexus.ui.homePages.clubLanding

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.Post
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ClubLandingUiState(
    val name: String = "Club Name",
    val description: String = "Club Description",
    val joined: Boolean = false,
    val tags: Array<String> = arrayOf(),
    val numberOfMembers: Int = 0,
)

data class PostInClubLandingUiState(
    val posts: Array<Post> = arrayOf(),
)



// Taken from MyEventsViewModel
class ClubLandingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClubLandingUiState())
    val uiState: StateFlow<ClubLandingUiState> = _uiState.asStateFlow()

    private val _uiStatePosts = MutableStateFlow(PostInClubLandingUiState())
    val uiStatePosts: StateFlow<PostInClubLandingUiState> = _uiStatePosts.asStateFlow()

    private var clubID:String = ""

    private val MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION = 80;

    init {
        fetchPosts()
    }

    public fun setClubID(newClubID: String?, context: Context?){
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

    public fun joinClub(){
        _uiState.update {
            it.copy(
                joined = true
            )
        }

        //TODO: add SUPASBASE RPC here
    }

    public fun leaveClub(){
        _uiState.update {
            it.copy(
                joined = false
            )
        }

        //TODO: add SUPASBASE RPC here
    }

    private fun updateClubPosts(newPosts: Array<Post>){
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
            )
        }
    }

    private fun fetchClubData() {
        // TODO: Fetch using Supabase using clubID
        Log.d("clubLandingViewModel", clubID)
        var tempClub: ClubDto = SupabaseSingleton.getClubById(clubID)!!

        val numOfMembers = SupabaseSingleton.getNoOfMembersOfClub(clubID)
        tempClub.numberOfMembers =numOfMembers

        val isJoined = SupabaseSingleton.checkIsJoined(clubID, UserSingleton.userID)

        Log.d("clubLandingViewModel", tempClub.toString())
        if (tempClub == null){
            tempClub = TempData.clubs[0]
        }
        updateClubInfo(tempClub)
    }

    private fun fetchPosts() {
        // TODO: FETCH USING SUPABASE using clubID

        val tempList: Array<Post> = TempData.clubPosts

        for (item: Post in tempList) {

            if (item.postText.length >= MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION) {
                val newPostText =
                    item.postText.substring(0, MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION - 4) + "..."
                item.postText = newPostText
            }
        }
        updateClubPosts(tempList)
    }
}