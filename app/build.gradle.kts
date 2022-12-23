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
        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(FileInputStream(rootProject.file("local.properties")))
        }
        properties.load(FileInputStream(rootProject.file("gradle.properties")))

        applicationId = "app.revanced.integrations"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = properties.getProperty("version", "")
        multiDexEnabled = false
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
