import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.1"
    namespace = "app.revanced.integrations"

    defaultConfig {
        applicationId = "app.revanced.integrations"
        minSdk = 26
        targetSdk = 33
        multiDexEnabled = false
        versionName = project.version as String
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    compileOnly(project(mapOf("path" to ":dummy")))
    compileOnly("androidx.annotation:annotation:1.5.0")
}
