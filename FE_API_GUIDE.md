# NarangHiking — 프론트엔드 연동 가이드

> 백엔드(Spring Boot) 코드를 기반으로 FE에서 필요한 정보를 정리한 문서입니다.
> 이 문서는 참고용 산출물이며, BE 소스 코드는 수정/삭제하지 않습니다.

---

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| Base URL (로컬) | `http://localhost:8080` |
| 공통 응답 포맷 | `ApiResult<T>` (아래 참고) |
| 인증 방식 | JWT (HttpOnly 쿠키 `accessToken` / `refreshToken`) |
| CORS 허용 Origin | `http://localhost:5173` (Vite 개발 서버) |
| 자격증명 | `withCredentials: true` **필수** (쿠키 전송) |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| API Docs(JSON) | `http://localhost:8080/v3/api-docs` |

### 인증/쿠키 동작
- 로그인 성공 시 서버가 `Set-Cookie`로 `accessToken`(15분), `refreshToken`(7일)을 내려줍니다. **둘 다 HttpOnly**라 JS에서 직접 읽을 수 없습니다.
- 모든 인증이 필요한 요청은 쿠키가 자동 전송되어야 하므로 FE는 `fetch(..., { credentials: 'include' })` 또는 `axios.defaults.withCredentials = true`를 반드시 설정해야 합니다.
- `accessToken` 만료(401) 시 `POST /api/auth/reissue`를 호출해 재발급 후 원래 요청을 재시도하는 인터셉터 구현을 권장합니다.
- `refreshToken` 쿠키는 path가 `/api/auth`로 제한되어 있습니다(로그아웃/재발급에서만 전송).
- 사용자 식별값(`userId`)은 서버가 토큰에서 추출하므로 FE가 body/param으로 보낼 필요가 없습니다.

---

## 2. 공통 응답 포맷 (`ApiResult<T>`)

모든 API는 아래 형태로 감싸서 반환됩니다.

```json
// 성공
{ "success": true, "error": null, "data": { /* T */ } }

// 실패
{ "success": false, "error": "에러 메시지", "data": null }
```

FE에서는 `res.data.success`로 분기하고, 실제 데이터는 `res.data.data`에서 꺼내 사용합니다.

---

## 3. 권한 정책 요약 (Spring Security)

| 구분 | 대상 |
|------|------|
| 인증 불필요(permitAll) | `POST /api/auth/login`, `POST /api/auth/reissue`, `POST /api/user`(회원가입), `GET /api/board/**`, Swagger |
| GET 공개 | `/api/mtn/**`, `/api/track/**`, `/api/weather/**`, `/api/sun/**` |
| 로그인 필요 | 위 외 나머지 모든 요청 (게시글/후기/댓글 작성·수정·삭제, 즐겨찾기, 추천, 내 정보 등) |
| 관리자(ROLE_ADMIN) 전용 | `GET /api/user/list`, `/api/mtn/**`·`/api/tracks/**`의 POST/PUT/PATCH/DELETE |

> 참고: 게시글 조회(`GET /api/board/**`)는 공개지만, 작성/수정/삭제는 인증이 필요합니다.

---

## 4. API 엔드포인트

### 4.1 인증 (`/api/auth`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | `/api/auth/login` | ❌ | 로그인 (쿠키 발급) |
| POST | `/api/auth/logout` | ✅ | 로그아웃 (쿠키 삭제 + 블랙리스트) |
| POST | `/api/auth/reissue` | 🍪 refreshToken | accessToken 재발급 |

**로그인 요청 body** (`LoginRequest`)
```json
{ "email": "user@test.com", "pass": "비밀번호" }
```
**로그인 응답 data**
```json
{ "userId": 1 }
```
- 실패: `401` — "사용자를 찾을 수 없습니다." / "비밀번호가 일치하지 않습니다."

**재발급**: body 없음. 브라우저가 `refreshToken` 쿠키 자동 전송. 성공 시 새 `accessToken` 쿠키 발급, `data: "ok"`.

