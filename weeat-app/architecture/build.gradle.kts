plugins {
    id("com.android.library")
}

android {
    namespace = "com.bbyy.architecture"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures{
        dataBinding=true
    }
}

dependencies {
    // 本地库文件
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    //解决DataBinding冲突-Android Studio版本最新更新Gradle配置与Kotlin包冲突问题
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    // UI框架
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    // 测试框架
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // 常用基础组件
    api("androidx.appcompat:appcompat:1.6.1")
    api("org.jetbrains:annotations:24.0.1")
    api("androidx.navigation:navigation-runtime:2.7.7")
    api("com.google.android.material:material:1.9.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.recyclerview:recyclerview:1.3.1")

    // 常用架构组件，已按功能提取分割为多个独立库，可按需选配
    api("com.github.KunMinX:MVI-Dispatcher:7.6.0")
    api("com.github.KunMinX:UnPeek-LiveData:7.8.0")
    api("com.github.KunMinX.Strict-DataBinding:binding_state:6.2.0")
    api("com.github.KunMinX.Strict-DataBinding:strict_databinding:6.2.0")
    api("com.github.KunMinX.Strict-DataBinding:binding_recyclerview:6.2.0")

    //Navigation相关依赖
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.navigation:navigation-runtime:2.7.7")
    implementation("androidx.navigation:navigation-fragment:2.7.7")

    // 常用数据、媒体组件
    api("com.github.bumptech.glide:glide:4.16.0")
    api("com.google.code.gson:gson:2.10.1")
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.11.0")
    api("com.squareup.okhttp3:okhttp:4.11.0")
    api("io.reactivex.rxjava2:rxandroid:2.1.1")
    api("io.reactivex.rxjava2:rxjava:2.2.21")
}