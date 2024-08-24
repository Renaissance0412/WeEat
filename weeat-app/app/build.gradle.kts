plugins {
    id("com.android.application")
}

android {
    namespace = "com.bbyy.weeat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bbyy.weeat"
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

    //chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

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

    //eventbus通信
    implementation("org.greenrobot:eventbus:3.3.1")

    //解决DataBinding冲突-Android Studio版本最新更新Gradle配置与Kotlin包冲突问题
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    //noinspection GradleCompatible
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Navigation相关依赖
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.navigation:navigation-runtime:2.7.7")
    implementation("androidx.navigation:navigation-fragment:2.7.7")

    implementation("org.slf4j:slf4j-android:1.7.36")
    implementation("com.sothree.slidinguppanel:library:3.4.0")
    implementation("com.github.KunMinX:Jetpack-MusicPlayer:5.2.0")
    implementation("com.github.KunMinX.KeyValueX:keyvalue:3.7.0-beta")
    annotationProcessor("com.github.KunMinX.KeyValueX:keyvalue-compiler:3.7.0-beta")

    //检查内存泄漏
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")
    releaseImplementation("com.squareup.leakcanary:leakcanary-android-no-op:2.13")

    //Room数据库框架
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    //jbox2d物理引擎，处理自定义气泡view
    implementation(group = "org.jbox2d", name = "jbox2d-serialization", version = "1.1.0")
    implementation(group = "org.jbox2d", name = "jbox2d-library", version = "2.2.1.1")

    //viewpager
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
}