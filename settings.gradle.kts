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
        // Agrega el repositorio de JitPack aquí
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "SENA Monitoreo"
include(":app")
 