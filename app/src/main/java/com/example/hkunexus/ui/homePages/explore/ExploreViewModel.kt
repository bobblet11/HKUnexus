package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.model.Club

class ExploreViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is explore Fragment"
    }

    val clubs: Array<Club> = arrayOf(
        Club("Club 1", "Club 1 Desc", false),
        Club("Club 2", "Club 2 Desc", false),
        Club("Club 3", "Club 3 Desc", false)
    )

    val text: LiveData<String> = _text
}