# Spring Boot Boilerplate(Work In Progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

<img width="1278" alt="image" src="https://github.com/andrewbae/spring-boot-boilerplate/assets/40394063/bfd1e61c-8323-4ac4-bf1e-c5dc66cd86dd">


## Getting Started

Spring Boot를 활용한 REST API 개발에 대한 저의 생각을 보일러 플레이트화 하여 공유하는 레포지토리 입니다.

시작하기 전에는 OpenJDK 17이 설치되어 있어야 합니다.

API 패키지에 있는 예제들은 필요에 따라 삭제하셔도 됩니다.

- Swagger: http://127.0.0.1:8080/swagger-ui/index.html
- Health Check: http://127.0.0.1:8080/actuator/health
- H2 Console: http://127.0.0.1:8080/h2-console

## Convention

1. SuccessResponseDto를 제외한 모든 DTO는 가독성과 불변성을 위해 Builder 패턴을 적용한다.
2. global 패키지는 다른 패키지를 참조하지 않는다.
3. 네이밍은 명확하다면 길어도 문제 없다.
4. Internal DTO는 클라이언트에게 전송하기 전에 Response DTO로 변환되어야 한다.
5. 테스트 코드는 Given, When, Then 컨벤션으로 성공, 실패 케이스를 작성한다.
6. 특정 API 에 종속적인 예외는 ExceptionType 구현하여 해당 API 패키지 아래에 선언한다.
7. 모든 API 응답은 SuccessResponseDto 통해 응답하여야 한다.
8. 모든 엔티티는 BaseEntity 를 상속 받아야 한다.
9. 애플리케이션에서 발생하는 모든 예외는 CommonException 에서 관리한다.
