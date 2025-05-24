## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md) [(Example)](UserAPIDetail.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md) [(Example)](MapAPIDetail.md)
- [Schedule API](ScheduleAPI.md) [(Example)](ScheduleAPIDetail.md)
- [Path API](PathAPI.md) [(Example)](PathAPIDetail.md)
- [Board API](BoardAPI.md) [(Example)](BoardAPIDetail.md)
- **Comment API** [(Example)](CommentAPIDetail.md)
- [Group API](GroupAPI.md) [(Example)](GroupAPIDetail.md)
- [GroupBoard API](GroupBoardAPI.md) [(Example)](GroupBoardAPIDetail.md)
- [GroupComment API](GroupCommentAPI.md) [(Example)](GroupCommentAPIDetail.md)

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
> page, size 등으로 일부만 조회할 수 있습니다.  
> 파라미터를 사용하지 않을 경우 page는 0, size는 10, sortBy는 commentWriteDate, direction은 desc이 기본값입니다.

## [Example](CommentAPIDetail.md#특정-게시글-댓글-조회)

#### 요청 쿼리 파라미터
| 파라미터      | 설명          | 값 예시               |
|-----------|-------------|--------------------|
| page      | 페이지 번호      | 0                  |
| size      | 한 번에 출력할 개수 | 10                 |
| sortBy    | 정렬 기준       | commentWriteDate 등 |
| direction | 정렬 방향       | asc, desc          |

#### 요청 경로 파라미터
| 파라미터    | 설명     | 값 예시 |
|---------|--------|------|
| boardNo | 게시글 번호 | 1    |

#### 응답 바디 파라미터
| 파라미터             | 설명                  | 값 예시                      |
|------------------|---------------------|---------------------------|
| content          | 댓글 목록               | [{boardNo,userId,...}]    |
| commentNo        | 댓글 번호               | 2                         |
| userId           | 작성한 회원 ID           | "user1"                   |
| userNick         | 작성한 회원 닉네임          | "걷는남자"                    |
| userType         | 작성한 회원 종류           | 0(관리자), 1(일반 사용자)         |
| userTypeName     | 작성한 회원 종류 이름        | Admin(관리자), User(일반 사용자)  |
| commentContent   | 댓글 내용               | "댓글 내용"                   |
| commentWriteDate | 댓글 작성일              | "2025-05-10T14:40:42"     |
| userImg          | 회원 프로필 사진 주소        | null                      |
| boardNo          | 게시글 번호              | 1                         |
| pageable         | 페이징                 | {pageNumber,pageSize,...} |
| pageNumber       | 페이지 번호              | 0                         |
| pageSize         | 한 번에 출력할 개수         | 10                        |
| sort             | 정렬 정보               | {empty,sorted,unsorted}   |
| empty            | 정렬 기준이 빈 값인지        | false                     |
| sorted           | 정렬이 적용되었는지          | true                      |
| unsorted         | 정렬이 미적용되었는지         | false                     |
| offset           | 전체 페이지 인덱스          | 0                         |
| paged            | 페이징이 적용되었는지         | true                      |
| unpaged          | 페이징이 미적용되었는지        | false                     |
| last             | 마지막 페이지인지           | false                     |
| totalElements    | 전체 데이터 수            | 14                        |
| totalPages       | 전체 페이지 수            | 2                         |
| first            | 처음 페이지인지            | true                      |
| numberOfElements | 현재 페이지에 들어 있는 데이터 수 | 10                        |
| size             | 요청한 페이지 크기          | 10                        |
| number           | 현재 페이지 번호           | 0                         |
| empty            | 현재 페이지가 비어있는지       | false                     |

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
