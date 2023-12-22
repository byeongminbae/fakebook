# Spring Boot Boilerplate(Work In Progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting Started

Spring Boot를 활용한 REST API 개발에 대한 저의 생각을 보일러 플레이트화 하여 공유하는 레포지토리 입니다.

주로 유지 보수가 용이한 코드 작성을 위한 프로젝트 구조, AOP, 테스트 코드, 예외 처리 등의 내용이 포함될 예정입니다.

시작하기 전에는 OpenJDK 17이 설치되어 있어야 합니다.

API 패키지에 있는 예제들은 필요에 따라 삭제하셔도 됩니다.

### Endpoints

- Swagger: http://127.0.0.1:8080/swagger-ui/index.html
- Health Check: http://127.0.0.1:8080/actuator/health

### Features
... 

## Q&A

### @Auth 어노테이션에 대해 더 설명해주세요

```java
@Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, pathVariableUserId = "userId")
    @Operation(summary = "Get user", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/{userId}")
    public SuccessResponseDto<GetUserResponseDto> getUser(@PathVariable Long userId) {}
```

@Auth 이 붙어있는 컨트롤러에 토큰과 함께 요청이 왔다고 가정해봅시다.

AuthAop 객체의 before 메서드가 호출이 되는데 이는 3가지 과정을 거쳐 요청을 받아들일지 예외를 던질지 판단합니다.

1. 요청에 담겨있는 토큰을 가져와 디코딩합니다. 물론, 만료되었거나 유효하지 않다면 예외가 발생합니다.
2. 토큰 내의 userRole 이 @Auth 에 나열된 userRoles 에 포함되는지 체크합니다.
3. 요청에 적힌 userId 와 토큰에 적힌 유저 아이디가 동일한지 체크합니다. 요청에 적힌 유저아이디를 식별할 수 있는 이유는 pathVariableUserId 에 필드명을 명시하였기 때문에 가능합니다.

pathVariableUserId = "userId" , @GetMapping("/{userId}"), @PathVariable Long userId

이 세가지에 들어가는 “userId” 라는 문자열은 동일해야 합니다. 가령 imUserId 라고 쓰고 싶다면 아래와 같이 하면 됩니다.

```java
@Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, pathVariableUserId = "imUserId")
    @Operation(summary = "Get user", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/{imUserId}")
    public SuccessResponseDto<GetUserResponseDto> getUser(@PathVariable Long imUserId) {}
```

만약, userId 체크 기능이 필요하지 않다면 아래와 같이 비활성화 할 수 있습니다.

```java
@Auth(userRoles = {UserRole.ADMIN, UserRole.USER})
```

### API 를 사용할 수 없어요

API 를 사용하려면 다음 절차를 따라야 합니다.

1. 스프링 부트 실행
2. http://127.0.0.1:8080/swagger-ui/index.html 접속
3. POST /users 에서 유저를 생성
4. POST /token 에 위에서 생성한 유저 아이디를 넣고 토큰(AccessToken, RefreshToken) 발급
5. Swagger 최상단 오른쪽의 자물쇠 버튼(**Authorize**) 을 눌러 생성된 AccessToken 복사 붙여넣기


## Convention

1. SuccessResponseDto를 제외한 모든 DTO는 가독성과 불변성을 위해 Builder 패턴을 적용한다.
2. global 패키지는 다른 패키지를 참조하지 않는다.
3. 네이밍은 명확하다면 길어도 문제 없다.
4. Internal DTO는 클라이언트에게 전송하기 전에 Response DTO로 변환되어야 한다.
5. 테스트 코드는 Given, When, Then 컨벤션으로 성공, 실패 케이스를 작성한다.
6. 특정 API 에 종속적인 예외는 ExceptionType 구현하여 해당 API 패키지 아래에 선언한다.
7. 모든 API 응답은 SuccessResponseDto 통해 응답하여야 한다.
8. 모든 엔티티는 BaseEntity 를 상속 받아야 한다.
9. 애플리케이션 전반에 공통적으로 사용되는 예외는 CommonException 에 정의하되, 커스텀 예외가 필요한 경우 api 패키지 아래에 ExceptionType 을 상속받아 선언한다.


## ToDo

- 테스트 코드 작성
    - AuthAop: 다른 role 로 접근하는 케이스, Auth 어노테이션에 pathVariableUserId 넘겼는데, 다른 userId 로 접근하는 케이스
- Refresh Token 에 userId 가 이미 담겨있는데 DB 에서 긁어올때 userId 를 검색조건으로 두는게 맞을까?
- login api
- 로그아웃: 서버측 리프레시 토큰을 지운 뒤, 클라이언트 토큰을 모두 삭제한다.
