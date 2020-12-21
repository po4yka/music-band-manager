import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.diffplug.spotless") version "5.8.2"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    options.encoding = "UTF-8"
}

val mainClassName = "com.herokuapp.musicband.ApplicationKt"

group = "me.po4yka"
version = "1.0-SNAPSHOT"

sourceSets.getByName("main") {
    java.srcDir("src/main/java")
    java.srcDir("src/main/kotlin")
    resources.srcDir("resources")
}

sourceSets.getByName("test") {
    java.srcDir("test/java")
    java.srcDir("test/kotlin")
    resources.srcDir("testresources")
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kodein-framework/Kodein-DI") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version") // default kotlin
    implementation("io.ktor:ktor-server-netty:$ktor_version") // netty server for ktor
    implementation("ch.qos.logback:logback-classic:$logback_version") // logging
    implementation("io.ktor:ktor-server-core:$ktor_version") // ktor server
    implementation("io.ktor:ktor-gson:$ktor_version") // gson for ktor
    implementation("io.ktor:ktor-html-builder:$ktor_version") // html render
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.0.0") // kodein for ktor
    implementation("com.github.jengelman.gradle.plugins:shadow:6.1.0")

    // Exposed ORM library
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // JDBC Connection Pool
    implementation("com.zaxxer:HikariCP:3.4.5")

    // JDBC Connector for PostgreSQL
    implementation("org.postgresql:postgresql:42.2.1")

    // Spotless
    implementation("com.diffplug.spotless:spotless-plugin-gradle:5.8.2")

    // Klaxon - json parse
    implementation("com.beust:klaxon:5.0.1")
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "com.herokuapp.musicband.ApplicationKt"
        archiveClassifier.set("all")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "com.herokuapp.musicband.ApplicationKt"
}

tasks.create("stage") {
    dependsOn("installDist")
}

task<Jar>("dist") {
    manifest {
        attributes("Main-Class" to mainClassName)
    }
}

fun Project.getKtlintConfiguration(): Configuration {
    return configurations.findByName("ktlint") ?: configurations.create("ktlint") {
        val dependency = project.dependencies.create("com.pinterest:ktlint:0.36.0")
        dependencies.add(dependency)
    }
}

tasks.register<JavaExec>(name = "ktlint") {
    group = "ktlint"
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = getKtlintConfiguration()
    args("src/**/*.kt")
}
tasks.check {
    dependsOn("ktlint")
}

tasks.register<JavaExec>(name = "ktlintFormat") {
    group = "ktlint"
    description = "Fix Kotlin code style deviations."
    main = "com.pinterest.ktlint.Main"
    classpath = getKtlintConfiguration()
    args("-F", "src/**/*.kt")
}

tasks.register<JavaExec>(name = "ktlintIntellij") {
    group = "ktlint"
    description = "Setup IntelliJ KTLint configuration."
    classpath = getKtlintConfiguration()
    main = "com.pinterest.ktlint.Main"
    args("--apply-to-idea-project", "-y")
}
