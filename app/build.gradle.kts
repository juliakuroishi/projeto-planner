plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projetoplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projetoplanner"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX UI Components
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout) // Você tinha duplicado esta linha, removi a segunda.
    implementation ("androidx.cardview:cardview:1.0.0")
    // Navigation Component
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Room Database - Usando versões diretas
    implementation("androidx.room:room-common:2.7.1")
    implementation("androidx.room:room-runtime:2.7.1")
    annotationProcessor("androidx.room:room-compiler:2.7.1") // Esta é a chave para o problema do Room

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}