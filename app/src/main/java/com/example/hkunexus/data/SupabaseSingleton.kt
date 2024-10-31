package com.example.hkunexus.data

import android.util.Log
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.EventPost
import com.example.hkunexus.data.model.GenericPost
import com.example.hkunexus.data.model.Post
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.Tag
import com.example.hkunexus.data.model.dto.UserToClubDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.system.exitProcess

import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.putJsonObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.putJsonArray
import org.json.JSONArray
import java.util.UUID
import kotlin.uuid.Uuid


object SupabaseSingleton{

    private var url = "https://ctiaasznssbnyizmglhv.supabase.co"
    private var key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0aWFhc3puc3Nibnlpem1nbGh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NDQ3NzEsImV4cCI6MjA0NDEyMDc3MX0.t0-AHECeFc0PWItTVJ-X0BGGclh_LEbFhFOtBi9rNd4"
    private var accessToken: String? = null
    private var session: UserSession? = null
    private var currentUser: UserInfo? = null

    init {
        println("attempting to create supabase client using $url, $key")
    }

    private var client: SupabaseClient? = null

    init {
        getSupabase()
    }

    private fun getSupabase() {
        Log.e("SupabaseSingleton", "attempting to connect with supabase")
        try {
            this.client = createSupabaseClient(
                supabaseUrl = this.url,
                supabaseKey = this.key
            ) {
                install(Postgrest)
                install(Auth)
            }
        }catch (e: Exception) {
            Log.e("SupabaseSingleton", "could not create supabase client\nClosing app", e)
            exitProcess(0);
        }
        Log.e("SupabaseSingleton", "successfully connected with supabase")
    }

    public fun login(email: String, password: String):Boolean{

        return runBlocking {
            try {
                val result = client!!.auth.signInWith(Email) {
                    this.email = email;
                    this.password = password;
                }

                currentUser = client!!.auth.retrieveUserForCurrentSession(updateSession = true)
                session = client!!.auth.currentSessionOrNull()
                accessToken = session!!.accessToken

                Log.d("SupabaseSingleton", "Sign-in successful: $result")

                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-in failed: $e")
                currentUser = null
                session = null
                accessToken = null
                return@runBlocking false
            }
        }
    }

    public fun isEmailAvailable(email: String):Boolean{
        return runBlocking {
            try{
                val jsonParams = buildJsonObject {
                    put("email", email)
                }
                val result = client?.postgrest?.rpc("check_email_exists", jsonParams)?.data
                Log.d("SupabaseSingleton", result.toString())
                return@runBlocking !result.toBoolean()
            }catch(e: Exception){
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }
    }


    public fun isDisplayNameAvailable(displayName: String):Boolean{
        //query public user table
        return runBlocking {
            try{
                val jsonParams = buildJsonObject {
                    put("display_name", displayName)
                }
                val result = client?.postgrest?.rpc("check_display_name_exists", jsonParams)?.data
                Log.d("SupabaseSingleton", result.toString())
                return@runBlocking !result.toBoolean()
            }catch(e: Exception){
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }

    }


    public fun register(firstName: String, lastName: String, email: String, password: String, username : String):Boolean{
        return runBlocking {
            try {
                val user = client!!.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("first_name", firstName)
                        put("last_name", lastName)
                        put("display_name", username)
                        put("profile_picture", "")
                        put("joined_at", "")
                    }
                }

                Log.d("SupabaseSingleton", "Sign-up successful: $user")

                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-in failed: $e")
                currentUser = null
                session = null
                accessToken = null
                return@runBlocking false
            }

        }
    }

    public fun getAccessToken():String{
        try{
            if (this.accessToken!!.isNotEmpty()){
                Log.d("SupabaseSingleton", "retrieved access token")
                return this.accessToken!!
            }
        }catch (e: Exception) {
            Log.d("SupabaseSingleton", "no access token available, sign-in again")
        }
        return ""
    }



    public fun authenticateOtp(otpInput: String): Boolean{
        return runBlocking {
            try {
                client!!.auth.verifyEmailOtp(type = OtpType.Email.SIGNUP, email = "u3596276@connect.hku.hk", token = otpInput)

                Log.d("SupabaseSingleton", "verified OTP $otpInput")

                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "OTP verification failed: $e")
                currentUser = null
                session = null
                accessToken = null
                return@runBlocking false
            }

        }


