plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.cinemamanagerapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cinemamanagerapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Đã sửa
        targetCompatibility = JavaVersion.VERSION_1_8 // Đã sửa
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.database)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Thư viện hình ảnh hình tròn
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // Thư viện trình chiếu hình ảnh
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    // Thư viện ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ViewModel và LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Retrofit và OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Thư viện Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // Đã sửa
    implementation("jp.wasabeef:glide-transformations:4.3.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.libraries.places:places:4.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // ViewPager2
    implementation("androidx.fragment:fragment-ktx:1.8.4")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.12.0")

    // Thư viện OTP
    implementation("com.github.aabhasr1:OtpView:v1.1.2-ktx")

    // Stripe
    implementation("com.stripe:stripe-android:20.52.1")

    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3") // Kiểm tra phiên bản mới nhất
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3") // Kiểm tra phiên bản mới nhất

    implementation("com.github.bumptech.glide:glide:4.16.0")

    
}
