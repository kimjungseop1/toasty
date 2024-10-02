plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.syncrown.arpang"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.syncrown.arpang"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.androidx.annotation)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.fragment.ktx)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)

    // kakao
    implementation(libs.v2.user)
    implementation(libs.v2.share)

    // naver
    implementation(libs.oauth)

    // google
    implementation(libs.google.services)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)

    // facebook
    implementation(libs.facebook.android.sdk)

    // retrofit2 네트워크 api
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // Logging Network Calls 네트워크 로깅
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    // GSON
    implementation(libs.gson)
    // 이미지 로드
    implementation(libs.glide)
    implementation(libs.kotlinx.coroutines.android)

    // AR
    implementation(libs.core)
    implementation(libs.sceneform.ux)
    implementation(libs.sceneform.core)

    // exoplayer
    implementation(libs.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.transformer)

    // spinner
    implementation(libs.nice.spinner)

    // image cropper
    implementation(libs.ucrop)

    // dot indicator
    implementation(libs.dotsindicator)

    // noinspection RiskyLibrary
    implementation(libs.play.core)

    // crop image
    implementation(libs.android.image.cropper)

    // dynamically add view
    implementation(libs.flexbox)

}