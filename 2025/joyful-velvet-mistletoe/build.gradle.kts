plugins {
    id("java")
    kotlin("jvm") version "2.1.20"
}

repositories {
    mavenCentral()
}

dependencies {
    val lombok = "org.projectlombok:lombok:1.18.42"
    annotationProcessor(lombok)
    compileOnly(lombok)

    implementation("io.vavr:vavr:0.10.7")
    implementation("org.apache.commons:commons-geometry-core:1.0")
    // https://www.baeldung.com/jgrapht
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}