---

### 4.2 사용자 (`/api/user`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | `/api/user` | ❌ | 회원가입 |
| GET | `/api/user` | ✅ | 내 정보 조회 |
| PATCH | `/api/user` | ✅ | 내 정보 수정 |
| PATCH | `/api/user/remove` | ✅ | 회원 탈퇴(논리 삭제) |
| GET | `/api/user/list` | ✅ ADMIN | 전체 회원 조회 |

**회원가입 body** (`SignUpRequest`)
```json
{ "email": "user@test.com", "pass": "비밀번호", "name": "홍길동" }
```
응답: `201 Created`, `data: "signup ok"`

**내 정보 조회 응답 data** (`UserResponse`)
```json
{ "email": "user@test.com", "name": "홍길동", "role": "USER", "createdAt": "2026-06-01T22:08:26" }
```

**내 정보 수정 body** (`UpdateRequest`) — 변경할 값만
```json
{ "pass": "새비밀번호", "name": "새이름" }
```

**탈퇴**: body 없음. 응답 `data: "ok"`.

---

### 4.3 산 (`/api/mtn`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/mtn/list` | ❌ | 산 전체 조회 |
| GET | `/api/mtn/{id}` | ❌ | 산 단건 조회 |
| GET | `/api/mtn/{mtnId}/track` | ❌ | 해당 산의 코스 목록 조회 |
| POST | `/api/mtn` | ✅ ADMIN | 산 등록 (multipart) |
| PUT | `/api/mtn/{id}` | ✅ ADMIN | 산 수정 (multipart) |
| DELETE | `/api/mtn/{id}` | ✅ ADMIN | 산 삭제 |

**Mtn 객체**
```json
{
  "id": 1,
  "name": "북한산",
  "location": "서울",
  "height": 836,
  "description": "설명",
  "originalFilename": "원본.jpg",
  "storedFilename": "저장된파일키"
}
```
- 등록/수정은 `multipart/form-data`: part `mtn`(JSON) + part `file`(이미지, 선택).

---

### 4.4 코스/트랙 (`/api/track`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/track/search?name=` | ❌ | 코스명 검색 |
| GET | `/api/track` | ❌ | 조건 검색 (mtnName, location, height) |
| GET | `/api/track/{id}` | ❌ | 코스 단건 조회 |
| POST | `/api/track` | ✅ ADMIN* | 코스 등록 (multipart, GPX) |
| PUT | `/api/track` | ✅ ADMIN* | 코스 수정 (multipart, GPX) |
| DELETE | `/api/track/{id}` | ✅ ADMIN* | 코스 삭제 |

> *Security 설정상 admin 경로는 `/api/tracks/**`로 적혀 있어 `/api/track`과 표기가 다릅니다. 실제 권한 적용 여부는 BE와 확인 필요.

**Track 객체**
```json
{
  "id": 1,
  "mountainId": 1,
  "name": "백운대 코스",
  "gpxFilePath": "gpx/1/저장된파일키",
  "recommendCnt": 12
}
```
- 조건 검색 쿼리 파라미터 (`TrackCondition`): `mtnName`, `location`, `height`
- 등록/수정 multipart: part `t`(Track JSON) + part `file`(GPX, 선택)
- 단건 조회 실패 시 `404`.

---

### 4.5 게시판 (`/api/board`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/board?keyword=&category=` | ❌ | 게시글 목록(검색/카테고리) |
| GET | `/api/board/{id}` | ❌ | 게시글 상세 |
| POST | `/api/board` | ✅ | 게시글 작성 (multipart) |
| PUT | `/api/board/{id}` | ✅ | 게시글 수정 (multipart) |
| DELETE | `/api/board/{id}` | ✅ | 게시글 삭제 |

**목록 응답 data** (`BoardListResponse[]`)
```json
[{
  "id": 1, "name": "김수한무", "title": "오늘의 등산 코스",
  "commentCount": 1, "category": "free",
  "image": "대표이미지키 또는 default_image.png",
  "createdAt": "2026년06월01일, 22시36분"
}]
```
- 검색 파라미터: `keyword`(제목/내용), `category`(`free` / `feedback`) — 모두 선택.
- 결과 없으면 빈 배열 반환.

