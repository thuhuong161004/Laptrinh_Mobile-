plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.laptrinh_mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.laptrinh_mobile"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/*.md"
        }
    }
}

dependencies {
    // Mail và activation
    implementation(libs.android.mail)
    implementation(libs.android.activation)

    // Java OTP (nếu cần)
    implementation("com.eatthepath:java-otp:0.4.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-database") // Thêm dòng này để sử dụng Realtime Database
    // Comment tạm thời nếu lỗi: implementation("com.google.firebase:firebase-functions")

    // AndroidX và Material
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Ép buộc phiên bản để tránh xung đột
    configurations.all {
        resolutionStrategy {
            force("org.apache.httpcomponents:httpclient:4.5.14")
            force("org.apache.httpcomponents:httpcore:4.4.16")
        }
    }
}