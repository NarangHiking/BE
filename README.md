# 나랑등산 BE 🥾

## 팀원별 기여 내역

### 👤 황우찬 (cmy397264)

#### 🔐 인증/인가
- JWT 토큰 기반 로그인·로그아웃·회원가입 구현
- accessToken + refreshToken 이중 토큰 구조 설계
- 토큰 저장 방식을 LocalStorage → HttpOnly Cookie로 전환
- Spring Security 설정 및 권한별 API 접근 제어
- 관리자(ADMIN) 권한 확대 (게시글·댓글 수정/삭제 권한)
- 비밀번호 재설정 기능 구현

#### 🏔 산/코스 관리
- 산(Mountain) CRUD API 및 MyBatis 매퍼 구현
- 코스(Track) API, GPX 일괄 등록 기능
- GPX 파일 미존재 시 무시 후 삭제 처리
- DTO 설계 및 schema 설계/수정

#### ☁️ 인프라/스토리지
- Cloudflare R2 이미지 스토리지 연동
- CORS 설정 및 프론트엔드 배포 도메인 추가
- 모든 API 경로에 `/api` prefix 추가

#### ⭐ 즐겨찾기/추천
- 코스 즐겨찾기 CRUD (POST/DELETE/GET `/favorite/{trackId}`)
- 코스 추천 CRUD (POST/DELETE/GET `/recommend/{trackId}`)

#### 🌤 날씨 API
- 기상청 단기예보 API 연동
- 위경도 → 격자좌표 변환(LCC) 구현으로 DB 기반 날씨 조회

#### 👤 사용자
- 회원가입, 유저 조회, 정보 수정 API
- `UserResponse` DTO 분리 (비밀번호 노출 방지)

#### 🧪 테스트
- 단위 테스트(AuthService, TokenService) / 통합 테스트 분리
- FavoriteController, Mountain, User 테스트 작성

---

### 👤 박승진 (jamonda1)

#### 📝 게시판
- 게시글 CRUD (생성, 조회, 수정, 삭제) 전체 구현
- 게시글 삭제 방식 Hard delete → Soft delete(논리적 삭제) 전환
- 게시글 이미지 첨부, 키워드·카테고리 기반 동적 검색 쿼리
- 게시글 댓글 CRUD (작성, 수정, 삭제) 구현

#### 🥾 코스 후기
- 트랙(코스)별 후기 CRUD 구현 (이미지 첨부 포함)
- 401 유효하지 않은 토큰 에러 처리

#### 🤖 AI 챗봇 (RAG)
- 사용자 메시지 임베딩 → Vector DB 유사도 검색 로직 구현
- LLM 체이닝 로직 설계 및 구현
- 챗봇 Controller/Service 완성, 프롬프트 최적화
- 사용자가 조회 중인 산 이름 컨텍스트 활용

#### ⚙️ 인프라/운영
- GitHub Actions CI/CD 파이프라인 설정
- AWS RDS 세팅
- 논리적 삭제 데이터 물리적 삭제 스케줄러 구현 (10분 주기)
- `ApiResult` 공통 응답 포맷 + `GlobalExceptionHandler` 적용

---

# 개발일지

## 2026-06-23

### 🌤 날씨 API 개선 — DB 위경도 기반 조회 (황우찬)
- 하드코딩된 `MtnGrid` enum 제거, DB의 lat/lng → 기상청 격자좌표(LCC) 실시간 변환으로 교체
- `WeatherController` 파라미터를 `mtnName` → `lat`, `lng`로 변경
- `fcstDate` 필터 제거 → 단기예보 최대 3일치 데이터 전체 반환

### 🔐 게시판 권한 제어 강화 (황우찬)
- 게시판 목록/상세 조회는 비로그인 허용, 글쓰기/수정/삭제는 로그인 필수
- `SecurityConfig`에서 GET `/api/board`, `/api/board/{id}` permitAll, 나머지는 authenticated
- 관리자(ADMIN) 게시글 수정 권한 추가 (`update` 메서드에 `isAdmin()` 우회)

### 🔑 비밀번호 재설정 및 관리자 권한 확대 (황우찬)
- 비밀번호 재설정 엔드포인트 추가
- 관리자 게시글/댓글 삭제 권한 부여