**상세 응답 data** (`BoardDetailResponse`)
```json
{
  "id": 1, "userId": 2, "name": "김수한무", "trackId": 1,
  "title": "...", "content": "...", "category": "free",
  "commentCount": 1,
  "images": ["이미지키1", "이미지키2"],
  "comments": [ /* BoardCommentResponse[] */ ],
  "createdAt": "2026년06월01일, 22시36분"
}
```

**작성/수정 요청** (`multipart/form-data`)
- part `board` (JSON, `BoardRequest`):
```json
{ "title": "제목", "content": "내용", "category": "free", "trackId": null }
```
  - `title`, `content`, `category` 필수(NotBlank). `category`는 `free`(자유) / `feedback`(건의사항). `trackId`는 건의사항일 때만.
  - `userId`는 토큰에서 주입되므로 보낼 필요 없음.
- part `images` (작성 시) / `addedImages` (수정 시): 첨부 이미지 파일들 (선택)
- 수정 시 `deletedImages`: 삭제할 이미지 키 리스트 (query param `List<String>`)

작성 성공 `201`, 응답 data는 생성된 게시글의 `BoardDetailResponse`.

---

### 4.6 게시글 댓글 (`/api/board/{boardId}/comment`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | `/api/board/{boardId}/comment` | ✅ | 댓글 작성 |
| PATCH | `/api/board/{boardId}/comment/{commentId}` | ✅ | 댓글 수정 |
| DELETE | `/api/board/{boardId}/comment/{commentId}` | ✅ | 댓글 삭제(논리) |

**요청 body** (`BoardCommentRequest`)
```json
{ "content": "좋은 정보 감사합니다." }
```
**응답 data** (`BoardCommentResponse`)
```json
{ "id": 1, "userId": 1, "name": "김수한무", "content": "...", "createdAt": "2026-06-01T22:08:26" }
```

---

### 4.7 코스 후기 (`/track/{trackId}/comment`)

> ⚠️ 다른 API와 달리 prefix에 `/api`가 **없습니다**. (`/track/...`)

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| GET | `/track/{trackId}/comment` | ❌(GET 공개)* | 후기 전체 조회 |
| POST | `/track/{trackId}/comment` | ✅ | 후기 작성 (multipart) |
| PUT | `/track/{trackId}/comment/{commentId}` | ✅ | 후기 수정 (multipart) |
| DELETE | `/track/{trackId}/comment/{commentId}` | ✅ | 후기 삭제(논리) |

> *Security의 GET 공개 목록은 `/api/track/**` 기준이므로, `/track/...`(api 미포함) 경로는 인증이 필요할 수 있습니다. BE와 확인 권장.

**조회 응답 data** (`TrackCommentListResponse[]`)
```json
[{
  "id": 1, "userId": 1, "name": "홍길동", "content": "이 코스 좋네요",
  "createdAt": "2026년06월01일, 22시36분",
  "images": [{ "originalFilename": "원본.jpg", "storedFilename": "random.jpg" }]
}]
```

**작성/수정 요청** (`multipart/form-data`)
- part `comment` (JSON, `TrackCommentRequest`): `{ "content": "후기 내용" }` (content 필수)
- part `images`(작성) / `addedImages`(수정): 첨부 이미지 (선택)
- 수정 시 `deletedImages`: 삭제할 이미지 키 리스트 (query param)
- `userId`, `trackId`는 토큰/경로에서 주입.

---

