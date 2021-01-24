val ktor_version: String by project

dependencies {
    // implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
}

