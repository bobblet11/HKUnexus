package com.example.hkunexus.data

import com.example.hkunexus.data.model.dto.UserProfileDto


object UserSingleton{
    public var userID = ""
    public var userProfile: UserProfileDto? = null


    public fun clear(){
        userID = ""
        userProfile = null
    }

}