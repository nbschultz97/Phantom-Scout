plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.vanta.phantomscout"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vanta.phantomscout"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")
}
