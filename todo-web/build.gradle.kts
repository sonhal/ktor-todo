val ktor_version: String by project
val koin_version: String by project


plugins {
    application
}

group = "no.sonhal"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-mustache:$ktor_version")
    implementation("org.koin:koin-ktor:$koin_version")

    // Authenication
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("com.auth0:java-jwt:3.12.1")
}

