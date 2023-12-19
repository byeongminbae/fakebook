# Spring Boot Boilerplate(Work In Progress)
I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting started

예제들을 사용하기 전, 아래 절차를 밟아 토큰을 발급받아야 함.

1. 스프링 부트 실행(./gradlew bootrun)
2. http://127.0.0.1:8080/swagger-ui/index.html 접속
3. POST /users 에서 유저를 생성
4. POST /token 에 유저 아이디를 넣고 토큰(AccessToken, RefreshToken) 발급
5. Swagger 최상단 오른쪽의 자물쇠 버튼(**Authorize**) 을 눌러 생성된 AccessToken 복사 붙여넣기

## Features

- 전역에서 사용되는 인증 로직은 AOP로 처리 하였습니다.
    1. 유저 권한에 따라 접근할 수 있는 API 를 제한할 수 있음
    2. 토큰 유저 아이디와 url 상 유저 아이디를 비교하는 공통 로직을 추상화 함
- JpaRepository 를 오버라이딩하여 자주 사용되는 코드 추상화

## Convention

1. 가독성과 불변을 위해 SuccessResponseDto 를 제외한 모든 DTO 는 Builder 패턴 적용
2. global 패키지는 타 패키지를 참조하지 않음
3. 클래스, 변수 네이밍은 명확하다면 길더라도 괜찮음
4. Internal DTO 는 클라이언트에게 전송하기 전, Response DTO 로 변환해야 함

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

- AuthAop 테스트 코드 작성
    - 다른 role 로 접근하는 케이스
    - Auth 어노테이션에 requestParamUserId 넘겼는데, 다른 userId 로 접근하는 케이스
- Refresh Token 에 userId 가 이미 담겨있는데 DB 에서 긁어올때 userId 를 검색조건으로 두는게 맞을까?
- Given, When, Then 으로 테스트 성공/실패 케이스 작성
- login api
- health api
