# Fakebook(Working in progress)

I wrote this in Korean as I'm currently short on time. If I have the time later, I'll rewrite it in English.

## Getting Started

시작하기 전, OpenJDK 17이 설치되어 있어야 합니다.

- Swagger: http://127.0.0.1:8080/swagger-ui/index.html
- Health Check: http://127.0.0.1:8080/actuator/health
- H2 Console: http://127.0.0.1:8080/h2-console

## Feature ToDo

1. 메시징
    - 웹소켓 채팅 기능
2. 유저
    - crud(회원가입, 로그인, 정보 수정, 비밀번호 재설정, 계정 삭제)
3. 게시물 관리
    - 게시물 CRUD
    - 게시물 무한 스크롤(커서)
    - 게시물에 대한 좋아요, 댓글
4. 친구 및 팔로우 관리
    - 친구 목록 관리 및 친구 요청/수락/거절 기능
5. 메인 페이지, 검색
    - 사용자, 게시물 검색 기능
    - 인기있는 게시물 또는 트렌딩 항목 표시 기능(커서)

## Technical ToDo

- 예외 Common 에 뭉탱이로 두지 말고, Common, Database 이런식으로 쪼개기

## 웹소켓 채팅 기능

### 전제 조건

엑세스 토큰 만료시, 프론트에서 엑세스 토큰 재발급 받아 재연결

해시맵에 채널 아이디(key), 웹소켓 세션 리스트(value) 로 채널 구독자를 등록한다.(타인에게 실시간 메시지 전송할때 사용)

해시맵의 데이터가 날아가는 것과 로드밸런서 적용시 공유가 안되는 이슈가 걱정된다면 redis 를 사용해 세션 관리를 하도록 하자.

해시맵에 등록된건 실시간 메시지를 받기 위함이고, 이 리스트에서 제외되었다면(클라이언트 문제로 소켓이 종료된 경우) 실시간 메시지 기능은 제 역할을 못하는 것이 맞다.(채팅방에서 나가는것과 무관.)

로그아웃(리프레시 토큰 삭제) 가 아닌 이상 다시 접속가능하니 문제될것은 없다.

- GET /channels/{channelId}/chat
    1. 일단은 페이지네이션 적용하지 말고 구현
    2. 채팅방의 모든 메시지 불러오기
    3. send_message 할때 다른사람에게 전송되는 DTO 랑 동일해야함
    4. 메시지 엔티티: chatId, channelId, createdAt, updatedAt, message, isDeleted, deletedAt, sender  
- POST /channels/{channelId}/subscribe
    1. 해시맵 구독자 리스트에 등록한다.
    2. DB 참여자 리스트에 등록한다. 유저 - 다대다 중간테이블 - 채팅방 
    3. 채널 엔티티: channelId, createdAt, updatedAt, isDeleted, deletedAt
    4. 메시지 엔티티와의 관계는 일대다
- DELETE /channel/{channelId}/subscribe
    1. 해시맵 구독자 리스트에서 제거
    2. DB 참여자 리스트에서 제거
- websocket /chat
    1. 엑세스토큰에 있는 userId 가 DB 참여자 리스트에 등록되어있다면 아래 시퀀스 실행
    2. 채널 아이디, 메시지 전송
    3. DB 에 메시지 남기기
    4. 해시맵 구독자 리스트 foreach 돌면서 메시지 전달(본인 제외)

## Convention

1. SuccessResponseDto를 제외한 모든 DTO는 가독성과 불변성을 위해 Builder 패턴을 적용한다.
2. global 패키지는 다른 패키지를 참조하지 않는다.
3. 네이밍은 명확하다면 길어도 문제 없다.
4. Internal DTO는 클라이언트에게 전송하기 전에 Response DTO로 변환되어야 한다.
5. 테스트 코드는 Given, When, Then 컨벤션으로 성공, 실패 케이스를 작성한다.
6. 모든 API 응답은 봉투 패턴을 따른다.
7. 모든 엔티티는 BaseEntity 를 상속 받아야 한다.
