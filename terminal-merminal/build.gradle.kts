@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // 【修改点 1】新增：Kotlin 2.0 必须显式引入 Compose 编译器插件
    // 注意：如果你在根目录 build.gradle 定义了版本，这里只需 id(...)
    // 如果没有，可能需要加上 version "2.0.0" (要与你的 Kotlin 版本一致)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "cn.mucute.merinal"
    compileSdk = 34
    
    // 如果你没有特定需求，建议删除 ndkVersion 这一行，使用 IDE 默认安装的即可
     ndkVersion = "28.2.13676358" 

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                // 【修改点 2】: 再次提醒，不要在这里写 cppFlags += "-std=c++11"
                // 应该让 CMakeLists.txt 控制标准 (C++17)
            }
        }

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }
        
    externalNativeBuild {
        cmake {
            path = file("src/main/jni/CMakeLists.txt")
            version = "3.25.1"
        }
    }

    buildTypes {
        getByName("release") {
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
	
	   kotlinOptions {
        jvmTarget = "1.8"
    }
    
    buildFeatures {
        compose = true
    }

    // 【修改点 3】: 删除 composeOptions 代码块
    // Kotlin 2.0 不再需要在这里指定 kotlinCompilerExtensionVersion
    /* composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" 
    }
    */
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha12")
}
