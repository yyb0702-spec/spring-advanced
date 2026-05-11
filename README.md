## 📅 Spring Advanced

Spring Boot 기반의 일정 관리 REST API 서버입니다.

사용자는 회원가입 후 JWT 기반 로그인을 통해 인증을 진행할 수 있으며,

인증된 사용자는 일정(Todo)을 생성하고 댓글(Comment)을 작성할 수 있습니다.

또한 일정 작성자는 담당자(Manager)를 지정할 수 있고,

관리자 권한을 가진 사용자는 관리자 전용 API에 접근할 수 있습니다.

과제 진행 과정에서 단순 기능 구현뿐 아니라

N+1 문제 개선, Validation 구조 개선, API 로깅, 테스트 코드 개선 

등을 함께 진행했습니다.

---

## ⚙️ 주요 기능
### 👤 User (회원)
- 회원가입
- 로그인 (JWT 발급)
- 사용자 단건 조회
- 비밀번호 변경
- 사용자 권한 변경 (관리자 전용)
### 📅 Todo (일정)
- 일정 생성
- 단건 조회
- 전체 조회
- 수정
- 삭제
### 💬 Comment (댓글)
- 댓글 생성
- 특정 일정 댓글 목록 조회
- 댓글 삭제 (관리자 전용)
### 👥 Manager (담당자)
- 담당자 등록
- 담당자 목록 조회
- 담당자 삭제
### 🔐 인증 / 인가
- JWT 기반 인증
- 사용자 권한(Role) 관리
- 관리자 API 접근 제어
- ArgumentResolver 기반 인증 사용자 주입
- Interceptor 기반 관리자 접근 처리
### 📄 로깅 / 예외 처리
- AOP 기반 관리자 API 요청/응답 로깅
- Interceptor 기반 접근 로그 기록
- GlobalExceptionHandler 기반 공통 예외 처리
- Validation 실패 응답 형식 통일

---

## 🛠 기술 스택
| 구분             | 기술                         |
| -------------- | -------------------------- |
| Language       | Java 17                    |
| Framework      | Spring Boot 3.3.3          |
| ORM            | Spring Data JPA, Hibernate |
| Database       | H2, MySQL                  |
| Authentication | JWT                        |
| Validation     | Bean Validation            |
| Logging        | Spring AOP, Interceptor    |
| Test           | JUnit5, Mockito            |
| Build Tool     | Gradle                     |

---

## 📡 API 명세 요약
### 🔐 Auth
| Method | URL            | 설명   |
| ------ | -------------- | ---- |
| POST   | `/auth/signup` | 회원가입 |
| POST   | `/auth/signin` | 로그인  |

### 👤 User
| Method | URL                     | 설명        |
| ------ | ----------------------- | --------- |
| GET    | `/users/{userId}`       | 사용자 조회    |
| PUT    | `/users`                | 비밀번호 변경   |
| PATCH  | `/admin/users/{userId}` | 사용자 권한 변경 |

### 📅 Todo
| Method | URL               | 설명       |
| ------ | ----------------- | -------- |
| POST   | `/todos`          | 일정 생성    |
| GET    | `/todos`          | 일정 목록 조회 |
| GET    | `/todos/{todoId}` | 일정 단건 조회 |

### 💬 Comment
| Method | URL                           | 설명        |
| ------ | ----------------------------- | --------- |
| POST   | `/todos/{todoId}/comments`    | 댓글 생성     |
| GET    | `/todos/{todoId}/comments`    | 댓글 목록 조회  |
| DELETE | `/admin/comments/{commentId}` | 관리자 댓글 삭제 |

### 👥 Manager
| Method | URL                                    | 설명        |
| ------ | -------------------------------------- | --------- |
| POST   | `/todos/{todoId}/managers`             | 담당자 등록    |
| GET    | `/todos/{todoId}/managers`             | 담당자 목록 조회 |
| DELETE | `/todos/{todoId}/managers/{managerId}` | 담당자 삭제    |

---

