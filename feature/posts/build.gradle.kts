@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.plugin)
    alias(libs.plugins.navigationSafeArgs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.parcelize)
}

android {
    compileSdk = ConfigData.compileSdk
    namespace = NamceSpace.Feature.posts

    defaultConfig {
        minSdk = ConfigData.minSdk
    }

    buildTypes {
        getByName(BuildTypeDebug.name) {
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
        }

        getByName(BuildTypeRelease.name) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlinJvmTarget.get()
    }

    hilt {
        enableAggregatingTask = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(Modules.Core.network))
    implementation(project(Modules.Core.utils))
    implementation(project(Modules.Core.ui))


    implementation(libs.bundles.ui)
    implementation(libs.bundles.navigationComponent)
    implementation(libs.bundles.room)

    implementation(libs.hilt)
    kapt(libs.hiltDaggerCompiler)



    testImplementation(libs.bundles.unitTest)
}