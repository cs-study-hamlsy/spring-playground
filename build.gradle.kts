plugins {
    java
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

allprojects {
    group = "com.playground"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")

        "compileOnly"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")
        "testCompileOnly"("org.projectlombok:lombok")
        "testAnnotationProcessor"("org.projectlombok:lombok")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

configure(
    listOf(
        project(":jpa:dirty-checking"),
        project(":jpa:n-plus-one"),
        project(":jpa:fetch-join"),
        project(":jpa:optimistic-lock"),
        project(":jpa:batch-insert"),
        project(":transaction:propagation"),
        project(":transaction:isolation"),
        project(":transaction:rollback"),
        project(":concurrency:pessimistic-lock"),
        project(":concurrency:distributed-lock"),
        project(":spring-core:bean-lifecycle"),
        project(":spring-core:aop"),
    )
) {
    apply(plugin = "org.springframework.boot")

    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter")
        "implementation"("org.springframework.boot:spring-boot-starter-data-jpa")
        "runtimeOnly"("com.h2database:h2")
    }
}
