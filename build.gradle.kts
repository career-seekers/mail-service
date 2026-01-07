import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.0"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.serialization") version "2.1.20"
    id("com.google.protobuf") version "0.9.4"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("it.nicolasfarabegoli.conventional-commits") version "3.1.3"
}

val mockitoAgent: Configuration by configurations.creating {
    isTransitive = false
}

group = "org.careerseekers"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val springStarterMail: String by project
val jsonwebtokenVersion: String by project
val jaxbApiVersion: String by project
val mapstructVersion: String by project
val grpcMessagingVersion: String by project
val protobufVersion: String by project
val grpcProtobufVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationVersion: String by project
val dotenvSpringVersion: String by project
val javaxAnnotationVersion: String by project
val mockitoVersion: String by project

dependencies {
    // Spring Boot
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-mail:$springStarterMail")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // JWT Auth
    implementation("io.jsonwebtoken:jjwt:$jsonwebtokenVersion")
    implementation("javax.xml.bind:jaxb-api:$jaxbApiVersion")

    // Databases
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Mapper
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Kafka messaging
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // gRPC messaging
    implementation("net.devh:grpc-server-spring-boot-starter:$grpcMessagingVersion")
    implementation("net.devh:grpc-client-spring-boot-starter:$grpcMessagingVersion")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("io.grpc:grpc-protobuf:$grpcProtobufVersion")
    implementation("io.grpc:grpc-stub:$grpcProtobufVersion")

    // Kotlinx coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //Kotlinx serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Utilities
    implementation("one.stayfocused.spring:dotenv-spring-boot:$dotenvSpringVersion")
    implementation("javax.annotation:javax.annotation-api:$javaxAnnotationVersion")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    mockitoAgent("org.mockito:mockito-core")

    // Metrics
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Netty
    val nettyVersion = "4.1.122.Final"
    val osName = System.getProperty("os.name").lowercase()
    val osArch = System.getProperty("os.arch").lowercase()

    if (osName.contains("mac") && osArch == "aarch64") {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:$nettyVersion:osx-aarch_64")
    } else if (osName.contains("mac")) {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:$nettyVersion:osx-x86_64")
    }
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:4.28.2" }
    plugins {
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.57.2" }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    jvmArgs("-Xshare:off")
}

tasks.bootBuildImage {
    runImage = "paketobuildpacks/ubuntu-noble-run-base:latest"
}

conventionalCommits {
    warningIfNoGitRoot = true
    types += listOf("build", "chore", "docs", "feat", "fix", "refactor", "style", "test")
    scopes = emptyList()
    successMessage = "Сообщение коммита соответствует стандартам Conventional Commit."
    failureMessage = "Сообщение коммита не соответствует стандартам Conventional Commit."
}
