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
