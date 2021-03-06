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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
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
        maven { url = uri("https://dl.bintray.com/spekframework/spek-dev") }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("io.ktor:ktor-server-cio:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-host-common:$ktor_version")

        implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.12.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")

        // .env file support lib
        implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

        testImplementation("org.amshove.kluent:kluent:$kluent_version")
        testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
        testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version")

    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

}


project(":todo-shared"){

}

project(":dataaccess-service"){
    dependencies {
        implementation(project(":todo-shared"))
        implementation(project(":repository"))
    }
}

project(":todo-restapi"){
    dependencies {
        implementation(project(":todo-shared"))
        implementation(project(":dataaccess-service"))
        implementation(project(":repository"))
    }
}

project(":todo-web"){
    dependencies {
        implementation(project(":todo-shared"))
        implementation(project(":todo-service"))
    }
}
project(":todo-service"){
    dependencies {
        implementation(project(":todo-shared"))
    }
}
