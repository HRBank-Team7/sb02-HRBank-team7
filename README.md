# Team7을 소개합니다 🙌🏻

## Team7 문서
[팀 협업 문서 링크](https://github.com/HRBank-Team7/sb02-HRBank-team7/wiki)

---

## 🧑‍🤝‍🧑 팀원 구성


강지훈: [개인 Github 링크](https://github.com/homeA90)


김지협: [개인 Github 링크](https://github.com/QuokkaJoa)


심민혁: [개인 Github 링크](https://github.com/minhyuksim)


정종현: [개인 Github 링크](https://github.com/JJHyunDev)


한상엽: [개인 Github 링크](https://github.com/sangyeobhan)

---

## 🚀 프로젝트 소개

프로젝트명: HRBANK

부제 : Batch로 데이터를 관리하는 Open EMS

소개 : 기업의 인적 자원을 안전하게 관리하는 서비스

기간: 2025.04.21 ~ 2024.04.28

---

## ⚙️ 기술 스택 및 협업 도구

#### Backend
* **프레임워크:** Spring Boot
* **Spring 기술:** Spring Data JPA, Criteria Api
* **매핑 라이브러리:** MapStruct
* **데이터베이스:** PostgreSQL
* **API 문서화:** Swagger

#### 협업 도구
* Discord
* GitHub

#### 일정 관리
* GitHub Projects
  
---

## ⚙️ 팀원별 구현 기능 상세

### 👨‍💻강지훈/정종현 공동구현

직원 정보 생성 api:
- `MapStruct`를 이용한 toEntity, toDto 변환 mapper 구현
- `@RequestPart`를 이용해 `file + body` 형식의 form-data 를 받아 오는 RESTful API 구현
- 직원 생성 정보를 받아올때 JPA를 활용

직원 정보 삭제 api:
- `@PathVariable`을 이용해 id 별로 삭제가 가능한 동적 라우팅 구현

직원 정보 갱신 api:
- PATCH 요청으로 요청값을 `@RequestPart + @PathVariable` 사용하여 동적인 form-data 제어 기능 추가

직원 수 추이 조회:
- GET 요청으로 클라이언트의 대시보드 기능 활용을 위한 RESTful API 구현

직원 분포 조회 구현:
- GET 요청으로 클라이언트의 대시보드 기능 활용을 위한 RESTful API 구현

직원 수 조회:
- GET 요청으로 클라이언트 대시보드 기능 활용을 위한 RESTful API 구현

직원 조회(목록/상세) 기능 구현:
- `CriteriaQuery`를 이용하여 동적 Query구현
- `@PathVariable을` 이용해 id 별로 직원 조회 기능 구현


### 💡김지협

데이터 백업 기능 생성 api:

- DDD 아키텍쳐를 이용해 도메인이 본인의 상태에 책임을 가질 수 있게 구현
- POST 요청 / `@HttpServletRequest`으로 Header의 ipAddress 를 이용한 데이터 백업 기능 생성 기능 구현
- `MapStruct`를 이용한 toEntity, toDto 변환 mapper 구현
- 기존 FileCreate 기능을 활용해 csv, log 확장자 파일로 변환하여 파일을 생성하는 Generator 기능 구현

데이터 백업 이력 조회

- GET 요청 / 페이징에 관련된 DTO 필드들을 받아오기 위해 `@MobdelAttribute`를 사용, Cursor 페이지네이션 기능의 전체 백업 이력 조회 기능 구현
- `Criteria API + Curosr Paging` 기능을 사용해 복잡한 조건의 조회 정보를 객체에 흐름에 맞게 페이징 하여 가져옴
- `Lazy Loading + fetch` 전략을 활용해 조회 시 조회 성능 향상 및 N+1 문제 해결
- 1차적으로 `Stream + fetch_size` 전략을 이용해 OOM 방지 → 직원 수에 따라 `BufferedWriter + ByteArrayOutputStream` or `FileWriter` 도입 고려

데이터 백업 최신 이력 조회

- GET 요청 / `@RequestParam` 을 이용해서 쿼리스트링된 status 값을 받아온 뒤 최근 완료된 백업 내역 조회 기능 구현

### 🌷심민혁/한상엽 공동구현

파일 다운로드 api:
- DB 성능을 위한 실제 파일 저장과 메타 데이터 저장 분리
- DB의 File 테이블의 id값으로 바이너리 데이터를 save 하는 기능 구현
- `MapStruct`를 이용한 toEntity, toDto 변환 mapper 구현

부서 정보 조회 api:
- `@PathVariable`을 활용한 단건 조회 기능 구현
 
부서 정보 갱신 api:
- PATCH 요청으로 요청값을 `@PathVariable + @RequestBody`을 사용하여 데이터 처리 기능 구현

부서 정보 삭제 api:
- `@PathVariable`을 이용해 id 별로 삭제가 가능한 동적 라우팅 구현


### 🍦정종현/한상엽 공동구현

직원 정보 수정 이력 목록 조회 api

- `Criteria API`를 사용하여 ipAddress, at등의 정렬 필드와 정렬 방향으로 정렬할 수 있는 커서 기반 페이지 목록 조회 구현
- `Condition DTO`를 사용해 함수 시그니처 간소화

직원 정보 수정 이력 상세 조회 api

- `@PathVariable`을 이용해 id 별 생성, 수정, 삭제 이력 조회 기능 구현

수정 이력 건수 조회 api

- fromDate, toDate 요청으로 해당 기간 동안 수정 이력 건수를 조회하는 기능 구현
