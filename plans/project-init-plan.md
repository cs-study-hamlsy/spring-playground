# Spring Playground - 프로젝트 초기 세팅 플랜

> 이 문서는 AI(또는 개발자)가 프로젝트를 처음부터 세팅할 수 있도록 작성된 실행용 명세서입니다.  
> 아래 순서대로 진행하면 초기 구조, 빌드, 테스트, 문서화, 협업 설정까지 한 번에 맞출 수 있습니다.

---

## 0. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 목적 | Spring 관련 개념을 주제별로 실험하고 기록하는 멀티모듈 학습 저장소 |
| 언어 | Java 21 |
| 빌드 도구 | Gradle Wrapper + Kotlin DSL (`build.gradle.kts`) |
| 프레임워크 | Spring Boot 3.3.x |
| DB | H2 in-memory |
| 테스트 | JUnit 5 + AssertJ + Spring Boot Test |
| 저장소 형태 | Mono-repo, Gradle 멀티모듈 |
| 운영 원칙 | 실험은 작게, 결과는 문서화, 테스트는 재현 가능하게 유지 |

---

## 1. 초기 세팅 목표

초기 세팅은 단순히 프로젝트가 실행되는 수준이 아니라 아래 상태를 만족해야 합니다.

- 저장소를 클론한 뒤 `./gradlew build` 가 바로 통과한다.
- 새 실험 모듈을 정해진 규칙으로 빠르게 추가할 수 있다.
- 실험 결과를 README, PR, 이슈로 연결해 누적 기록할 수 있다.
- 테스트가 로컬/CI에서 같은 방식으로 재현된다.
- 공통 설정과 실험별 설정의 경계가 분명하다.

---

## 2. 최종 디렉터리 구조

```text
spring-playground/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── experiment.yml
│   │   └── bug.yml
│   ├── workflows/
│   │   └── ci.yml
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── CODEOWNERS
├── gradle/
│   └── wrapper/
├── core/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/playground/core/
│       │   ├── domain/
│       │   └── config/
│       ├── main/resources/
│       │   └── application-core.yml
│       └── test/java/com/playground/core/
├── jpa/
│   ├── dirty-checking/
│   ├── n-plus-one/
│   ├── fetch-join/
│   ├── optimistic-lock/
│   └── batch-insert/
├── transaction/
│   ├── propagation/
│   ├── isolation/
│   └── rollback/
├── concurrency/
│   ├── pessimistic-lock/
│   └── distributed-lock/
├── spring-core/
│   ├── bean-lifecycle/
│   └── aop/
├── templates/
│   └── module-template/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
├── .gitattributes
├── .editorconfig
├── CONTRIBUTING.md
├── README.md
└── plans/
    └── project-init-plan.md
```

### 2-1. 각 실험 모듈 내부 구조

```text
jpa/dirty-checking/
├── build.gradle.kts
├── README.md
└── src/
    ├── main/
    │   ├── java/com/playground/jpa/dirtychecking/
    │   │   ├── DirtyCheckingApplication.java
    │   │   ├── domain/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources/
    │       ├── application.yml
    │       └── data.sql
    └── test/
        ├── java/com/playground/jpa/dirtychecking/
        │   └── DirtyCheckingTest.java
        └── resources/
            └── application-test.yml
```

> 패키지 네이밍 규칙: `com.playground.{category}.{topicCamelCase}`  
> 예: `com.playground.jpa.dirtychecking`, `com.playground.transaction.propagation`

---

## 3. 추가로 반영할 개선 포인트

기존 플랜에 더해 아래 항목을 초기 세팅에 포함하는 것을 권장합니다.

### 3-1. 저장소 표준 파일

- `.editorconfig`: 줄바꿈, 인덴트, charset 통일
- `.gitattributes`: Windows/Unix 환경에서 줄바꿈 차이 최소화
- `.gitignore`: `.idea`, `build/`, `.gradle/`, `*.iml`, `.DS_Store` 등 포함
- `CONTRIBUTING.md`: 새 실험 추가 절차, 브랜치/커밋/PR 규칙 정리

### 3-2. CI 자동화

- `.github/workflows/ci.yml` 추가
- 기본 검증은 `./gradlew build`
- `main`, `develop`가 있다면 해당 브랜치와 PR 대상으로 동작
- 최소 JDK 21 환경에서 실행

### 3-3. 공통 설정 분리

- `core` 모듈 설정은 공통 베이스 역할만 하도록 최소화
- 각 실험 모듈은 자체 `application.yml` 을 갖되 공통 패턴을 유지
- 테스트 전용 값은 `application-test.yml` 로 분리
- `data.sql` 을 사용할 경우 `spring.jpa.defer-datasource-initialization=true` 를 기본 고려

### 3-4. 모듈 생성 생산성

- `templates/module-template/` 에 기본 스켈레톤을 두고 복사 기반 생성
- 또는 이후 `scripts/create-module.ps1` 같은 생성 스크립트 추가를 고려
- 초기 세팅에서는 우선 템플릿 디렉터리까지 준비

