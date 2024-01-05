# Fakebook(Working in progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting Started

Before getting started, OpenJDK 17 must be installed.

- Swagger: http://127.0.0.1:8080/swagger-ui/index.html
- Health Check: http://127.0.0.1:8080/actuator/health
- H2 Console: http://127.0.0.1:8080/h2-console

## Features
- 실시간 웹 소켓 기반 채팅
    - 채널 목록 조회, 생성, 삭제
    - 이전 채팅 불러오기
    - 채널 참여, 나가기
- 토큰 기반 인증
    - Refresh Token 리스트 조회, 삭제
    - Refresh Token을 통한 Access Token 갱신
- 무한 스크롤 가능한 피드
    - 모든 피드 가져오기, 피드 가져오기
    - 피드 생성, 수정, 삭제
    - 이미지, 동영상 미디어 추가
    - 댓글 조회, 추가, 삭제
    - 댓글에 이미지, 동영상 미디어 추가
    - 피드, 댓글 반응(좋아요, 슬퍼요, 웃겨요, 졸려요) 추가, 삭제
- 하나의 엔드포인트로 규격화된 미디어(이미지, 동영상) 업로드
- 멤버
    - 멤버 조회, 생성, 삭제, 정보 업데이트
    - 참여한 채널 전체보기
    - 패스워드 업데이트
    - 친구 리스트, 추가, 삭제

## Development

### ERD

```mermaid
erDiagram

Member ||--o{ Friend : OneToMany
Member ||--o{ Feed : OneToMany
Member ||--o{ FeedComment : OneToMany
Member ||--o{ ChannelMember :OneToMany
Member ||--o{ Chat : OneToMany
Member ||--o{ FeedCommentReaction : OneToMany
Member ||--o{ FeedReaction : OneToMany
Member ||--o{ Media : OneToMany
FeedComment ||--o{ Media: OneToMany
FeedComment ||--o{ FeedCommentReaction : OneToMany
Channel ||--o{ ChannelMember :OneToMany
Member ||--o{ Channel :OneToMany
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

ChannelMember{
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
	Member creator FK
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
