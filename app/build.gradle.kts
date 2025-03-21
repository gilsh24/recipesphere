import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.recipesphere"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recipesphere"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val localPropertiesFile = rootProject.file("local.properties")
        val localProperties = Properties().apply {
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }

        buildConfigField("String", "CLOUD_NAME", "\"${localProperties.getProperty("cloudinary_cloud_name", "")}\"")
        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("cloudinary_api_key", "")}\"")
        buildConfigField("String", "API_SECRET", "\"${localProperties.getProperty("cloudinary_api_secret", "")}\"")
        buildConfigField("String", "EDAMAM_APP_ID", "\"${localProperties.getProperty("edamam_app_id", "")}\"")
        buildConfigField("String", "EDAMAM_APP_KEY", "\"${localProperties.getProperty("edamam_app_key", "")}\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.cloudinary.android)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.loggingInterceptor)
    implementation(libs.picasso)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.gson)
}