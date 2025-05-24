## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md) [(Example)](UserAPIDetail.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md) [(Example)](MapAPIDetail.md)
- [Schedule API](ScheduleAPI.md) [(Example)](ScheduleAPIDetail.md)
- [Path API](PathAPI.md) [(Example)](PathAPIDetail.md)
- [Board API](BoardAPI.md) [(Example)](BoardAPIDetail.md)
- [Comment API](CommentAPI.md) [(Example)](CommentAPIDetail.md)
- [Group API](GroupAPI.md) [(Example)](GroupAPIDetail.md)
- [GroupBoard API](GroupBoardAPI.md) [(Example)](GroupBoardAPIDetail.md)
- **GroupComment API** [(Example)](GroupCommentAPIDetail.md)

# GroupComment API
## API 바로가기
| API 호출                                                                                    | 설명           |
|-------------------------------------------------------------------------------------------|--------------|
| [GET /groups/{groupNo}/boards/{groupBoardNo}/comments](#특정-게시글-댓글-조회)                     | 특정 게시글 댓글 조회 |
| [POST /groups/{groupNo}/boards/{groupBoardNo}/comments](#그룹-게시글-댓글-등록)                    | 그룹 게시글 댓글 등록 |
| [PUT /groups/{groupNo}/boards/{groupBoardNo}/comments/{groupCommentNo}](#그룹-게시글-댓글-수정)    | 그룹 게시글 댓글 수정 |
| [DELETE /groups/{groupNo}/boards/{groupBoardNo}/comments/{groupCommentNo}](#그룹-게시글-댓글-삭제) | 그룹 게시글 댓글 삭제 |

---

## API 상세

### 특정 게시글 댓글 조회

**GET** `/groups/{groupNo}/boards/{groupBoardNo}/comments`

> 해당 그룹의 특정 게시글의 댓글을 조회합니다.  
> page, size 등으로 일부만 조회할 수 있습니다.  
> 파라미터를 사용하지 않을 경우 page는 0, size는 10, sortBy는 groupCommentWriteDate, direction은 desc이 기본값입니다.

## [Example](GroupCommentAPIDetail.md#특정-게시글-댓글-조회)

#### 요청 경로 파라미터
| 파라미터         | 설명        | 값 예시 |
|--------------|-----------|------|
| groupNo      | 그룹 번호     | 3    |
| groupBoardNo | 그룹 게시글 번호 | 3    |

#### 요청 쿼리 파라미터
| 파라미터      | 필수 여부 | 설명          | 값 예시                    |
|-----------|-------|-------------|-------------------------|
| page      | X     | 페이지 번호      | 0                       |
| size      | X     | 한 번에 출력할 개수 | 10                      |
| sortBy    | X     | 정렬 기준       | groupCommentWriteDate 등 |
| direction | X     | 정렬 방향       | asc, desc               |

#### 응답 바디 파라미터
| 파라미터             | 설명                  | 값 예시                      |
|------------------|---------------------|---------------------------|
| content          | 게시글 목록              | [{boardNo,userId,...}]    |
| boardNo          | 게시글 번호              | 1                         |
| userId           | 작성한 회원 ID           | "user1"                   |
| userNick         | 작성한 회원 닉네임          | "걷는남자"                    |
| userType         | 작성한 회원 종류           | 0(관리자), 1(일반 사용자)         |
| userTypeName     | 작성한 회원 종류 이름        | Admin(관리자), User(일반 사용자)  |
| boardTitle       | 게시글 제목              | "서울숲 산책 코스 추천"            |
| boardDescription | 게시글 설명              | "조용하고 분위기 있어서 좋았어요."      |
| boardViewCount   | 게시글 조회수             | 1                         |
| boardWriteDate   | 게시글 작성일             | "2025-05-09T23:21:49"     |
| boardUpdateDate  | 게시글 수정일             | "2025-05-09T23:51:40"     |
| boardLike        | 게시글 좋아요 수           | 0                         |
| boardHate        | 게시글 싫어요 수           | 0                         |
| categoryNo       | 게시글 카테고리 번호         | 1                         |
| categoryName     | 게시글 카테고리 이름         | "자유게시판"                   |
| commentCount     | 게시글 댓글 수            | 0                         |
| userImg          | 회원 프로필 사진 주소        | null                      |
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

### 그룹 게시글 댓글 등록

**POST** `/groups/{groupNo}/boards/{groupBoardNo}/comments`

> 해당 그룹 게시글에 댓글을 작성합니다.  
> 해당 그룹의 멤버만 작성할 수 있습니다.

## [Example](GroupCommentAPIDetail.md#그룹-게시글-댓글-등록)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터         | 설명        | 값 예시 |
|--------------|-----------|------|
| groupNo      | 그룹 번호     | 3    |
| groupBoardNo | 그룹 게시글 번호 | 3    |

#### 요청 바디 파라미터
| 파라미터              | 설명           | 값 예시        |
|-------------------|--------------|-------------|
| groupBoardContent | 그룹 게시글 댓글 내용 | "테스트 댓글입니다" |

#### 응답 바디 파라미터
| 파라미터                  | 설명               | 값 예시                          |
|-----------------------|------------------|-------------------------------|
| groupCommentNo        | 그룹 게시글 댓글 번호     | 3                             |
| userId                | 작성한 회원 ID        | "user1"                       |
| userNick              | 작성한 회원 닉네임       | "걷는남자"                        |
| groupCommentContent   | 그룹 게시글 댓글 내용     | "테스트 댓글입니다"                   |
| groupCommentWriteDate | 그룹 게시글 댓글 작성일    | "2025-05-24T21:44:08.9765259" |
| userImg               | 작성한 회원 프로필 사진 주소 | "amazonaws.com..."            |
| groupBoardNo          | 그룹 게시글 번호        | 3                             |

---

### 그룹 게시글 댓글 수정

**PUT** `/groups/{groupNo}/boards/{groupBoardNo}/comments/{groupCommentNo}`

> 해당 그룹 게시글의 댓글을 수정합니다.  
> 해당 댓글 작성자만 수정할 수 있습니다.

## [Example](GroupCommentAPIDetail.md#그룹-게시글-댓글-수정)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터           | 설명           | 값 예시 |
|----------------|--------------|------|
| groupNo        | 그룹 번호        | 3    |
| groupBoardNo   | 그룹 게시글 번호    | 3    |
| groupCommentNo | 그룹 게시글 댓글 번호 | 3    |

#### 요청 바디 파라미터
| 파라미터              | 설명           | 값 예시        |
|-------------------|--------------|-------------|
| groupBoardContent | 그룹 게시글 댓글 내용 | "테스트 댓글입니다" |

---

### 그룹 게시글 댓글 삭제

**PUT** `/groups/{groupNo}/boards/{groupBoardNo}/comments/{groupCommentNo}`

> 해당 그룹 게시글의 댓글을 삭제합니다.  
> 그룹장과 해당 댓글 작성자만 삭제할 수 있습니다.

## [Example](GroupCommentAPIDetail.md#그룹-게시글-댓글-삭제)

#### 요청 쿠키
| 쿠키          | 설명  | 값 예시            |
|-------------|-----|-----------------|
| accessToken | jwt | eyJhbGciOiJI... |
- 로그인을 진행하면 자동으로 쿠키가 등록되어 보내집니다.

#### 요청 경로 파라미터
| 파라미터           | 설명           | 값 예시 |
|----------------|--------------|------|
| groupNo        | 그룹 번호        | 3    |
| groupBoardNo   | 그룹 게시글 번호    | 3    |
| groupCommentNo | 그룹 게시글 댓글 번호 | 3    |
