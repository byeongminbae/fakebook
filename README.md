# Spring Boot Boilerplate(Work In Progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting Started

### **Dependencies**

- OpenJDK 17

### **Executing**

```java
$ ./gradlew bootrun
```

### Endpoints
- http://127.0.0.1:8080/swagger-ui/index.html
- http://127.0.0.1:8080/actuator/health

## Features

### Global

- AOP를 활용한 전역 인증 로직 관심사 분리
    1. 유저 권한에 따른 API 접근 제어 기능
    2. 토큰 유저 아이디와 URL 상의 유저 아이디 비교를 위한 공통 로직 추상화
- BaseRepository 에서 JpaRepository 오버라이딩을 통한 공통 코드 추상화
    1. ID 기반 조회 시 데이터가 존재하지 않을 경우 예외 발생

### API

API 패키지의 예제들은 전부 삭제해도 무방합니다.

- 유저 기본 CRUD 예제 제공
- 토큰(Access Token, Refresh Token) 기반 인증 API 기본 제공

## Convention

1. SuccessResponseDto를 제외한 모든 DTO는 가독성과 불변성을 위해 Builder 패턴을 적용한다.
2. global 패키지는 다른 패키지를 참조하지 않는다.
3. 네이밍은 명확하다면 길어도 문제 없다.
4. Internal DTO는 클라이언트에게 전송하기 전에 Response DTO로 변환되어야 한다.

## Project Structure

```
.
.
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── boilerplate
│   │               ├── Application.java
│   │               ├── api
│   │               │   ├── token
│   │               │   │   ├── controller
│   │               │   │   │   └── TokenController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── request
│   │               │   │   │   │   ├── CreateTokenRequestDto.java
│   │               │   │   │   │   └── RenewTokenRequestDto.java
│   │               │   │   │   └── response
│   │               │   │   │       ├── AccessTokenResponseDto.java
│   │               │   │   │       ├── RefreshTokenResponseDto.java
│   │               │   │   │       └── TokenResponseDto.java
│   │               │   │   ├── entity
│   │               │   │   │   └── RefreshTokenEntity.java
│   │               │   │   ├── repository
│   │               │   │   │   └── TokenRepository.java
│   │               │   │   └── service
│   │               │   │       └── TokenService.java
│   │               │   └── user
│   │               │       ├── controller
│   │               │       │   └── UserController.java
│   │               │       ├── dto
│   │               │       │   ├── request
│   │               │       │   │   ├── CreateUserRequestDto.java
│   │               │       │   │   ├── ReplaceUserRequestDto.java
│   │               │       │   │   └── UpdateUserRequestDto.java
│   │               │       │   └── response
│   │               │       │       ├── CreateUserResponseDto.java
│   │               │       │       ├── GetUserResponseDto.java
│   │               │       │       ├── ReplaceUserResponseDto.java
│   │               │       │       └── UpdateUserResponseDto.java
│   │               │       ├── entity
│   │               │       │   └── UserEntity.java
│   │               │       ├── repository
│   │               │       │   └── UserRepository.java
│   │               │       └── service
│   │               │           └── UserService.java
│   │               └── global
│   │                   ├── aop
│   │                   │   ├── Auth.java
│   │                   │   └── AuthAop.java
│   │                   ├── auth
│   │                   │   ├── TokenManager.java
│   │                   │   └── dto
│   │                   │       └── internal
│   │                   │           ├── AccessTokenInternalDto.java
│   │                   │           ├── AccessTokenPayloadInternalDto.java
│   │                   │           ├── RefreshTokenInternalDto.java
│   │                   │           ├── RefreshTokenPayloadInternalDto.java
│   │                   │           └── TokenInternalDto.java
│   │                   ├── config
│   │                   │   └── SwaggerConfig.java
│   │                   ├── dto
│   │                   │   └── response
│   │                   │       ├── ExceptionResponseDto.java
│   │                   │       └── SuccessResponseDto.java
│   │                   ├── entity
│   │                   │   ├── BaseEntity.java
│   │                   │   ├── UserRole.java
│   │                   │   └── UserStatus.java
│   │                   ├── exception
│   │                   │   ├── BusinessException.java
│   │                   │   ├── CommonException.java
│   │                   │   ├── ExceptionType.java
│   │                   │   └── GlobalExceptionHandler.java
│   │                   ├── repository
│   │                   │   └── BaseRepository.java
│   │                   └── util
│   │                       └── TimeUtil.java
│   └── resources
│       └── application.yml
└── test
    └── java
        └── com
            └── example
                └── boilerplate
                    ├── ApplicationTests.java
                    └── global
                        └── auth
                            └── TokenManagerTest.java

```

## ToDo

- 테스트 코드 작성
    - Given, When, Then 으로 테스트 성공/실패 케이스 작성
    - AuthAop: 다른 role 로 접근하는 케이스, Auth 어노테이션에 requestParamUserId 넘겼는데, 다른 userId 로 접근하는 케이스
- Refresh Token 에 userId 가 이미 담겨있는데 DB 에서 긁어올때 userId 를 검색조건으로 두는게 맞을까?
- login api
- health api