### 3-5. 문서 연결성

- 루트 README에 실험 목록 표를 유지
- 각 모듈 README에 관련 이슈/PR 링크 자리 확보
- `CONTRIBUTING.md` 와 루트 README 사이 링크 연결

---

## 4. Gradle 설정 원칙

### 4-1. `gradle.properties`

```properties
# Java
javaVersion=21

# Spring Boot
springBootVersion=3.3.4
springDependencyManagementVersion=1.1.6

# Lombok
lombokVersion=1.18.34

# QueryDSL (optional)
querydslVersion=5.1.0
```

### 4-2. `settings.gradle.kts`

```kotlin
rootProject.name = "spring-playground"

// core
include("core")

// JPA
include("jpa:dirty-checking")
include("jpa:n-plus-one")
include("jpa:fetch-join")
include("jpa:optimistic-lock")
include("jpa:batch-insert")

// Transaction
include("transaction:propagation")
include("transaction:isolation")
include("transaction:rollback")

// Concurrency
include("concurrency:pessimistic-lock")
include("concurrency:distributed-lock")

// Spring Core
include("spring-core:bean-lifecycle")
include("spring-core:aop")
```

### 4-3. 루트 `build.gradle.kts`

루트 빌드는 모든 하위 모듈에 공통 규칙을 적용하되, 공통 의존성과 실행 모듈 성격을 분리해서 관리합니다.

```kotlin
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

configure(listOf(
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
    project(":spring-core:aop")
)) {
    apply(plugin = "org.springframework.boot")

    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter")
        "implementation"("org.springframework.boot:spring-boot-starter-data-jpa")
        "runtimeOnly"("com.h2database:h2")
    }
}

project(":core") {
    tasks.named("jar") { enabled = true }
}
```

### 4-4. 왜 이렇게 분리하는가

- `core` 는 라이브러리 성격이므로 Spring Boot 실행 모듈로 강제하지 않는 편이 안전하다.
- 모든 서브모듈에 무조건 `org.springframework.boot` 를 적용하면, 실행 클래스가 필요 없는 모듈까지 Boot 모듈이 된다.
- 실험 모듈과 라이브러리 모듈의 역할을 분리하면 이후 확장 시 덜 흔들린다.

---

## 5. `core` 모듈 설계 원칙

`core` 는 실험을 돕는 공통 기반만 담고, 실험의 결론을 흐릴 정도로 무거워지지 않게 유지합니다.

- 공통 엔티티는 최소 세트만 둔다.
- 실험에서 독립성이 중요하면 모듈 내 엔티티를 별도로 생성한다.
- 공통 엔티티는 학습용 예제를 돕는 수준에서만 유지한다.
- `core` 에 서비스/비즈니스 로직을 과도하게 넣지 않는다.

### 5-1. 권장 엔티티

- `BaseEntity`
- `Member`
- `Team`
- `Product`
- `Order`
- `OrderItem`

### 5-2. 보완 권장 사항

- `BaseEntity` 를 쓸 경우 Auditing 활성화 위치를 명확히 한다.
- 양방향 연관관계는 편의 메서드를 둘지 여부를 초기에 정한다.
- `toString()` 에 연관관계 필드를 넣지 않는다.
- 동시성 실험용 필드(`@Version`)는 실험 목적이 분명한 엔티티에만 둔다.

---

## 6. 애플리케이션 설정 원칙

### 6-1. 실험 모듈 `application.yml` 기본 예시

```yaml
spring:
  application:
    name: "{module-name}-playground"
  datasource:
    url: jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
    open-in-view: false
  sql:
    init:
      mode: always

logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.orm.jdbc.bind: trace
```

### 6-2. 설정 보완 포인트

- `show_sql` 대신 logger 기반 출력만 써서 중복 로그를 줄인다.
- `open-in-view` 는 `false` 로 고정한다.
- 테스트에서만 필요한 설정은 `src/test/resources/application-test.yml` 에 둔다.
- 테스트 클래스에 `@ActiveProfiles("test")` 사용 여부를 팀 규칙으로 정한다.

---

## 7. 브랜치 전략

### 7-1. 브랜치 구조

```text
main
└── experiment/{category}/{topic}
```

### 7-2. 네이밍 규칙

| 유형 | 패턴 | 예시 |
|------|------|------|
| 실험 | `experiment/{category}/{topic}` | `experiment/jpa/dirty-checking` |
| 세팅 | `setup/{description}` | `setup/init-multi-module` |
| 버그 수정 | `fix/{description}` | `fix/h2-ddl-error` |
| 문서 | `docs/{description}` | `docs/readme-update` |

### 7-3. 커밋 메시지 컨벤션

```text
{type}({scope}): {description}
```