## 📂 프로젝트 구조
### 📦 src
```
┣ 📂 main
┃ ┣ 📂 java
┃ ┃ ┗ 📂 org.example.expert
┃ ┃   ┣ 📂 client
┃ ┃   ┃ ┗ 📂 dto
┃ ┃   ┣ 📂 config
┃ ┃   ┗ 📂 domain
┃ ┃     ┣ 📂 auth
┃ ┃     ┣ 📂 comment
┃ ┃     ┣ 📂 common
┃ ┃     ┣ 📂 manager
┃ ┃     ┣ 📂 todo
┃ ┃     ┗ 📂 user
┃ ┗ 📂 resources
┃   ┗ 📄 application.yml
┗ 📂 test
┗ 📂 java
┗ 📂 org.example.expert
```
---
## 🔥 주요 개선 사항

### lv0. 프로젝트 실행 설정 복구
초기 실행 시 Gradle wrapper 설정과 애플리케이션 설정이 누락되어 실행이 실패했습니다.

- gradle-wrapper.properties 추가
- application.properties 추가
- JWT secret 설정 추가
- driver-class 설정 추가
- JWT 의존성 범위 수정

### lv1.AuthUserArgumentResolver 등록
@Auth AuthUser 파라미터가 정상 동작하도록 AuthUserArgumentResolver를 Spring MVC에 등록했습니다.

- AuthUserArgumentResolver를 Bean으로 등록
- WebConfig에서 addArgumentResolvers()로 resolver 추가
- JWT Filter에서 request attribute로 저장한 사용자 정보를 컨트롤러 파라미터로 주입

### lv2.코드 개선
Early Return, 불필요한 if-else 제거, Bean Validation 적용을 진행했습니다.

- 회원가입 중복 이메일 검증을 먼저 처리
- WeatherClient의 불필요한 else 제거
- 비밀번호 형식 검증을 DTO의 Bean Validation으로 이동

### lv3.N+1 문제 개선
JPQL fetch join 기반 조회를 @EntityGraph 기반 조회로 변경했습니다.

- TodoRepository의 fetch join 제거
- @EntityGraph(attributePaths = {"user"}) 적용
- Todo 조회 시 user 연관 엔티티를 함께 로딩

### lv4.테스트 코드 개선
기존 실패 테스트를 실제 서비스 동작과 맞게 수정했습니다.

- PasswordEncoderTest의 matches() 인자 순서 수정
- Manager 목록 조회 예외 테스트 수정
- Comment 등록 실패 예외 타입 수정
- todo.user == null 케이스에서 NPE 대신 도메인 예외 발생하도록 수정

### lv5.API 로깅
Interceptor와 AOP를 활용해 관리자 API 접근 제어와 로깅을 구현했습니다.

- AdminInterceptor로 /admin/** 접근 권한 확인
- 관리자 접근 시 요청 시각과 URL 로깅
- AdminApiLoggingAspect로 관리자 API 요청/응답 로깅
- 요청 사용자 ID, 요청 시각, URL, RequestBody, ResponseBody 기록

### lv6.검증 정책 및 매핑 개선
도전 과제로 직접 문제를 정의하고 개선했습니다.

- @Valid 실패 응답 형식 통일
- 회원가입 비밀번호 형식 검증 추가

---

## ✨ 회고

프로젝트를 진행하면서 가장 어렵게 느껴졌던 부분 중 하나는 이미 작성되어 있는 코드를 읽고 흐름을 파악하는 과정이었다. 

기존 구조를 이해하지 못하면 어떤 부분을 수정해야 하는지조차 판단하기 어려웠다. 

JWT 인증 흐름, ArgumentResolver 등록 과정, 테스트 코드 의도, 예외 처리 구조처럼 

여러 컴포넌트가 연결된 부분은 호출 흐름을 따라가며 하나씩 확인해야 했다.

이번 경험을 통해 기능 구현 능력뿐 아니라, 기존 코드를 읽고 구조를 이해하는 능력도 매우 중요하다는 점을 느꼈다. 

단순히 코드를 작성하는 것에 그치지 않고, 다른 사람이 작성한 코드의 흐름과 의도를 빠르게 파악할 수 있도록

더 많은 코드 리딩 경험이 필요하다고 느꼈다.