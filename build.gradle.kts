buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:9.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.7")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52.1")
    }
}