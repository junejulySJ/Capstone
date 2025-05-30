# Comment API
## API 바로가기
| API 호출                                          | 설명           |
|-------------------------------------------------|--------------|
| [GET /boards/{boardNo}/comments](#특정-게시글-댓글-조회) | 특정 게시글 댓글 조회 |
| [POST /boards/{boardNo}/comments](#댓글-등록)       | 댓글 등록        |
| [PUT /comments/{commentNo}](#댓글-수정)             | 댓글 수정        |
| [DELETE /comments/{commentNo}](#댓글-삭제)          | 댓글 삭제        |

---

## API 상세

### 특정 게시글 댓글 조회

**GET** `/boards/{boardNo}/comments`

> 특정 게시글의 댓글 전체를 조회합니다.

## [Example](CommentAPIDetail.md#특정-게시글-댓글-조회)

#### 요청 경로 파라미터
| 파라미터    | 설명     | 값 예시 |
|---------|--------|------|
| boardNo | 게시글 번호 | 1    |

#### 응답 바디 파라미터
| 파라미터             | 설명           | 값 예시                     |
|------------------|--------------|--------------------------|
| commentNo        | 댓글 번호        | 2                        |
| userId           | 작성한 회원 ID    | "user1"                  |
| userNick         | 작성한 회원 닉네임   | "걷는남자"                   |
| userType         | 작성한 회원 종류    | 0(관리자), 1(일반 사용자)        |
| userTypeName     | 작성한 회원 종류 이름 | Admin(관리자), User(일반 사용자) |
| commentContent   | 댓글 내용        | "댓글 내용"                  |
| commentWriteDate | 댓글 작성일       | "2025-05-10T14:40:42"    |
| userImg          | 회원 프로필 사진 주소 | null                     |
| boardNo          | 게시글 번호       | 1                        |

---

### 댓글 등록

**POST** `/boards/{boardNo}/comments`

> 댓글을 등록합니다.

## [Example](CommentAPIDetail.md#댓글-등록)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터    | 설명     | 값 예시 |
|---------|--------|------|
| boardNo | 게시글 번호 | 1    |

#### 요청 바디 파라미터
| 파라미터           | 설명    | 값 예시    |
|----------------|-------|---------|
| commentContent | 댓글 내용 | "댓글 내용" |

#### 응답 바디 파라미터
| 파라미터             | 설명           | 값 예시                     |
|------------------|--------------|--------------------------|
| commentNo        | 댓글 번호        | 2                        |
| userId           | 작성한 회원 ID    | "user1"                  |
| userNick         | 작성한 회원 닉네임   | "걷는남자"                   |
| userType         | 작성한 회원 종류    | 0(관리자), 1(일반 사용자)        |
| userTypeName     | 작성한 회원 종류 이름 | Admin(관리자), User(일반 사용자) |
| commentContent   | 댓글 내용        | "댓글 내용"                  |
| commentWriteDate | 댓글 작성일       | "2025-05-10T14:40:42"    |
| userImg          | 회원 프로필 사진 주소 | null                     |
| boardNo          | 게시글 번호       | 1                        |

---

### 댓글 수정

**PUT** `/comments/{commentNo}`
> 댓글을 수정합니다.

## [Example](CommentAPIDetail.md#댓글-등록)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터      | 설명    | 값 예시 |
|-----------|-------|------|
| commentNo | 댓글 번호 | 2    |

#### 요청 바디 파라미터
| 파라미터           | 설명    | 값 예시    |
|----------------|-------|---------|
| commentContent | 댓글 내용 | "댓글 내용" |

---

### 댓글 삭제

**DELETE** `/comments/{commentNo}`
> 댓글을 삭제합니다.

## [Example](CommentAPIDetail.md#댓글-삭제)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터      | 설명    | 값 예시 |
|-----------|-------|------|
| commentNo | 댓글 번호 | 2    |
