import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id("application")
    id("idea")
}

group = "marregui.kotlinkoans"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Libs.kotlinCoroutines)
    implementation(kotlin("stdlib-jdk8"))
    implementation(Libs.logging)
    testImplementation(Libs.junitJupiter)
    testImplementation(Libs.junitJupiterApi)
    testImplementation(Libs.junitJupiterEngine)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mokitoCore)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "13"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "13"
}

application {
    mainClass.set("kotlinkoans.Snake")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.7"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