### 🤖 LLM 체이닝 로직 완성 (박승진)
- RAG 챗봇 LLM 체이닝 로직 설계 및 구현 완료
- 사용자가 조회 중인 산 이름을 챗봇 컨텍스트에 활용
- boardService 수정 및 버그 픽스

### ⏰ 스케줄러 삭제 빈도 변경 (박승진)
- 논리적 삭제 데이터의 물리적 삭제 스케줄러 주기를 10분으로 조정

---

## 2026-06-22

### 🏔 산/코스 DTO 및 서비스 수정 (황우찬)
- Mountain DTO에 lat/lng 필드 추가, 서비스 로직 수정
- properties 설정 수정 (DB 연결, 환경변수)
- GPX 파일 미존재 시 무시 후 삭제 처리
- schema/data.sql 수정, 추천 수 카운트 버그 수정

### 🌐 CORS 설정 (황우찬)
- 프론트엔드 배포 도메인(Vercel) CORS 허용 추가
- CORS 주소 관련 버그 수정

### 🤖 챗봇 버그 수정 (박승진)
- 챗봇 관련 다수 버그 수정

---

## 2026-06-21

### ☁️ R2 스토리지 전환 및 GPX 일괄등록 (황우찬)
- `FileService` 삭제 → `R2Service`로 전면 교체
- 코스 삭제 시 댓글 FK 제약 문제 해결 (schema CASCADE 설정)
- Track 게시판 수정, GPX 일괄등록 기능 추가

### 🤖 AI 챗봇 기능 완성 (박승진)
- 챗봇 기능 최초 완성, 프롬프트 보완 예정
- GitHub Actions CI/CD 파이프라인 설정 완료
- properties 환경변수 정리

---

## 2026-06-18 ~ 20

### 🤖 챗봇 핵심 로직 구현 (박승진)
- 사용자 메시지 임베딩 로직 구현
- Vector DB 유사도 검색 로직 구현
- 챗봇 전체 로직 구현 완료 (프롬프트 보완 필요)

---

## 2026-06-15 ~ 17

### ☁️ R2 연동 (황우찬)
- Cloudflare R2 스토리지 연동 및 로컬 테스트 확인

### 🤖 챗봇 서비스 개발 시작 (박승진)
- 챗봇 Controller 완성
- 환경변수 세팅 (OpenAI API, Vector DB)

---

## 2026-06-14

### 🗄 RDS 세팅 완료 (박승진)
- AWS RDS MySQL 연동 설정 완료

---

## 2026-06-12

### 🔄 토큰 저장 방식 전환 (황우찬)
- accessToken/refreshToken 저장을 LocalStorage → HttpOnly Cookie로 변경
- 보안 강화 (XSS 공격 방어)

### 🗑 논리적 삭제 → 물리적 삭제 스케줄러 (박승진)
- Soft delete된 데이터를 주기적으로 물리적 삭제하는 스케줄러 구현

---

## 2026-06-09 ~ 10

### 🥾 코스 후기(Track Comment) CRUD (박승진)
- 코스별 후기 전체 조회, 작성, 수정, 삭제 구현
- 이미지 첨부 기능 포함
- 401 유효하지 않은 토큰 에러 처리 추가

---

## 2026-06-07

### 🔗 API 경로 통일 (황우찬)
- 모든 API 경로에 `/api` prefix 추가
- Remote API 호출 설정 추가

---

## 2026-06-05

### 💬 게시글 댓글 CRUD 완성 (박승진)
- 게시글 댓글 작성, 수정, 삭제 로직 구현 및 테스트 완료
- 논리적 삭제(Soft delete) 적용
- 게시글 작성/수정/삭제 로직 보완

### 🧪 User 테스트 추가 (황우찬)
- User 관련 테스트 코드 작성
- 반환 타입 수정

---

## 2026-06-04

### ⭐ 즐겨찾기(Favorite) 기능 구현
- 즐겨찾기 추가(`POST /favorite/{trackId}`), 삭제(`DELETE /favorite/{trackId}`),
  존재 여부 확인(`GET /favorite/{trackId}`), 즐겨찾기한 경로 목록 조회(`GET /favorite/track`) 구현
