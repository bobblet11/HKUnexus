package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is explore Fragment"
    }
    val text: LiveData<String> = _text
}