| type | 용도 |
|------|------|
| `exp` | 실험 코드 추가/수정 |
| `test` | 테스트 추가/수정 |
| `docs` | 문서 수정 |
| `setup` | 빌드/설정 변경 |
| `fix` | 버그 수정 |
| `chore` | 운영성 보조 작업 |

예시:

```text
exp(jpa/dirty-checking): 트랜잭션 내 엔티티 변경 감지 케이스 추가
setup(core): BaseEntity auditing 설정 추가
docs(readme): dirty-checking 모듈 결과 기록
```

---

## 8. 새 실험 모듈 생성 컨벤션

### Step 1. 이슈 생성

- GitHub에서 `experiment.yml` 템플릿으로 이슈 생성
- 라벨: `experiment` + 카테고리 라벨

### Step 2. 브랜치 생성

```bash
git checkout main
git pull
git checkout -b experiment/{category}/{topic}
```

### Step 3. `settings.gradle.kts` 에 모듈 등록

```kotlin
include("{category}:{topic}")
```

### Step 4. 템플릿 기반 스켈레톤 생성

아래 파일은 최소 생성 단위입니다.

- `build.gradle.kts`
- `README.md`
- `src/main/java/.../{Topic}Application.java`
- `src/main/resources/application.yml`
- `src/test/java/.../{Topic}Test.java`
- `src/test/resources/application-test.yml`

### Step 5. 테스트 우선으로 실험 구현

- 테스트 이름에 케이스 의도를 드러낸다.
- 하나의 실험 모듈은 한 가지 학습 질문에 집중한다.
- 실험 코드와 검증 코드의 경계를 분리한다.

### Step 6. 실행 확인

```bash
./gradlew :{category}:{topic}:test
./gradlew :{category}:{topic}:test --info
./gradlew :{category}:{topic}:bootRun
```

### Step 7. 문서화

- 모듈 README에 실험 목적, 케이스, 결과, 결론 기록
- PR 템플릿의 결과 표와 인사이트를 채움
- 루트 README 실험 목록 표 업데이트

---

## 9. GitHub 템플릿 및 협업 파일

초기 세팅 시 아래 파일까지 함께 만드는 것을 권장합니다.

- `.github/ISSUE_TEMPLATE/experiment.yml`
- `.github/ISSUE_TEMPLATE/bug.yml`
- `.github/PULL_REQUEST_TEMPLATE.md`
- `.github/workflows/ci.yml`
- `CODEOWNERS` (선택)
- `CONTRIBUTING.md`

### 9-1. CI에서 검증할 최소 항목

- Gradle Wrapper 실행 가능 여부
- 전체 모듈 `build`
- PR 기준 기본 컴파일/테스트 통과 여부

---

## 10. 루트 README에 꼭 들어갈 내용

- 프로젝트 목적
- 디렉터리 구조
- 실험 목록 표
- 전체 빌드/특정 모듈 테스트 방법
- 새 실험 추가 절차
- `CONTRIBUTING.md` 링크
- 브랜치/커밋 전략 요약

---

## 11. 세팅 순서 요약 체크리스트

```text
[ ] 1. 루트 디렉터리와 Gradle Wrapper 생성
[ ] 2. `gradle.properties`, `settings.gradle.kts`, 루트 `build.gradle.kts` 생성
[ ] 3. `.gitignore`, `.gitattributes`, `.editorconfig` 생성
[ ] 4. `.github/ISSUE_TEMPLATE`, `PULL_REQUEST_TEMPLATE.md`, `workflows/ci.yml` 생성
[ ] 5. `CONTRIBUTING.md` 생성
[ ] 6. `core/` 모듈 생성
[ ] 7. 실험 모듈 디렉터리 스켈레톤 생성
[ ] 8. `templates/module-template/` 생성
[ ] 9. 루트 `README.md` 생성 및 `CONTRIBUTING.md` 링크 연결
[ ] 10. 전체 빌드 확인: ./gradlew build
[ ] 11. 대표 모듈 테스트 확인: ./gradlew :jpa:dirty-checking:test
[ ] 12. CI 워크플로우 기준으로 로컬 검증 재실행
```

---

## 12. 초기에 일부러 미루는 항목

아래 항목은 유용하지만, 초기 세팅에서 반드시 넣어야 하는 것은 아닙니다.

- Testcontainers
- Redis, MySQL, Kafka 같은 외부 인프라 도커 구성
- Spotless / Checkstyle / SonarQube
- QueryDSL 전체 적용
- 모듈 자동 생성 스크립트

필요해지는 시점에 별도 `setup/*` 브랜치로 추가해도 충분합니다.

---

## 13. 결론

기존 플랜은 구조와 학습 의도가 잘 잡혀 있습니다.  
초기 세팅 단계에서는 여기에 아래 네 가지만 더 넣으면 훨씬 안정적입니다.

- `core` 와 실행 모듈의 역할 분리
- 테스트/로컬/CI 설정 일관성 확보
- 템플릿 및 협업 문서 보강
- README/이슈/PR 간 기록 연결 강화
