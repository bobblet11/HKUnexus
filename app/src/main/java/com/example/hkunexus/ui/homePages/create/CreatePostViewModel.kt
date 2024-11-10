package com.example.hkunexus.ui.homePages.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is createpost Fragment"
    }
    val text: LiveData<String> = _text
}