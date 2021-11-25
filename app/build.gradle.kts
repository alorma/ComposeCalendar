plugins {
  id("com.android.application")
  id("kotlin-android")
}

android {
  compileSdk = 31

  defaultConfig {
    applicationId = "com.alorma.calendar"
    minSdk = 23
    targetSdk =  31
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled =  false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
  }
  compileOptions {
    sourceCompatibility =  JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.1.0-beta03"
  }
  packagingOptions {
    resources {
      resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }
}

dependencies {

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.activity:activity-compose:1.4.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

  implementation("io.insert-koin:koin-core:3.1.3")
  implementation("io.insert-koin:koin-android:3.1.3")
  implementation("io.insert-koin:koin-androidx-compose:3.1.3")

  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

  implementation("androidx.compose.ui:ui:1.1.0-beta03")
  implementation("androidx.compose.ui:ui-tooling-preview:1.1.0-beta03")
  implementation("androidx.compose.material3:material3:1.0.0-alpha01")
  implementation("androidx.compose.material:material-icons-extended:1.1.0-beta03")

  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-beta03")

  debugImplementation("androidx.compose.ui:ui-tooling:1.1.0-beta03")
}