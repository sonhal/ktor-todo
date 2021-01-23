val ktor_version: String by project


plugins {
    application
}

group = "no.sonhal"
version = "0.0.1"


dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
}