        return true
    }

    public fun getClubById(club_uuid : String) : ClubDto?{
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc("get_club_by_id", buildJsonObject { put("club_uuid", club_uuid) })
                Log.d("SupabaseSingleton", "get_club_by_id_rpc, $result")
                val output = result.decodeSingle<ClubDto>();
                Log.d("SupabaseSingleton", "get_club_by_id_rpc_output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    public fun getNoOfMembersOfClub(club_uuid: String): Int?{
        return runBlocking {
            @Serializable
            data class OutputDto(
                @SerialName("id")
                val id : String,
                @SerialName("membercount")
                val memberCount : Int,
            )
            val func_name = "get_no_of_members_of_club";
            val func_param = buildJsonObject {
                put("club_uuid", club_uuid)
            }
            try {
                val result = client!!.postgrest.rpc(func_name, func_param)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeSingle<OutputDto>().memberCount;
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }


    public fun getTagById(tag_id : String): Tag?{
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc("get_tag_by_id", buildJsonObject { put("tag_id", tag_id) })
                Log.d("SupabaseSingleton", "get_tag_by_id_rpc, $result")
                val output = result.decodeSingle<Tag>();
                Log.d("SupabaseSingleton", "get_tag_by_id_rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    public fun getTagsOfClub(club_uuid: String): List<Tag>?{
        return runBlocking {
            val func_name = "get_tags_of_club";
            val func_param = buildJsonObject {
                put("club_uuid", club_uuid)
            }
            try {
                val result = client!!.postgrest.rpc(func_name, func_param)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeList<Tag>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    public fun insertOrUpdateUserToClub(user_id_arg: String, club_id_arg:String, role_arg: String): UserToClubDto?{
        return runBlocking {
            val func_name = "insert_or_update_user_to_club";
            val func_param = buildJsonObject {
                put("user_id_arg", user_id_arg)
                put("club_id_arg", club_id_arg)
                put("role_arg", role_arg)
            }
            try {
                val result = client!!.postgrest.rpc(func_name, func_param)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeSingle<UserToClubDto>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    public fun removeUserToClub(user_id_arg: String, club_id_arg:String): UserToClubDto?{
        return runBlocking {
            val func_name = "remove_user_to_club";
            val func_param = buildJsonObject {
                put("user_id_arg", user_id_arg)
                put("club_id_arg", club_id_arg)
            }
            try {
                val result = client!!.postgrest.rpc(func_name, func_param)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeSingle<UserToClubDto>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    public fun searchClubsByLikeName(query : String): List<ClubDto>?{
        return runBlocking {
            val func_name = "search_clubs_by_like_name";
            val func_param = buildJsonObject {
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(func_name, func_param)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeList<ClubDto>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    public fun searchClubsByTags(tag_ids : Array<String>): List<ClubDto>?{
        return runBlocking {
            val func_name = "search_clubs_by_tags";
            try {
                val result = client!!.postgrest.rpc(func_name, buildJsonObject {
                    put("tag_ids", Json.encodeToJsonElement(tag_ids))
                    })

                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeList<ClubDto>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    public fun getEventPostByUser(): List<EventPost>?{
        return runBlocking {
            val userId = currentUser?.id ?: return@runBlocking null
            val func_name = "get_event_post_from_user";
            try{
                val result = client!!.postgrest.rpc(func_name, buildJsonObject {
                    put("user_uuid", Json.encodeToJsonElement(userId))
                })
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeList<EventPost>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    public fun getRecentPosts(): List<EventPost>?{
        return runBlocking {
            val func_name = "get_recent_posts";
            try{
                val result = client!!.postgrest.rpc(func_name)
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                val output = result.decodeList<EventPost>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    public fun getRandomClubs(): List<ClubDto>? {
        return runBlocking {
            val func_name = "get_20_random_clubs"
            try{
                val result = client!!.postgrest.rpc(func_name)
                val resultData = result.data
                Log.d("SupabaseSingleton", "$func_name rpc, $result")
                Log.d("SupabaseSingleton", resultData)
                val output = result.decodeList<ClubDto>();
                Log.d("SupabaseSingleton", "$func_name rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

}