- JWT 토큰 기반으로 `@AuthenticationPrincipal`에서 userId 추출
- MyBatis 매퍼(`FavoriteDao`)로 insert / delete / isExist / selectFavorites 쿼리 작성
- 목록 조회 시 `tracks`와 `favorites` 조인, 서브쿼리로 `recommend_cnt`(추천 수) 함께 조회

### 🧪 FavoriteController 테스트 작성
- `@Order`로 insert → delete → 목록 조회 순서 제어
- JWT 토큰 발급 후 `Authorization` 헤더로 인증 요청 검증
- 테스트 작성 중 발견한 이슈 정리:
  - `DataIntegrityViolationException`을 일괄 409(중복)로 처리하던 부분이,
    실제로는 FK 제약 위반 등 다른 무결성 오류도 같은 메시지로 응답하던 문제 인지
  - 목록 조회 테스트에서 GET 매핑 엔드포인트를 DELETE로 호출하던 실수 수정

## 2026-06-02
## 작업 내용

### 🐛 버그 수정 - accessToken 파싱 오류 (황우찬)
- **PR #20** | `auth` 브랜치
- accessToken 값 앞에 `'bearer'` 문자열이 중복으로 포함되는 버그 수정
- JWT 필터 또는 토큰 생성 로직에서 prefix가 이중으로 붙던 문제 해결

---

### ♻️ 게시글 삭제 방식 변경 - Hard delete → Soft delete (jamonda1)
- **PR #21** | `feat/board` 브랜치
- 게시글 삭제 시 DB에서 즉시 제거(Hard delete)하던 방식을
  `deleted_at` 등의 플래그를 활용한 논리 삭제(Soft delete)로 전환
- 데이터 복구 가능성 및 감사(audit) 추적 대응

---

### ✅ 게시글 수정 로직 완성 (jamonda1)
- **PR #22** | `feat/board` 브랜치
- 게시글 수정 API 구현 완료
- 수정 대상 검증 및 업데이트 처리 로직 포함

---

## 2026-06-01

### 사용자 추가, 조회, 수정 구현
- 회원가입(`POST /user`), 유저 조회(`GET /user`), 정보 수정(`PATCH /user`) 구현
- JWT 토큰 기반으로 `@AuthenticationPrincipal`에서 userId 추출
- `UserResponse` DTO 분리로 비밀번호 응답 노출 방지
- 더미데이터 BCrypt 암호화 적용

### 테스트 분리 완료
- 단위 테스트 (`AuthServiceTest`, `TokenServiceTest`) / 통합 테스트 (`AuthServiceIntegrationTest`) 분리
- Mock 대상을 `UserDao` → `UserService`로 수정
- `logout`, `reissue` 테스트 케이스 추가

### remove 도메인 고민
- 회원 탈퇴 시 토큰 처리 책임 위치 검토
- `UserController → AuthService` 순환 의존성 문제 확인
- `UserService → TokenService` 주입 방식으로 결정

---

## 2026-05-27

### ApiResult 적용 및 GlobalExceptionHandler 적용
- 공통 응답 포맷 `ApiResult<T>` 전체 적용
- `GlobalExceptionHandler`로 예외 처리 일원화
- `UserNotFoundException`, `BadCredentialsException` 등 401 응답 처리

---

## 2026-05-25

### 단위 테스트 / 통합 테스트 분리 예정
- 테스트 전략 수립: Testing Pyramid 기반
- 단위 테스트: 순수 로직 검증 (Mock 사용)
- 통합 테스트: 실제 DB, Redis 연동 검증

### 데이터베이스 연결 및 Redis 연동 테스트
- MySQL 연결 확인
- Redis refresh token 저장/조회/삭제 동작 확인

---

## 2026-05-24

### 게시글 수정 로직 진행
- 게시글 수정 기능 구현 중
- 일부 코드 리팩토링 진행

### 게시글 전체 조회 로직 수정
- 기존 전체 조회에서 `keyword`, `category` 쿼리 파라미터 기반 조회로 변경
- 값이 있을 때만 조건 적용하는 동적 쿼리 구현

---

## 2026-05-22

### 게시글 CRUD 구현
- 게시글 생성 구현 완료
- 게시글 단건 조회 기능 완료
- `ApiResult` 공통 응답 포맷 추가
- schemas 폴더에 SQL 파일 추가
