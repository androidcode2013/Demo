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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Demo"
include(":app")
include(":leakcanarydemo")
include(":opengldemo")
include(":Particles")
include(":Skybox")
include(":Heightmap")
include(":BlendDemo")
include(":openglLibrary")
include(":vbodemo")
include(":vaodemo")
include(":ebodemo")
include(":fbodemo")
include(":scissordemo")
include(":stencildemo")
include(":skyboxdemo")
include(":glsldemo")
include(":msaademo")
include(":RoundRect")
include(":drawtext")
include(":renderyuv")
include(":workthread")
