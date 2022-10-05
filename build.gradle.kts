plugins {
    java
    application
    id("org.sourcegrade.style") version "1.3.0"
    id("org.sourcegrade.jagr-gradle") version "0.6.0-SNAPSHOT"
}

version = "0.1.0-SNAPSHOT"

jagr {
    assignmentId.set("h12")
    submissions {
        create("main") {
            studentId.set("ab12cdef")
            firstName.set("sol_first")
            lastName.set("sol_last")
        }
    }
    graders {
        create("grader") {
            graderName.set("FOP-2223-H12")
            rubricProviderName.set("h12.H12_RubricProvider")
        }
    }
}

dependencies {
    implementation("org.tudalgo:algoutils-tutor:0.2.0-SNAPSHOT")
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

application {
    mainClass.set("h12.Main")
}

tasks {
    val runDir = File("build/run")
    named<JavaExec>("run") {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }


    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