### 4.8 즐겨찾기 (`/api/favorite`) — 인증 필요

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/favorite/{trackId}` | 즐겨찾기 추가 (중복 시 `409`) |
| DELETE | `/api/favorite/{trackId}` | 즐겨찾기 해제 (없으면 `404`) |
| GET | `/api/favorite/{trackId}` | 즐겨찾기 여부 (`data: true/false`) |
| GET | `/api/favorite/track` | 내 즐겨찾기 코스 목록 (`Track[]`) |

### 4.9 추천 (`/api/recommend`) — 인증 필요

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/recommend/{trackId}` | 코스 추천 (중복 시 `409` "이미 추천한 경로입니다.") |
| DELETE | `/api/recommend/{trackId}` | 추천 취소 (없으면 `404`) |
| GET | `/api/recommend/{trackId}` | 추천 여부 (`data: true/false`) |
| GET | `/api/recommend/track` | 내가 추천한 코스 목록 (`Track[]`) |

---

### 4.10 날씨 (`/api/weather`) — 공개

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/weather?mtnName=&fcstDate=` | 산 이름 기준 단기예보 |

- `mtnName` 필수, `fcstDate`(yyyyMMdd) 선택 — 생략 시 오늘.
- 응답 data (`WeatherResponse[]`)
```json
[{
  "fcstDate": "20260615", "fcstTime": "1200",
  "temperature": "기온(℃)",
  "sky": "1맑음 3구름많음 4흐림",
  "precipitationType": "0없음 1비 2비/눈 3눈 4소나기",
  "precipitationProbability": "강수확률(%)",
  "humidity": "습도(%)",
  "windSpeed": "풍속(m/s)"
}]
```

### 4.11 일출/일몰 (`/api/sun`) — 공개

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/sun?locdate=&location=` | 일출·일몰 시각 조회 |

- `locdate`, `location` 둘 다 필수. 잘못된 입력 시 `400`.
- 응답 data (`SuntimeResponse`) — 가공 후 `sunrise`, `sunset` 사용 가능. (원본 구조는 공공 API 응답 래핑 형태)

### 4.12 챗봇 (`/api/chatbot`)
- 현재 컨트롤러 골격만 존재(미구현). Redis(단기 기억) + Pinecone(Vector DB) + Spring AI + OpenAI 조합 예정. **FE 연동 불가 상태.**

---

## 5. FE 구현 시 체크리스트

1. **axios/fetch에 `withCredentials: true` (credentials: 'include') 전역 설정** — 안 하면 모든 인증 API가 401.
2. **응답 언래핑**: 항상 `res.data.data`에서 실제 값 사용, `res.data.success`로 성공 분기.
3. **401 인터셉터**: accessToken 만료 시 `/api/auth/reissue` 호출 → 성공하면 원요청 재시도, 실패하면 로그인 페이지로.
4. **multipart 요청**(게시글·후기·산·코스 작성/수정): JSON part는 `Blob`(`type: application/json`)으로, 파일은 별도 part로 전송. `Content-Type` 헤더는 직접 지정하지 말고 브라우저가 boundary 포함해 자동 설정하게 둘 것.
5. **이미지 표시**: 응답의 `storedFilename`/`image`는 Cloudflare R2 저장 키. 실제 표시용 URL 규칙(버킷 public URL prefix)은 BE에 확인 필요. 게시글 목록 대표 이미지가 없으면 `default_image.png`로 내려옴.
6. **날짜 포맷 혼재**: 게시글/후기 `createdAt`은 `"2026년06월01일, 22시36분"` 문자열, 댓글은 ISO(`2026-06-01T22:08:26`) 등 포맷이 섞여 있으니 파싱 주의.
7. **경로 prefix 주의**: 코스 후기는 `/track/...`로 `/api`가 빠져 있음. 그 외는 모두 `/api/...`.
8. **카테고리 값**: 게시판은 `free`(자유), `feedback`(건의사항) 두 종류.

---

## 6. 미확정/BE 확인 필요 항목

- 코스(`/api/track`) 쓰기 권한 경로 표기 불일치(`/api/track` vs Security의 `/api/tracks/**`).
- 코스 후기 경로(`/track/...`)의 실제 인증 정책(GET 공개 여부).
- R2 이미지 키 → 실제 접근 URL 변환 규칙.
- 챗봇 API 스펙(미구현).
