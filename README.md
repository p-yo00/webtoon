## 웹툰 서비스
작가에게 후원 댓글을 보낼 수 있는 웹툰 api 서비스입니다.

## 사용 기술
<div align=center>
<img src="https://img.shields.io/badge/Spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Elastic Search-005571?style=for-the-badge&logo=elasticsearch&logoColor=white"> <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> 
</div>

## ERD
<img src="https://github.com/p-yo00/webtoon/blob/main/doc/img/erd.png">

## 프로젝트 기능

### [회원]

- 회원가입
    - [일반 사용자 / 작가 / 관리자] 권한으로 가입한다.
- 로그인
- 회원 탈퇴
- 회원 수정

### [웹툰 관리]

> 웹툰의 Create, Update, Delete는 '작가'에게 권한이 있다.

- 웹툰 등록
    - 관리자의 승인 이후 등록된다.
- 웹툰 정보 수정
- 웹툰 삭제
    - 삭제는 관리자만 가능하다.

### [에피소드 관리]

> 에피소드는 웹툰 안에 있는 각 편을 뜻한다. (1편, 2편..)<br>
> 에피소드의 Create, Update, Delete는 '작가'에게 권한이 있다.

- 웹툰에 에피소드를 업로드
    - 업로드 시간을 정해서 해당 시간이 지나야 목록에 보이도록 한다.
- 에피소드 수정
- 에피소드 삭제
    - 삭제는 관리자만 가능하다.

### [웹툰 조회]

- 선택한 조건으로 정렬하여 조회한다.
   - 조건: 인기순(누적 조회수), 실시간 인기(당일 조회수), 찜순, 최근 업로드순
- 선택한 조건으로 분류하여 조회한다.
  - 조건: 장르별, 연재중, 완결
- 사용자가 선호하는 장르의 인기 웹툰을 추천

### [에피소드 조회]

- 특정 웹툰의 에피소드 목록 조회
- 에피소드 내용 조회
    - 에피소드는 이미지들로 구성
    - 연령 제한이 있는 에피소드는 성인인증이 된 사용자만 조회 가능

### [마이페이지]

- 최근 본 웹툰을 조회한다.
  - 쿠키 저장으로 현재 기기에서만 조회 가능
- 찜한 웹툰 조회

### [웹툰 검색]

- 키워드, 작가, 제목으로 검색
- 작가 및 웹툰 검색 시 자동 완성 제공

### [댓글]

- 댓글 작성
    - 각 에피소드마다 댓글을 작성할 수 있다.
- 댓글 삭제
    - 댓글 작성자와 댓글이 달린 웹툰의 작가에게 삭제 권한이 있다.
- 댓글 수정
- 댓글 추천
    - 추천수가 높은 상위 10개는 베스트 댓글 선정
- 포인트와 함께 후원 댓글 작성
    - 포인트는 30%의 수수료를 떼고 작가에게 포인트로 입금
    - 후원 금액 상위 3위 베스트 댓글 선정
    - 작가는 자신의 후원 댓글만 따로 조회 가능

### [포인트 관리]

- 포인트 충전
- 포인트 인출

### [알림]

- 사용자는 알림 설정한 웹툰의 업로드 알림을 받을 수 있다.
- 작가는 자신의 웹툰 후원 알림을 받을 수 있다.

<br>

## Trouble Shooting
[go to the trouble shooting section](doc/TROUBLE_SHOOTING.md)
