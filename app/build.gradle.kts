plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pppbapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pppbapp"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // TAMBAHKAN DEPENDENCIES FIREBASE INI:

    // 1. Firebase BoM (Pakai versi terbaru yang kamu temukan)
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    // 2. Firebase Auth (TANPA menulis nomor versi, karena sudah diatur oleh BoM)
    implementation("com.google.firebase:firebase-auth")
    // 3. Google Play Services (Gunakan versi terbaru 21.4.0)
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    // Opsional: Analytics
    implementation("com.google.firebase:firebase-analytics")
    // Library untuk Splash Screen kustom
    implementation("androidx.core:core-splashscreen:1.2.0")
}