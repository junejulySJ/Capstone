## 📚 목차
- [Home](../README.md)
- **User API** [(Example)](UserAPIDetail.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md) [(Example)](MapAPIDetail.md)
- [Schedule API](ScheduleAPI.md) [(Example)](ScheduleAPIDetail.md)
- [Path API](PathAPI.md) [(Example)](PathAPIDetail.md)
- [Board API](BoardAPI.md) [(Example)](BoardAPIDetail.md)
- [Comment API](CommentAPI.md) [(Example)](CommentAPIDetail.md)
- [Group API](GroupAPI.md) [(Example)](GroupAPIDetail.md)
- [GroupBoard API](GroupBoardAPI.md) [(Example)](GroupBoardAPIDetail.md)
- [GroupComment API](GroupCommentAPI.md) [(Example)](GroupCommentAPIDetail.md)

# User API
## API 바로가기
| API 호출                               | 설명          |
|--------------------------------------|-------------|
| [POST /user/check-id](#아이디-중복-검사)    | 아이디 중복 검사   |
| [POST /user/register](#회원가입)         | 회원가입        |
| [GET /user](#회원-정보-조회)               | 회원 정보 조회    |
| [GET /user/list](#전체-회원-조회)          | 전체 회원 조회    |
| [PUT /user](#회원-정보-변경)               | 회원 정보 변경    |
| [GET /user/boards](#작성한-글-조회)        | 작성한 글 조회    |
| [GET /user/boards/liked](#좋아요한-글-조회) | 좋아요한 글 조회   |
| [GET /user/groups](#속한-그룹-조회)        | 속한 그룹 조회 ✏️ |
| [POST /user/password](#비밀번호-변경)      | 비밀번호 변경 ✏️  |
| [DELETE /user](#회원-탈퇴)               | 회원 탈퇴 ✏️    |

---

## API 목록

### 아이디 중복 검사

**POST** `/user/check-id`

> 사용자가 입력한 아이디가 이미 존재하는지 검사합니다.

## [Example](UserAPIDetail.md#아이디-중복-검사)

#### 요청 바디 파라미터
| 파라미터   | 설명    | 값 예시          |
|--------|-------|---------------|
| userId | 회원 ID | "exampleUser" |

---

### 회원가입

**POST** `/user/register`

> 신규 회원을 등록합니다.

## [Example](UserAPIDetail.md#회원가입)

#### 요청 바디 파라미터
| 파라미터        | 설명      | 값 예시                |
|-------------|---------|---------------------|
| userId      | 회원 ID   | "exampleUser"       |
| userEmail   | 회원 이메일  | "hong@example.com"  |
| userPasswd  | 회원 비밀번호 | "password123"       |
| userNick    | 회원 닉네임  | "홍길동"               |
| userAddress | 회원 주소   | "서울특별시 중구 세종대로 110" |

#### 응답 바디 파라미터
| 파라미터        | 설명     | 값 예시                |
|-------------|--------|---------------------|
| userId      | 회원 ID  | "exampleUser"       |
| userEmail   | 회원 이메일 | "hong@example.com"  |
| userNick    | 회원 닉네임 | "홍길동"               |
| userAddress | 회원 주소  | "서울특별시 중구 세종대로 110" |
| userType    | 회원 유형  | 1(일반 사용자)           |

---

### 회원 정보 조회

**GET** `/user`

> 회원 본인의 정보를 조회합니다.

## [Example](UserAPIDetail.md#회원-정보-조회)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 응답 바디 파라미터
| 파라미터        | 설명        | 값 예시                |
|-------------|-----------|---------------------|
| userId      | 회원 ID     | "exampleUser"       |
| userEmail   | 회원 이메일    | "hong@example.com"  |
| userNick    | 회원 닉네임    | "홍길동"               |
| userImg     | 회원 프로필 사진 | "hong.png"          |
| userAddress | 회원 주소     | "서울특별시 중구 세종대로 110" |
| userType    | 회원 유형     | 1(일반 사용자)           |

---

### 전체 회원 조회

**GET** `/user/list`

> 모든 회원 정보를 조회합니다.  
> 관리자 계정으로 로그인된 경우만 접근할 수 있습니다.

## [Example](UserAPIDetail.md#전체-회원-조회)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 응답 바디 파라미터
| 파라미터        | 설명        | 값 예시                |
|-------------|-----------|---------------------|
| userId      | 회원 ID     | "exampleUser"       |
| userEmail   | 회원 이메일    | "hong@example.com"  |
| userNick    | 회원 닉네임    | "홍길동"               |
| userImg     | 회원 프로필 사진 | "hong.png"          |
| userAddress | 회원 주소     | "서울특별시 중구 세종대로 110" |
| userType    | 회원 유형     | 1(일반 사용자)           |

---

### 회원 정보 변경

**PUT** `/user`

> 회원 본인의 정보를 변경합니다.

## [Example](UserAPIDetail.md#회원-정보-변경)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 바디 파라미터(**※반드시 FormData로 보낼 것**)
| 파라미터         | 설명           | 값 예시                             |
|--------------|--------------|----------------------------------|
| user         | 요청 user json | {userEmail,userNick,userAddress} |
| userEmail    | 회원 이메일       | "hong@example.com"               |
| userNick     | 회원 닉네임       | "홍길동"                            |
| userAddress  | 회원 주소        | "서울특별시 중구 세종대로 110"              |
| profileImage | 프로필 사진       | 파일                               |

---

### 작성한 글 조회

**PUT** `/user/boards`

> 회원 본인이 작성한 게시글을 조회합니다.

## [Example](UserAPIDetail.md#작성한-글-조회)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 응답 바디 파라미터
| 파라미터             | 설명                  | 값 예시                     |
|------------------|---------------------|--------------------------|
| content          | 게시글 목록              | [{boardNo,userId,...}]   |
| boardNo          | 게시글 번호              | 1                        |
| userId           | 작성한 회원 ID           | "user1"                  |
| userNick         | 작성한 회원 닉네임          | "걷는남자"                   |
| userType         | 작성한 회원 종류           | 0(관리자), 1(일반 사용자)        |
| userTypeName     | 작성한 회원 종류 이름        | Admin(관리자), User(일반 사용자) |
| boardTitle       | 게시글 제목              | "서울숲 산책 코스 추천"           |
| boardDescription | 게시글 설명              | "조용하고 분위기 있어서 좋았어요."     |
| boardViewCount   | 게시글 조회수             | 1                        |
| boardWriteDate   | 게시글 작성일             | "2025-05-09T23:21:49"    |
| boardUpdateDate  | 게시글 수정일             | "2025-05-09T23:51:40"    |
| boardLike        | 게시글 좋아요 수           | 0                        |
| boardHate        | 게시글 싫어요 수           | 0                        |
| categoryNo       | 게시글 카테고리 번호         | 1                        |
| categoryName     | 게시글 카테고리 이름         | "자유게시판"                  |
| commentCount     | 게시글 댓글 수            | 0                        |
| userImg          | 회원 프로필 사진 주소        | null                     |
| pageNumber       | 페이지 번호              | 0                        |
| pageSize         | 한 번에 출력할 개수         | 10                       |
| sort             | 정렬 정보               | {empty,sorted,unsorted}  |
| empty            | 정렬 기준이 빈 값인지        | false                    |
| sorted           | 정렬이 적용되었는지          | true                     |
| unsorted         | 정렬이 미적용되었는지         | false                    |
| offset           | 전체 페이지 인덱스          | 0                        |
| paged            | 페이징이 적용되었는지         | true                     |
| unpaged          | 페이징이 미적용되었는지        | false                    |
| last             | 마지막 페이지인지           | false                    |
| totalElements    | 전체 데이터 수            | 14                       |
| totalPages       | 전체 페이지 수            | 2                        |
| first            | 처음 페이지인지            | true                     |
| numberOfElements | 현재 페이지에 들어 있는 데이터 수 | 10                       |
| size             | 요청한 페이지 크기          | 10                       |
| number           | 현재 페이지 번호           | 0                        |
| empty            | 현재 페이지가 비어있는지       | false                    |

---

### 좋아요한 글 조회

**PUT** `/user/boards/liked`

> 회원 본인이 좋아요를 누른 게시글을 조회합니다.

## [Example](UserAPIDetail.md#좋아요한-글-조회)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 응답 바디 파라미터
| 파라미터             | 설명                  | 값 예시                     |
|------------------|---------------------|--------------------------|
| content          | 게시글 목록              | [{boardNo,userId,...}]   |
| boardNo          | 게시글 번호              | 1                        |
| userId           | 작성한 회원 ID           | "user1"                  |
| userNick         | 작성한 회원 닉네임          | "걷는남자"                   |
| userType         | 작성한 회원 종류           | 0(관리자), 1(일반 사용자)        |
| userTypeName     | 작성한 회원 종류 이름        | Admin(관리자), User(일반 사용자) |
| boardTitle       | 게시글 제목              | "서울숲 산책 코스 추천"           |
| boardDescription | 게시글 설명              | "조용하고 분위기 있어서 좋았어요."     |
| boardViewCount   | 게시글 조회수             | 1                        |
| boardWriteDate   | 게시글 작성일             | "2025-05-09T23:21:49"    |
| boardUpdateDate  | 게시글 수정일             | "2025-05-09T23:51:40"    |
| boardLike        | 게시글 좋아요 수           | 0                        |
| boardHate        | 게시글 싫어요 수           | 0                        |
| categoryNo       | 게시글 카테고리 번호         | 1                        |
| categoryName     | 게시글 카테고리 이름         | "자유게시판"                  |
| commentCount     | 게시글 댓글 수            | 0                        |
| userImg          | 회원 프로필 사진 주소        | null                     |
| pageNumber       | 페이지 번호              | 0                        |
| pageSize         | 한 번에 출력할 개수         | 10                       |
| sort             | 정렬 정보               | {empty,sorted,unsorted}  |
| empty            | 정렬 기준이 빈 값인지        | false                    |
| sorted           | 정렬이 적용되었는지          | true                     |
| unsorted         | 정렬이 미적용되었는지         | false                    |
| offset           | 전체 페이지 인덱스          | 0                        |
| paged            | 페이징이 적용되었는지         | true                     |
| unpaged          | 페이징이 미적용되었는지        | false                    |
| last             | 마지막 페이지인지           | false                    |
| totalElements    | 전체 데이터 수            | 14                       |
| totalPages       | 전체 페이지 수            | 2                        |
| first            | 처음 페이지인지            | true                     |
| numberOfElements | 현재 페이지에 들어 있는 데이터 수 | 10                       |
| size             | 요청한 페이지 크기          | 10                       |
| number           | 현재 페이지 번호           | 0                        |
| empty            | 현재 페이지가 비어있는지       | false                    |

---

### 속한 그룹 조회

**GET** `/user/groups`

> 회원이 속한 그룹들을 조회합니다.

## [Example](UserAPIDetail.md#속한-그룹-조회)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 응답 바디 파라미터
| 파라미터               | 설명            | 값 예시                  |
|--------------------|---------------|-----------------------|
| groupNo            | 그룹 ID         | 1                     |
| groupTitle         | 그룹명           | "헬스 커뮤니티"             |
| groupDescription   | 그룹 설명         | "헬스를 사랑하는 자들의 모임"     |
| groupCreateDate    | 그룹 생성일        | "2025-05-24T01:02:54" |
| groupCreatedUserId | 그룹을 생성한 회원 ID | "user1"               |

---

### 비밀번호 변경

**POST** `/user/password`

> 회원의 비밀번호를 변경합니다.

## [Example](UserAPIDetail.md#비밀번호-변경)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 바디 파라미터
| 파라미터       | 설명      | 값 예시       |
|------------|---------|------------|
| userPasswd | 회원 비밀번호 | "12345678" |

---

### 회원 탈퇴

**DELETE** `/user`

> 탈퇴 처리를 진행합니다.  
> 탈퇴시 비밀번호를 입력해 기존 비밀번호와 일치해야 합니다.

## [Example](UserAPIDetail.md#회원-탈퇴)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 바디 파라미터
| 파라미터       | 설명      | 값 예시       |
|------------|---------|------------|
| userPasswd | 회원 비밀번호 | "12345678" |

