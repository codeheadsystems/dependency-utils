plugins {
    `maven-publish`
    signing
}

// Publication configuration for Maven Central via nmcp plugin
// The nmcp settings plugin (configured in settings.gradle.kts) handles the actual
// upload to Central Portal. This convention plugin defines the publication metadata.
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            // Set artifact coordinates
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            pom {
                // Module-specific name and description
                // Override these in individual module build.gradle.kts if needed
                name.set(project.findProperty("pomName")?.toString() ?: project.name)
                description.set(project.findProperty("description")?.toString()
                    ?: "Dependency utilities for Java projects")
                url.set("https://github.com/codeheadsystems/dependency-utils")

                licenses {
                    license {
                        name.set("MIT No Attribution License")
                        url.set("https://opensource.org/license/mit-0")
                    }
                }

                developers {
                    developer {
                        id.set("wolpert")
                        name.set("Ned Wolpert")
                        email.set("ned.wolpert@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/codeheadsystems/dependency-utils.git")
                    developerConnection.set("scm:git:ssh://git@github.com:codeheadsystems/dependency-utils.git")
                    url.set("https://github.com/codeheadsystems/dependency-utils")
                }
            }
        }
    }
    // Note: No repositories block needed - nmcp plugin handles upload to Central Portal
}

signing {
    // Only sign if credentials are available (not required for local builds)
    val signingRequired = project.hasProperty("signing.gnupg.keyName")
        || System.getenv("GPG_KEY_ID") != null

    isRequired = signingRequired && !version.toString().endsWith("SNAPSHOT")

    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

// Task to verify publication configuration
tasks.register("verifyPublishConfig") {
    doLast {
        println("Group: ${project.group}")
        println("Artifact: ${project.name}")
        println("Version: ${project.version}")
        println("Is SNAPSHOT: ${version.toString().endsWith("SNAPSHOT")}")
    }
}
