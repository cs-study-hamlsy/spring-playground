# Spring Playground

Spring 핵심 개념을 주제별로 직접 실험하고, 결과를 기록하기 위한 멀티 모듈 학습 저장소입니다.  
각 모듈은 하나의 질문을 중심으로 실험하며, 테스트와 문서(README/PR)를 통해 재현 가능한 학습 기록을 남기는 것을 목표로 합니다.

## 1. 프로젝트 목표

- JPA, 트랜잭션, 동시성, Spring Core 주제를 모듈 단위로 분리해 실험
- 테스트 코드 기반으로 “가설 -> 실행 -> 검증” 흐름을 고정
- 실험 결과를 모듈 README와 PR에 기록해 지식 자산화
- 새 실험 주제를 빠르게 추가할 수 있는 일관된 구조 유지

## 2. 기술 스택

- Java: 21
- Build: Gradle (Kotlin DSL)
- Framework: Spring Boot 3.3.x
- DB: H2 (in-memory)
- Test: JUnit 5, Spring Boot Test, AssertJ

## 3. 디렉터리 구조

```text
spring-playground/
├── core/
│   └── src/main/java/com/playground/core/domain/BaseEntity.java
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
├── .github/
│   ├── ISSUE_TEMPLATE/
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows/ci.yml
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

현재는 요청사항에 맞춰 `core`에는 `BaseEntity`만 작성되어 있고, 나머지는 실험용 폴더 스켈레톤 중심으로 구성되어 있습니다.

## 4. 모듈 목록

| Category | Module | Purpose |
|---|---|---|
| JPA | `jpa:dirty-checking` | 영속성 컨텍스트 변경 감지 검증 |
| JPA | `jpa:n-plus-one` | N+1 문제 재현 및 해결 비교 |
| JPA | `jpa:fetch-join` | fetch join 동작 확인 |
| JPA | `jpa:optimistic-lock` | 낙관적 락(`@Version`) 시나리오 |
| JPA | `jpa:batch-insert` | 대량 insert 성능 실험 |
| Transaction | `transaction:propagation` | 전파 옵션 실험 |
| Transaction | `transaction:isolation` | 격리수준/동시성 현상 실험 |
| Transaction | `transaction:rollback` | rollback 동작/예외 정책 실험 |
| Concurrency | `concurrency:pessimistic-lock` | 비관적 락 동작 실험 |
| Concurrency | `concurrency:distributed-lock` | 분산 락 시나리오 실험 |
| Spring Core | `spring-core:bean-lifecycle` | Bean 생명주기 확인 |
| Spring Core | `spring-core:aop` | AOP 프록시/포인트컷 동작 확인 |

## 5. 시작하기

### 5-1. 사전 준비

- JDK 21 설치
- Git 설치
- (권장) IntelliJ IDEA

### 5-2. 저장소 클론

```bash
git clone https://github.com/cs-study-hamlsy/spring-playground.git
cd spring-playground
```

### 5-3. 빌드/테스트 실행

전체 빌드:

```bash
./gradlew build
```

특정 모듈 테스트:

```bash
./gradlew :jpa:dirty-checking:test
./gradlew :transaction:propagation:test
./gradlew :concurrency:pessimistic-lock:test
```

특정 테스트 클래스만 실행:

```bash
./gradlew :jpa:dirty-checking:test --tests "com.playground.jpa.dirtychecking.DirtyCheckingTest"
```

애플리케이션 실행:

```bash
./gradlew :jpa:dirty-checking:bootRun
```

H2 콘솔(모듈 실행 중):

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## 6. 새 실험 모듈 작업 방법

1. 이슈 생성: `.github/ISSUE_TEMPLATE/experiment.yml` 사용
2. 브랜치 생성: `experiment/{category}/{topic}`
3. 모듈 등록: `settings.gradle.kts`에 `include("{category}:{topic}")` 추가
4. 폴더 생성: 기존 모듈 구조를 기준으로 `src/main`, `src/test` 스켈레톤 생성
5. 테스트 우선으로 실험 코드 작성
6. 모듈 README에 목적/케이스/결론 기록
7. PR 템플릿 작성 후 리뷰 요청

## 7. 협업 규칙

브랜치 네이밍:

- `experiment/{category}/{topic}`
- `setup/{description}`
- `fix/{description}`
- `docs/{description}`

커밋 메시지:

```text
{type}({scope}): {description}
```

예시:

- `exp(jpa/dirty-checking): add transactional dirty-checking case`
- `test(transaction/propagation): add requires_new rollback scenario`
- `docs(readme): update run guide`
- `setup(core): initialize base entity`

## 8. 문서 가이드

- 프로젝트 초기 세팅 및 운영 계획: [plans/project-init-plan.md](plans/project-init-plan.md)
- 협업 절차: [CONTRIBUTING.md](CONTRIBUTING.md)

## 9. CI

GitHub Actions(`.github/workflows/ci.yml`)에서 다음을 검증합니다.

- Gradle Wrapper 유효성
- `./gradlew build` 실행 성공

## 10. 앞으로의 확장 방향

- 각 모듈에 최소 2개 이상의 실험 시나리오 테스트 추가
- 모듈별 README에 결과 테이블/인사이트 누적
- 필요 시 QueryDSL, Testcontainers, 외부 인프라(예: Redis) 단계적 도입
