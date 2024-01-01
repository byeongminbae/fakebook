# Fakebook(Working in progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting Started

시작하기 전, OpenJDK 17이 설치되어 있어야 합니다.

- Swagger: http://127.0.0.1:8080/swagger-ui/index.html
- Health Check: http://127.0.0.1:8080/actuator/health
- H2 Console: http://127.0.0.1:8080/h2-console

## Development

### ToDo

- Feature
    1. 웹소켓 채팅
    2. 유저 회원가입, 로그인, 정보 수정, 비밀번호 재설정, 계정 삭제
    3. 피드 CRUD, 피드 무한 스크롤(커서), 피드 대한 좋아요, 댓글
    4. 친구 관리(친구 요청, 수락, 거절 기능)
    5. 메인 페이지, 검색(유저, 피드) 최근 올라온 피드(커서)
- Technical
    - 예외 Common 에 뭉탱이로 두지 말고, Common, Database 이런식으로 쪼개기
    - common 패키지에 공통으로 사용되는 클래스 묶기
    - 클래스 이름에서 “엔티티” 단어 지우기. 유저는 멤버로 변경
    - Base 에 deletedAt 추가. 모든 삭제는 논리삭제로 변경
    - 아이디 기반으로 조회하지만 논리삭제는 제외하고 가져오는 메서드 인터페이스에 정의

### Endpoints

- Member
    - GET /members/{memberId} : 멤버 조회
    - POST /members/{memberId}: 멤버 생성
    - DELETE /members/{memberId}: 멤버 삭제
    - PATCH /members/{memberId}: 멤버 정보 업데이트
    - PUT /members/{memberId}/password: 멤버 패스워드 업데이트
    - GET /members/{memberId}/friends: 친구 리스트
    - POST /members/{memberId}/friends: 친구 요청
- Chat
    - GET /channels: 채널 목록 조회
    - POST /channels: 채널 생성
    - DELETE /channels/{channelId}: 채널 삭제
    - GET /channels/{channelId}/chat: 이전 채팅 불러오기 
    - POST /channels/{channelId}/members/{memberId}: 채널 참여
    - DELETE /channels/{channelId}/members/{memberId}: 채널 나가기
- Feed
    - GET /feeds: 모든 피드 가져오기
    - GET  /feeds/{feedId}: 피드 가져오기
    - POST /feeds/{feedId}: 피드 생성
    - PATCH /feeds/{feedId}: 피드 수정
    - DELETE /feeds/{feedId}: 피드 삭제
    - POST /feeds/{feedId}/reaction: 피드 리엑션 추가
    - POST /feeds/{feedId}/media: 피드 미디어(이미지, 동영상) 추가
    - DELETE /feeds/{feedId}/reaction: 피드 리엑션 삭제
    - GET /feeds/{feedId}/comments: 댓글 전체 조회
    - GET /feeds/{feedId}/comments/{commentId}: 댓글 조회
    - POST /feeds/{feedId}/comments/{commentId}/media: 댓글 미디어(이미지, 동영상) 추가
    - POST /feeds/{feedId}/comments: 댓글 추가
    - DELETE /feeds/{feedId}/comments: 댓글 삭제
    - POST /feeds/{feedId}/comments/{commentId}/reaction: 피드 리엑션 추가
- Media
    - POST /media: 이미지 동영상 업로드. 업로드 후 나온 Id 사용

### ERD

```mermaid
erDiagram

Member ||--o{ Friend : OneToMany
Member ||--o{ Feed : OneToMany
Member ||--o{ FeedComment : OneToMany
Member ||--o{ MemberChannel :OneToMany
Member ||--o{ Chat : OneToMany
Member ||--o{ FeedCommentReaction : OneToMany
Member ||--o{ FeedReaction : OneToMany
Member ||--o{ Media : OneToMany
FeedComment ||--o{ Media: OneToMany
FeedComment ||--o{ FeedCommentReaction : OneToMany
Channel ||--o{ MemberChannel :OneToMany
Feed ||--o{ FeedComment : OneToMany 
Feed ||--o{ FeedReaction : OneToMany
Feed ||--o{ Media: OneToMany
Channel ||--o{ Chat : OneToMany

Member{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	String signId
	String signPassword
	LocalDateTime lastSignInAt
	String email
	String phoneNumber
	LocalDateTime blacklistedAt
}

Friend{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	%% PENDING, FRIEND, REJECTED
	Enum status
	Member memberFrom FK
	Member memberTo FK
}

Feed{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	Member author FK
	String content
}

FeedComment{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	Feed feed FK
	Member author FK 
	String content
}

MemberChannel{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	Member member FK
	Channel channel FK
}

Channel{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	String title
	
}

Chat{
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	Member author FK
	Channel channel FK
	String content
}

FeedReaction {
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	Feed feed FK
	Member member FK
	%% LIKE, SAD, FUNNY, SLEEPY
	Enum reaction
}

FeedCommentReaction {
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	FeedComment feedComment FK
	Member member FK 
	%% LIKE, SAD, FUNNY, SLEEPY
	Enum reaction
}

Media {
	Long id PK
	LocalDateTime createdAt
	LocalDateTime updatedAt
	LocalDateTime deletedAt
	String contentUrl
	Member uploader FK
	Member member FK
	Feed feed FK
	%% IMAGE, VIDEO
	Enum mediaType
}
```

## 웹소켓 채팅 기능

### 전제 조건

엑세스 토큰 만료시, 프론트에서 엑세스 토큰 재발급 받아 재연결

해시맵에 채널 아이디(key), 웹소켓 세션 리스트(value) 로 채널 구독자를 등록한다.(타인에게 실시간 메시지 전송할때 사용)

해시맵의 데이터가 날아가는 것과 로드밸런서 적용시 공유가 안되는 이슈가 걱정된다면 redis 를 사용해 세션 관리를 하도록 하자.

해시맵에 등록된건 실시간 메시지를 받기 위함이고, 클라이언트 문제로 소켓이 종료된 경우 실시간 메시지 기능은 제 역할을 못하는 것이 맞다

채팅방에서 나가는것과 무관하다. chat 받아오고 소켓 연결만 다시 해주면 된다.

- GET /channels/{channelId}/chat
    1. 일단은 페이지네이션 적용하지 말고 구현
    2. memberId 등록 체크후 채팅방의 모든 메시지 불러오기
    3. send_message 할때 다른사람에게 전송되는 DTO 랑 동일
- POST /channels/{channelId}/members/{memberId}
    1. 해시맵 구독자 리스트에 등록한다.
    2. DB 참여자 리스트에 등록한다.
- DELETE /channels/{channelId}/members/{memberId}
    1. 해시맵 구독자 리스트에서 제거
    2. DB 참여자 리스트에서 제거
- websocket /chat
    1. 채널 아이디, 메시지 전송
    2. 세션에 담긴 엑세스토큰에 있는 memberId 가 DB 참여자 리스트에 등록되어 있는지 체크
    3. DB 에 메시지 남기기
    4. 해시맵 구독자 리스트 foreach 돌면서 메시지 전달(본인 제외)

## Convention

1. SuccessResponseDto를 제외한 모든 DTO는 가독성과 불변성을 위해 Builder 패턴을 적용한다.
2. global 패키지는 다른 패키지를 참조하지 않는다.
3. 네이밍은 명확하다면 길어도 문제 없다.
4. Internal DTO는 클라이언트에게 전송하기 전에 Response DTO로 변환되어야 한다.
5. 테스트 코드는 Given, When, Then 컨벤션으로 성공, 실패 케이스를 작성한다.
7. 모든 엔티티는 BaseEntity 를 상속 받아야 한다.
