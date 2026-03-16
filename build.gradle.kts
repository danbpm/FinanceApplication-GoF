plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.tigerbank"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Настройка кодировки для ВСЕХ задач, использующих JVM
tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
    systemProperty("sun.stdout.encoding", "UTF-8")
    systemProperty("sun.stderr.encoding", "UTF-8")
}

// Дополнительно: настройка для компиляции
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Дополнительно: настройка для тестов
tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
    systemProperty("sun.stdout.encoding", "UTF-8")
    systemProperty("sun.stderr.encoding", "UTF-8")
    useJUnitPlatform()
}