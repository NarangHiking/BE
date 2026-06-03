.
# 개발일지 - 2026년 6월 2일

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
