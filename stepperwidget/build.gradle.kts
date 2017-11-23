plugins {
    id("com.android.library") version "3.0.1"
}

android {
    compileSdkVersion(27)

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(27)
    }
}

dependencies {
    api("com.android.support:appcompat-v7:27.0.1")
}
