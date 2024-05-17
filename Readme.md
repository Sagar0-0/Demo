# Dependencies

In app level build.gradle

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)

    kotlin("plugin.serialization") version "1.9.24" // Plugin

In libs.version.toml
```
kotlinxSerializationJson = "1.6.3"
navigationCompose = "2.8.0-beta01"



kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerializationJson" }
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
```
