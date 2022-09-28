# Test-Driven-Development

테스트 주도 개발에 통해 간단한 API를 직접 구현하며 공부한 레포지토리입니다.

<br/>

## 개발 환경

- Spring Boot 2.7.4
- Gradle 7.5
- Java 11 openJDK
- H2 Database

## Dependencies

- Spring Web
  - implementation 'org.springframework.boot:spring-boot-starter-web'
- Spring Data JPA
  - implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
- Gson
  - implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.7'
- Validation
  - implementation 'org.springframework.boot:spring-boot-starter-validation:2.6.3'
- Lombok

<br/>

## 요구사항

### 기능 요구 사항

- 멤버십 연결하기, 나의 멤버십 조회, 멤버십 연결끊기, 포인트 적립 API 를 구현합니다.
- 사용자 식별값은 문자열 형태이며 "X-USER-ID" 라는 HTTP Header 로 전달되며, 이 값은 포인트 적립할 때 바코드 대신 사용됩니다.
- Content-type 응답 형태는 application/json(JSON) 형식을 사용합니다.
- 각 기능 및 제약사항에 대한 개발을 TDD, 단위테스트를 기반으로 진행해야 합니다.

### 상세 기술 구현 사항

- 멤버십 등록 API
  - 기능: 나의 멤버십을 등록합니다.
  - 요청: 사용자 식별 값, 멤버십 이름, 포인트
  - 응답: 멤버십 ID, 멤버십 이름
  

- 멤버십 전체 조회 API
  - 기능: 내가 가진 모든 멤버십을 조회합니다.
  - 요청: 사용자 식별 값
  - 응답: {멤버십 ID, 멤버십 이름, 포인트, 가입 일시}의 멤버십 리스트


- 멤버십 상세 조회 API
  - 기능: 나의 1개 멤버십을 상세조회합니다.
  - 요청: 사용자 식별 값, 멤버십 ID
  - 응답: 멤버십 ID, 멤버십 이름, 포인트, 가입 일시


- 멤버십 삭제 API
  - 기능: 나의 멤버십을 삭제합니다.
  - 요청: 사용자 식별 값, 멤버십 번호
  - 응답: X


- 멤버십 포인트 적립 API
  - 기능: 나의 멤버십 포인트를 결제 금액의 1%만큼 적립합니다.
  - 요청: 사용자 식별 값, 멤버십 ID, 사용 금액을 입력 값으로 받습니다.
  - 응답: X

<br/>
<br/>

## REST API 명세서

- Membership
  - GET : /api/v1/memberships
    - 멤버십 리스트 조회
  - GET : /api/v1/memberships/{id}
    - 멤버십 상세 조회
  - POST : /api/v1/memberships
    - 멤버십 등록
  - PUT : /api/v1/memberships/{id}/accumulate
    - 멤버십 적립
  - DELETE : /api/v1/memberships/{id}
    - 멤버십 삭제

<br/>
<br/>
