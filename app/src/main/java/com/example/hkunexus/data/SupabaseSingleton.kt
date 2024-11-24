package com.example.hkunexus.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.metrics.Event
import android.util.Log
import android.widget.TextView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.data.model.dto.EventToPost
import com.example.hkunexus.data.model.dto.PostDto
import com.example.hkunexus.data.model.dto.Tag
import com.example.hkunexus.data.model.dto.UserToClubDto
import com.example.hkunexus.data.model.dto.UserToEventDto
import com.example.hkunexus.data.model.dto.fromPostToEvent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.system.exitProcess


object SupabaseSingleton {

    private var url = "https://ctiaasznssbnyizmglhv.supabase.co"
    private var key =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0aWFhc3puc3Nibnlpem1nbGh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NDQ3NzEsImV4cCI6MjA0NDEyMDc3MX0.t0-AHECeFc0PWItTVJ-X0BGGclh_LEbFhFOtBi9rNd4"
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
                install(Storage)
            }
        } catch (e: Exception) {
            Log.e("SupabaseSingleton", "could not create supabase client\nClosing app", e)
            exitProcess(0)
        }
        Log.e("SupabaseSingleton", "successfully connected with supabase")
    }

    suspend fun login(email: String, password: String): Boolean {
        try {
            val result = client!!.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            currentUser = client!!.auth.retrieveUserForCurrentSession(updateSession = true)
            session = client!!.auth.currentSessionOrNull()
            accessToken = session!!.accessToken

            Log.d("SupabaseSingleton", "Sign-in successful: $result")
            UserSingleton.userID = currentUser!!.id!!
            UserSingleton.email = currentUser!!.email!!
            UserSingleton.display_name = getDisplayName(currentUser!!.id)
//                UserSingleton.userProfile = getUserProfile(userID = currentUser!!.id)





            return true

        } catch (e: Exception) {
            Log.d("SupabaseSingleton", "Sign-in failed: $e")
            currentUser = null
            session = null
            accessToken = null
            return false
        }

    }

    fun logout(): Boolean {

        return runBlocking {
            try {
                val result = client!!.auth.signOut()
                UserSingleton.clear()

                Log.d("SupabaseSingleton", "Sign-out successful: $result")
                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-out failed: $e")
                return@runBlocking false
            }
        }
    }

    fun isEmailAvailable(email: String): Boolean {
        return runBlocking {
            try {
                val jsonParams = buildJsonObject {
                    put("email", email)
                }
                val result = client?.postgrest?.rpc("check_email_exists", jsonParams)?.data
                Log.d("SupabaseSingleton", result.toString())
                return@runBlocking !result.toBoolean()
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }
    }

    fun getDisplayName(userID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("display_name_")
            var display_name_: String = "0",
        )

        val funcName = "get_display_name"
        val funcParam = buildJsonObject {
            put("user_uuid", userID)
        }
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output.display_name_
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking userID.substring(0, 10)
            }
        }
    }

    suspend fun getDisplayNameAsync(userID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("display_name_")
            var display_name_: String = "0",
        )

        val funcName = "get_display_name"
        val funcParam = buildJsonObject {
            put("user_uuid", userID)
        }
        return withContext(Dispatchers.IO) {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output.display_name_
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                userID.substring(0, 10)
            }
        }
    }

    suspend fun getClubBannerAsync(clubID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("image")
            var media: String = "",
        )

        val funcName = "get_club_banner_image"
        val funcParam = buildJsonObject {
            put("club_id", clubID)
        }
        return withContext(Dispatchers.IO) {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output.media
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                ""
            }
        }
    }

    suspend fun getUserPfpAsync(userID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("profile_picture")
            var media: String = "",
        )

        val funcName = "get_user_profile_image"
        val funcParam = buildJsonObject {
            put("user_id", userID)
        }
        return withContext(Dispatchers.IO) {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output.media
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                ""
            }
        }
    }

    fun getClubName(clubID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("name")
            var name: String = "0",
        )

        val funcName = "get_club_name"
        val funcParam = buildJsonObject {
            put("club_uuid", clubID)
        }
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output.name
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking clubID.substring(0, 10)
            }
        }
    }

    suspend fun getClubNameAsync(clubID: String): String {

        @Serializable
        data class outputDTO(
            @SerialName("name")
            var name: String = "0",
        )

        val funcName = "get_club_name"
        val funcParam = buildJsonObject {
            put("club_uuid", clubID)
        }
        return withContext(Dispatchers.IO)  {
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: outputDTO = result.decodeSingle<outputDTO>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output.name
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                clubID.substring(0, 10)
            }
        }
    }


    fun isDisplayNameAvailable(displayName: String): Boolean {
        //query public user table
        return runBlocking {
            try {
                val jsonParams = buildJsonObject {
                    put("display_name", displayName)
                }
                val result = client?.postgrest?.rpc("check_display_name_exists", jsonParams)?.data
                Log.d("SupabaseSingleton", result.toString())
                return@runBlocking !result.toBoolean()
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }

    }


    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        username: String,
        mediaArg: String
    ): Boolean {
        return runBlocking {
            try {
                val user = client!!.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("first_name", firstName)
                        put("email_", email)
                        put("last_name", lastName)
                        put("display_name_", username)
                        put("profile_picture", mediaArg)
//                        put("created_at", "")
                    }
                }
                Log.d("SupabaseSingleton", "Sign-up successful: $user")
                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-in failed: $e")
                return@runBlocking false
            }

        }
    }

    fun getAccessToken(): String {
        try {
            if (this.accessToken!!.isNotEmpty()) {
                Log.d("SupabaseSingleton", "retrieved access token")
                return this.accessToken!!
            }
        } catch (e: Exception) {
            Log.d("SupabaseSingleton", "no access token available, sign-in again")
        }
        return ""
    }


    fun authenticateOtp(otpInput: String, email: String): Boolean {
        return runBlocking {
            try {
                client!!.auth.verifyEmailOtp(
                    type = OtpType.Email.SIGNUP,
                    email = email,
                    token = otpInput
                )

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
    }

    fun getClubById(clubUUID: String): ClubDto? {
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc(
                    "get_club_by_id",
                    buildJsonObject { put("id_arg", clubUUID) })
                Log.d("SupabaseSingleton", "get_club_by_id_rpc, $result")
                val output = result.decodeSingle<ClubDto>()
                Log.d("SupabaseSingleton", "get_club_by_id_rpc_output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    suspend fun getClubByIdAsync(clubUUID: String): ClubDto? {
        return withContext(Dispatchers.IO) {
            try {
                val result = client!!.postgrest.rpc(
                    "get_club_by_id",
                    buildJsonObject { put("id_arg", clubUUID) })
                Log.d("SupabaseSingleton", "get_club_by_id_rpc, $result")
                val output = result.decodeSingle<ClubDto>()
                Log.d("SupabaseSingleton", "get_club_by_id_rpc_output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                null
            }
        }
    }

    fun getNoOfMembersOfClub(clubUUID: String): Int {
        return runBlocking {
            val funcName = "get_no_of_members_of_club"
            val funcParam = buildJsonObject {
                put("club_uuid", clubUUID)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.data.toInt()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking 0
            }
        }

    }

    suspend fun getNoOfMembersOfClubAsync(clubUUID: String): Int {
        return  withContext(Dispatchers.IO) {
            val funcName = "get_no_of_members_of_club"
            val funcParam = buildJsonObject {
                put("club_uuid", clubUUID)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.data.toInt()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                0
            }
        }

    }

    fun getTagById(tagID: String): Tag? {
        return runBlocking {
            try {
                val result = client!!.postgrest.rpc(
                    "get_tag_by_id",
                    buildJsonObject { put("tag_id", tagID) })
                Log.d("SupabaseSingleton", "get_tag_by_id_rpc, $result")
                val output = result.decodeSingle<Tag>()
                Log.d("SupabaseSingleton", "get_tag_by_id_rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    fun getTagsOfClub(clubUUID: String): List<Tag>? {
        return runBlocking {
            val funcName = "get_tags_of_club"
            val funcParam = buildJsonObject {
                put("club_uuid", clubUUID)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<Tag>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertOrUpdateUserToClub(
        userIDArg: String,
        clubIDArg: String,
        roleArg: String
    ): UserToClubDto? {
        return runBlocking {
            val funcName = "insert_or_update_user_to_club"
            val funcParam = buildJsonObject {
                put("user_id_arg", userIDArg)
                put("club_id_arg", clubIDArg)
                put("role_arg", roleArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<UserToClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    fun insertOrUpdateCurrentUserToClub(clubIDArg: String, roleArg: String): UserToClubDto? {
        return insertOrUpdateUserToClub(userIDArg = currentUser!!.id, clubIDArg, roleArg)
    }

    fun removeUserToClub(userIDArg: String, clubIDArg: String): UserToClubDto? {
        return runBlocking {
            val funcName = "remove_user_to_club"
            val funcParam = buildJsonObject {
                put("user_id_arg", userIDArg)
                put("club_id_arg", clubIDArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<UserToClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    fun removeCurrentUserToClub(clubIDArg: String): UserToClubDto? {
        return removeUserToClub(currentUser!!.id, clubIDArg)

    }


    fun searchClubsByLikeName(query: String): List<ClubDto>? {
        return runBlocking {
            val funcName = "search_clubs_by_like_name"
            val funcParam = buildJsonObject {
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    suspend fun searchClubsByLikeNameAsync(query: String): List<ClubDto>? {
        return withContext(Dispatchers.IO) {
            val funcName = "search_clubs_by_like_name"
            val funcParam = buildJsonObject {
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                null
            }
        }

    }

    fun searchClubsByTags(tagIDs: Array<String>): List<ClubDto>? {
        return runBlocking {
            val funcName = "search_clubs_by_tags"
            try {
                val result = client!!.postgrest.rpc(funcName, buildJsonObject {
                    put("tag_ids", Json.encodeToJsonElement(tagIDs))
                })

                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun searchClubs(tagIDs: Array<String>, query: String): List<ClubDto>? {
        return runBlocking {
            val funcName = "search_clubs"
            val funcParam = buildJsonObject {
                put("tag_ids", Json.encodeToJsonElement(tagIDs))
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }


    suspend fun searchClubsAsync(tagIDs: Array<String>, query: String): List<ClubDto>? {
        return withContext(Dispatchers.IO)  {
            val funcName = "search_clubs"
            val funcParam = buildJsonObject {
                put("tag_ids", Json.encodeToJsonElement(tagIDs))
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                null
            }
        }
    }

    fun getRandomClubs(): List<ClubDto>? {
        return runBlocking {
            val funcName = "get_20_random_clubs"
            try {
                val result = client!!.postgrest.rpc(funcName)
                val resultData = result.data
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                Log.d("SupabaseSingleton", resultData)
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun searchTags(query: String): List<Tag>? {
        return runBlocking {
            val funcName = "search_tags"
            val funcParam = buildJsonObject {
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<Tag>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }

    }

    suspend fun searchTagsAsync(query: String): List<Tag>? {
        return withContext(Dispatchers.IO)  {
            val funcName = "search_tags"
            val funcParam = buildJsonObject {
                put("query", query)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<Tag>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                null
            }
        }

    }


    fun checkIsJoined(clubID: String, userID: String): Boolean {
        return runBlocking {
            val funcName = "check_is_user_joined_to_club"
            val funcParam = buildJsonObject {
                put("clubid", clubID)
                put("userid", userID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: Boolean = result.data.toBoolean()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking false
            }
        }
    }

    suspend fun checkIsJoinedAsync(clubID: String, userID: String): Boolean {
        return withContext(Dispatchers.IO) {
            val funcName = "check_is_user_joined_to_club"
            val funcParam = buildJsonObject {
                put("clubid", clubID)
                put("userid", userID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: Boolean = result.data.toBoolean()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                false
            }
        }
    }


    fun getLargestDenominationPast(duration: Duration): String {
        return when {
            duration.toDays() > 60 -> "${duration.toDays() / 30} months ago"
            duration.toDays() >= 30 -> "${duration.toDays() / 30} month ago"
            duration.toDays() > 2 -> "${duration.toDays()} days ago"
            duration.toDays() >= 1 -> "${duration.toDays()} day ago"
            duration.toHours() > 2 -> "${duration.toHours()} hours ago"
            duration.toHours() >= 1 -> "${duration.toHours()} hour ago"
            duration.seconds > 120 -> "${duration.toMinutes()} minutes ago"
            duration.seconds >= 60 -> "${duration.toMinutes()} minute ago"
            else -> "just now"
        }
    }

    fun getLargestDenominationFuture(duration: Duration): String {
        return when {
            duration.toDays() > 60 -> "in ${duration.toDays() / 30} months"
            duration.toDays() >= 30 -> "in ${duration.toDays() / 30} month"
            duration.toDays() > 2 -> "in ${duration.toDays()} days"
            duration.toDays() >= 1 -> "in ${duration.toDays()} day"
            duration.toHours() > 2 -> "in ${duration.toHours()} hours"
            duration.toHours() >= 1 -> "in ${duration.toHours()} hour"
            else -> "starting soon"
        }
    }

    fun getPostsFromClub(clubID: String): List<PostDto> {

        return runBlocking {
            val funcName = "get_all_posts_and_events_from_a_club"
            val funcParam = buildJsonObject {
                put("given_club_id", clubID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: List<PostDto> = result.decodeList<PostDto>()

                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
                for (P in output) {
                    val postDateTime = OffsetDateTime.parse(P.createdAt, firstApiFormat)
                    val duration = Duration.between(postDateTime, currentDateTime)
                    Log.d("test", getLargestDenominationPast(duration))
                    P.createdAt = getLargestDenominationPast(duration)

                    if (P.eventId != null) {
                        val eventDateTime = OffsetDateTime.parse(P.eventTimeStart, firstApiFormat)
                        val durationE = Duration.between(currentDateTime, eventDateTime)
                        P.eventTimeStart = getLargestDenominationFuture(durationE)
                    }

                }


                Log.d("SupabaseSingleton", "$funcName rpc output, $output")

                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking listOf()
            }
        }
    }

   suspend fun getPostsFromClubAsync(clubID: String): List<PostDto> {

        return withContext(Dispatchers.IO)  {
            val funcName = "get_all_posts_and_events_from_a_club"
            val funcParam = buildJsonObject {
                put("given_club_id", clubID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: List<PostDto> = result.decodeList<PostDto>()

                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
                for (P in output) {
                    val postDateTime = OffsetDateTime.parse(P.createdAt, firstApiFormat)
                    val duration = Duration.between(postDateTime, currentDateTime)
                    Log.d("test", getLargestDenominationPast(duration))
                    P.createdAt = getLargestDenominationPast(duration)
                    P.userPfp = getUserPfpAsync(P.userId)

                    if (P.eventId != null) {
                        val eventDateTime = OffsetDateTime.parse(P.eventTimeStart, firstApiFormat)
                        val durationE = Duration.between(currentDateTime, eventDateTime)
                        P.eventTimeStart = getLargestDenominationFuture(durationE)
                    }

                }


                Log.d("SupabaseSingleton", "$funcName rpc output, $output")

                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                listOf()
            }
        }
    }

    fun getEventsFromClub(clubID: String): List<EventDto> {

        return runBlocking {
            val funcName = "get_event_by_club_id"
            val funcParam = buildJsonObject {
                put("club_id_arg", clubID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val temp: List<EventDto> = result.decodeList<EventDto>()
                val output: ArrayList<EventDto> = arrayListOf()

                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)

                for (events in temp) {
                    val eventDateTime = OffsetDateTime.parse(events.timeStart, firstApiFormat)
                    val durationE = Duration.between(currentDateTime, eventDateTime)
                    events.timeStart = getLargestDenominationFuture(durationE)
                    output.add(events)
                }

                Log.d("SupabaseSingleton", "$funcName rpc output, $output")

                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking listOf()
            }
        }
    }

//
//    fun getEventsFromClubOldCode(clubID: String): List<EventDto> {
//
//        return runBlocking {
//            val funcName = "get_all_posts_and_events_from_a_club"
//            val funcParam = buildJsonObject {
//                put("given_club_id", clubID)
//            }
//
//            try {
//                val result = client!!.postgrest.rpc(funcName, funcParam)
//                Log.d("SupabaseSingleton", "$funcName rpc, $result")
//                val temp: List<PostDto> = result.decodeList<PostDto>()
//                val output: ArrayList<EventDto> = arrayListOf()
//
//                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
//                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
//
//                for (P in temp) {
//                    if (P.eventId != null) {
//                        val eventDateTime = OffsetDateTime.parse(P.eventTimeStart, firstApiFormat)
//                        val durationE = Duration.between(currentDateTime, eventDateTime)
//                        P.eventTimeStart = getLargestDenominationFuture(durationE)
//                        var e = fromPostToEvent(P)
//                        output.add(e)
//                    }
//
//                }
//
//                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
//
//                return@runBlocking output
//            } catch (e: Exception) {
//                Log.d("SupabaseSingleton", "Failure, $e")
//                return@runBlocking listOf()
//            }
//        }
//    }

    fun getAllJoinedEvents(): List<EventDto> {
        return runBlocking {
            val userId = currentUser?.id ?: return@runBlocking emptyList()
            val funcName = "get_all_joined_events"
            try {
                val result = client!!.postgrest.rpc(funcName, buildJsonObject {
                    put("user_uuid", Json.encodeToJsonElement(userId))
                })
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking emptyList()
            }
        }
    }

    suspend fun getAllJoinedEventsAsync(): List<EventDto> {
        return withContext(Dispatchers.IO)  {
            val userId = currentUser?.id ?: throw Error("")

            val funcName = "get_all_joined_events"
            try {
                val result = client!!.postgrest.rpc(funcName, buildJsonObject {
                    put("user_uuid", Json.encodeToJsonElement(userId))
                })
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                emptyList()
            }
        }
    }


    fun getAllJoinedClubs(): List<ClubDto> {
        return runBlocking {
            val userId = currentUser?.id ?: return@runBlocking emptyList()
            val funcName = "get_all_joined_clubs"
            try {
                val result = client!!.postgrest.rpc(funcName, buildJsonObject {
                    put("given_user_id", Json.encodeToJsonElement(userId))
                })
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking emptyList()
            }
        }
    }


    suspend fun getAllJoinedClubsAsync(): List<ClubDto> {
        return withContext(Dispatchers.IO) {
            val userId = currentUser?.id ?: throw Error()
            val funcName = "get_all_joined_clubs"
            try {
                val result = client!!.postgrest.rpc(funcName, buildJsonObject {
                    put("given_user_id", Json.encodeToJsonElement(userId))
                })
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                emptyList()
            }
        }
    }

    suspend fun getPostsFromHomeAsync(): List<PostDto> {

        Log.d("getPOSTfromHome", UserSingleton.userID)

        return withContext(Dispatchers.IO) {
            val funcName = "get_all_posts_and_unjoined_events_from_all_joined_clubs"
            val funcParam = buildJsonObject {
                put("user_uuid", UserSingleton.userID)
            }

            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output: List<PostDto> = result.decodeList<PostDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
                for (P in output) {
                    val postDateTime = OffsetDateTime.parse(P.createdAt, firstApiFormat)
                    val duration = Duration.between(postDateTime, currentDateTime)
                    Log.d("test", getLargestDenominationPast(duration))
                    P.createdAt = getLargestDenominationPast(duration)
//                    P.clubPfp = getClubBannerAsync(P.clubId)
                    //changed to user
                    P.userPfp = getUserPfpAsync(P.userId)
                    Log.d("dwadw",   P.userPfp.toString())

                    if (P.eventId != null) {
                        val eventDateTime = OffsetDateTime.parse(P.eventTimeStart, firstApiFormat)
                        val durationE = Duration.between(currentDateTime, eventDateTime)
                        P.eventTimeStart = getLargestDenominationFuture(durationE)
                    }
                }
                Log.d("home posts",   output.toString())
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                listOf()
            }
        }
    }


    //valid bucketNames: "post_images", "club_images"
    fun uploadImageToBucket(imageFile: File, bucketName: String, quality: Int = 80, filepathArg: String? = null, register: Boolean = false): String? {
        return runBlocking {

            // Step 1: Decode the image file to a Bitmap
            val originalBitmap: Bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            // Step 2: Prepare for compression
            val compressedImageOutput = ByteArrayOutputStream()
            // Compress the bitmap to JPEG format
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, compressedImageOutput)

            // Step 3: Upload the compressed image
            val filePath = filepathArg ?: "images/${imageFile.nameWithoutExtension}.jpg" // Save as JPEG
            try {
                val uploadResult = client!!.storage.from(bucketName)
                    .upload(filePath, compressedImageOutput.toByteArray())
                Log.d("SupabaseSingleton", "Upload result: $uploadResult")
                return@runBlocking uploadResult.key
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Error uploading image: $e")
                return@runBlocking null
            }
        }
    }

    fun getImageUrl(imageName: String, bucketName: String): Deferred<String?> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                // Construct the image URL based on your bucket URL structure
                val imagePath = "$imageName.jpg"
                // Assuming you have a base URL for your Supabase storage
                val url = client!!.storage.from(bucketName).publicUrl(imagePath)
                Log.d("SupabaseSingleton", url)
                url
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Error constructing image URL: $e")
                null
            }
        }
    }

    fun insertOrUpdatePost(
        idArg: String,
        userIdArg: String,
        clubIdArg: String,
        titleArg: String,
        bodyArg: String,
        mediaArg: String,
    ): PostDto? {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
        val formattedTime = currentDateTime.format(firstApiFormat)
        return runBlocking {
            val funcName = "insert_or_update_post"
            val funcParam = buildJsonObject {
                put("body_arg", bodyArg)
                put("club_id_arg", clubIdArg)
                put("created_at_arg", formattedTime)
                put("id_arg", idArg)
                put("media_arg", mediaArg)
                put("title_arg", titleArg)
                put("user_id_arg", userIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<PostDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertOrUpdateEvent(
        idArg: String,
        clubIdArg: String,
        titleArg: String,
        bodyArg: String,
        timeStartArg: Calendar,
        durationArg: Long,
        locationArg: String,
    ): EventDto? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val timeStartStr = formatter.format(timeStartArg.time)
        val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
        return runBlocking {
            val funcName = "insert_or_update_event"
            val funcParam = buildJsonObject {
                put("body_arg", bodyArg)
                put("club_id_arg", clubIdArg)
                put("created_at_arg", currentDateTime.toString())
                put("id_arg", idArg)
                put("time_start_arg", timeStartStr)
                put("title_arg", titleArg)
                put("duration_arg", durationArg)
                put("location_arg", locationArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun removePost(
        idArg: String,
    ): PostDto? {
        return runBlocking {
            val funcName = "remove_post"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<PostDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun removeEvent(
        idArg: String,
    ): EventDto? {
        return runBlocking {
            val funcName = "remove_event"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun getPostById(
        idArg: String,
    ): PostDto? {
        return runBlocking {
            val funcName = "get_post_or_event_post_by_id"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<PostDto>()

                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
                val postDateTime = OffsetDateTime.parse(output.createdAt, firstApiFormat)
                val duration = Duration.between(postDateTime, currentDateTime)
                output.createdAt = getLargestDenominationPast(duration)
                if (output.eventId != null) {
                    val eventDateTime = OffsetDateTime.parse(output.eventTimeStart, firstApiFormat)
                    val durationE = Duration.between(currentDateTime, eventDateTime)
                    output.eventTimeStart = getLargestDenominationFuture(durationE)
                }
                output.userPfp = getUserPfpAsync(output.userId)

                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    suspend fun getPostByIdAsync(
        idArg: String,
    ): PostDto? {
        return withContext(Dispatchers.IO) {
            val funcName = "get_post_or_event_post_by_id"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<PostDto>()

                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)
                val postDateTime = OffsetDateTime.parse(output.createdAt, firstApiFormat)
                val duration = Duration.between(postDateTime, currentDateTime)
                output.createdAt = getLargestDenominationPast(duration)

                if (output.eventId != null) {
                    val eventDateTime = OffsetDateTime.parse(output.eventTimeStart, firstApiFormat)
                    val durationE = Duration.between(currentDateTime, eventDateTime)
                    output.eventTimeStart = getLargestDenominationFuture(durationE)
                }

                output.displayName = getDisplayNameAsync(output.userId)
                output.clubName = getClubNameAsync(output.clubId)
                output.userPfp = getUserPfpAsync(output.userId)

                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                null
            }
        }
    }

    fun getEventById(
        idArg: String,
    ): EventDto? {
        return runBlocking {
            val funcName = "get_event_by_id"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertEventToPost(
        eventIdArg: String,
        postIdArg: String,
    ): EventToPost? {
        return runBlocking {
            val funcName = "insert_event_to_post"
            val funcParam = buildJsonObject {
                put("event_id_arg", eventIdArg)
                put("post_id_arg", postIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventToPost>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun removeEventToPost(
        eventIdArg: String,
        postIdArg: String,
    ): EventToPost? {
        return runBlocking {
            val funcName = "remove_event_to_post"
            val funcParam = buildJsonObject {
                put("event_id_arg", eventIdArg)
                put("post_id_arg", postIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventToPost>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }


    }

    fun getEventToPostByPostId(
        postIdArg:  String,
    ): EventToPost?{
        return runBlocking {
            val funcName = "get_event_to_post_by_post_id"
            val funcParam = buildJsonObject {
                put("post_id_arg", postIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventToPost>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun getEventToPostsByEventId(
        eventIdArg: String,
    ): List<EventToPost>?{
        return runBlocking {
            val funcName = "get_event_to_posts_by_event_id"
            val funcParam = buildJsonObject {
                put("event_id_arg", eventIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<EventToPost>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception){
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun queryEvents(
        limitRow : Int,
        offsetRow : Int,
        orderByTimeStart : Int,
        orderByTitle : Int,
        byTitle : Boolean,
        titleArg : String,
        byClub : Boolean,
        clubIdArg : String,
        byUserToEvent : Boolean,
        userIdArg : String
    ): List<EventDto>? {
        return runBlocking {
            val funcName = "query_events"
            val funcParam = buildJsonObject {
                put("limit_row", limitRow)
                put("offset_row", offsetRow)
                put("order_by_time_start", orderByTimeStart)
                put("order_by_title", orderByTitle)
                put("by_title", byTitle)
                put("title_arg", titleArg)
                put("by_club", byClub)
                put("club_id_arg", clubIdArg)
                put("by_user_to_event", byUserToEvent)
                put("user_id_arg", userIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertOrUpdateClub(
        idArg: String,
        nameArg: String,
        descriptionArg: String,
        imageArg: String
    ): ClubDto? {
        return runBlocking {
            val currentDateTime = OffsetDateTime.now(ZoneOffset.UTC)

            val funcName = "insert_or_update_club"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
                put("name_arg", nameArg)
                put("created_at_arg", currentDateTime.toString())
                put("description_arg", descriptionArg)
                put("image_arg", imageArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<ClubDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun removeClub(
        idArg: String,
    ): EventDto? {
        return runBlocking {
            val funcName = "remove_club"
            val funcParam = buildJsonObject {
                put("id_arg", idArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<EventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertOrUpdateUserToEvent(
        userIdArg : String,
        eventIdArg : String,
    ): UserToEventDto? {
        return runBlocking {

            val funcName = "insert_or_update_user_to_event"
            val funcParam = buildJsonObject {
                put("user_id_arg", userIdArg)
                put("event_id_arg", eventIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<UserToEventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun insertOrUpdateCurrentUserToEvent(eventIdArg : String): UserToEventDto? {
        return insertOrUpdateUserToEvent(currentUser!!.id, eventIdArg)
    }

    fun removeUserFromEvent(
        userIdArg : String,
        eventIdArg : String,
    ): UserToEventDto? {
        return runBlocking {

            val funcName = "remove_user_to_event"
            val funcParam = buildJsonObject {
                put("user_id_arg", userIdArg)
                put("event_id_arg", eventIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeSingle<UserToEventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun removeCurrentUserFromEvent(eventIdArg : String): UserToEventDto? {
        return removeUserFromEvent(currentUser!!.id, eventIdArg)
    }


    fun getUserToEventByIds( // This is a plural function and return list of Dto despite the function is singularly named
        byUserId : Boolean,
        userIdArg : String,
        byEventId : Boolean,
        eventIdArg : String,
    ): List<UserToEventDto>? {
        return runBlocking {

            val funcName = "get_user_to_event_by_ids"
            val funcParam = buildJsonObject {
                put("by_user_id", byUserId)
                put("user_id_arg", userIdArg)
                put("by_event_id", byEventId)
                put("event_id_arg", eventIdArg)
            }
            try {
                val result = client!!.postgrest.rpc(funcName, funcParam)
                Log.d("SupabaseSingleton", "$funcName rpc, $result")
                val output = result.decodeList<UserToEventDto>()
                Log.d("SupabaseSingleton", "$funcName rpc output, $output")
                return@runBlocking output
            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Failure, $e")
                return@runBlocking null
            }
        }
    }

    fun getCurrentUserToEventByIds(eventIdArg : String): List<UserToEventDto>? {
        return getUserToEventByIds(true, currentUser!!.id, true, eventIdArg)
    }


}