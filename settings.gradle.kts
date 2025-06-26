pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://esri.jfrog.io/artifactory/arcgis")
    }
}

rootProject.name = "arcgis-android-tile-loading-test"
include(":app")
