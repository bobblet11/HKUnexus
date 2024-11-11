import org.apache.tools.ant.util.JavaEnvUtils.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

}

android {
    namespace = "com.example.hkunexus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hkunexus"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.annotation)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.0"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation(libs.postgrest.kt)
    implementation(libs.auth.kt)
    implementation(libs.realtime.kt)
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation(libs.ktor.client.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(libs.play.services.maps) // Check for the latest version
    implementation(libs.glide)
    annotationProcessor(libs.compiler) // For Java
    implementation(libs.osmdroid.android) // Check for the latest version


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}