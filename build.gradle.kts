import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion by System.getProperties()
    kotlin("jvm") version "$kotlinVersion"
    id("application")
    id("idea")
}

group = "marregui.kotlinkoans"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kotlinCoroutinesVersion by System.getProperties()
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    testImplementation(kotlin("test-junit5"))
    implementation(kotlin("stdlib-jdk8"))
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
