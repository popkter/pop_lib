plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.popkter.poplib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin{
        jvmToolchain(8)
    }

    viewBinding.enable = true
}


dependencies {


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")


    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("com.scwang.smart:refresh-layout-kernel:2.0.3")      //核心必须依赖
    implementation("com.scwang.smart:refresh-header-classics:2.0.3")    //经典刷新头
    /*    implementation("com.scwang.smart:refresh-header-radar:2.0.3")       //雷达刷新头
        implementation("com.scwang.smart:refresh-header-falsify:2.0.3")     //虚拟刷新头
        implementation("com.scwang.smart:refresh-header-material:2.0.3")    //谷歌刷新头
        implementation("com.scwang.smart:refresh-header-two-level:2.0.3")   //二级刷新头
        implementation("com.scwang.smart:refresh-footer-ball:2.0.3")        //球脉冲加载*/
    implementation("com.scwang.smart:refresh-footer-classics:2.0.3")    //经典加载
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.popkter.poplib"
                artifactId = "poplib"
                version = "1.0"
            }
        }
    }
}
