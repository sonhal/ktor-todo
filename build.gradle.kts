val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val spek_version: String by project
val jackson_version: String by project
val kluent_version: String by project


buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.20")
    }
}

plugins {
    java
}

allprojects {
    group = "no.sonhal"
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        jcenter()
        mavenLocal()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("io.ktor:ktor-server-cio:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-host-common:$ktor_version")
        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
}

project(":todo-shared"){

}

project(":todo-restapi"){
    dependencies {
        implementation(project(":todo-shared"))
    }
}