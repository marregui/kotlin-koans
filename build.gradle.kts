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
    implementation(kotlin("stdlib-jdk8"))
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.kotlinReflect)
    implementation(Libs.sparkJava)
    implementation(Libs.gson)
    implementation(Libs.slf4jApi)
    implementation(Libs.slf4jLog4jBinding)

    testImplementation(Libs.junitJupiter)
    testImplementation(Libs.junitJupiterApi)
    testImplementation(Libs.junitJupiterEngine)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mokitoCore)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

application {
    mainClass.set("kotlinkoans.GameOfLife")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.8"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
