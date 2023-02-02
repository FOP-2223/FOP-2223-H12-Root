dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "FOP-2223-H12-Root"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        gradlePluginPortal()
    }
}
