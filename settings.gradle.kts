pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral() // ¡Asegúrate de que esta línea esté presente!
    }
}

rootProject.name = "SENA Monitoreo"
include(":app")
 