plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "app.revanced.integrations"
    compileSdk = 33

    buildFeatures {
        buildConfig = true
    }

    applicationVariants.all {
        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")

        outputs.all {
            this as com.android.build.gradle.internal.api.ApkVariantOutputImpl

            outputFileName = "${rootProject.name}-$versionName.apk"
        }
    }

    defaultConfig {
        applicationId = "app.revanced.integrations"
        minSdk = 24
        targetSdk = 33
        multiDexEnabled = false
        versionName = project.version as String
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.appcompat)
    compileOnly(libs.annotation)
    compileOnly(libs.okhttp)
    compileOnly(libs.retrofit)

    compileOnly(project(":dummy"))
}

tasks {
    // Required to run tasks because Gradle semantic-release plugin runs the publish task.
    // Tracking: https://github.com/KengoTODA/gradle-semantic-release-plugin/issues/435
    register("publish") {
        group = "publishing"
        description = "Publishes all publications produced by this project."

        dependsOn(build)
    }
}
