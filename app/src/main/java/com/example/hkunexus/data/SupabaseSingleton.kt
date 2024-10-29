package com.example.hkunexus.data

import android.util.Log
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



    public fun authenticateOtp(otpInput: String):Boolean{
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

    public fun getClubByClubId(){}

}