// settings.gradle.kts

// --- Repository configuration for Gradle plugins ---
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

// --- Repository configuration for project dependencies ---
dependencyResolutionManagement {
    // This setting prevents Gradle from using repositories defined in individual build.gradle files
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        
        gradlePluginPortal()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
        maven { url = uri("https://jitpack.io") }
    }
}

// --- Project Definition ---
rootProject.name = "AcTermux"

// Include all submodules
include(":app")
include(":terminal-emulator")
include(":terminal-view")
include(":termux-shared")
include(":termux-app")
include(":terminal-merminal")