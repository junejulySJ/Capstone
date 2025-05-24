## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md) [(Example)](UserAPIDetail.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md) [(Example)](MapAPIDetail.md)
- [Schedule API](ScheduleAPI.md) [(Example)](ScheduleAPIDetail.md)
- [Path API](PathAPI.md) [(Example)](PathAPIDetail.md)
- [Board API](BoardAPI.md) [(Example)](BoardAPIDetail.md)
- [Comment API](CommentAPI.md) **(Example)**
- [Group API](GroupAPI.md) [(Example)](GroupAPIDetail.md)
- [GroupBoard API](GroupBoardAPI.md) [(Example)](GroupBoardAPIDetail.md)
- [GroupComment API](GroupCommentAPI.md) [(Example)](GroupCommentAPIDetail.md)

# Comment API 예시
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

#### Request 예시 1 (특정 게시글 댓글 전체 조회)
```javascript
axios.get(`${API_BASE_URL}/boards/37/comments`)
```

#### Response 예시 1 (특정 게시글 댓글 전체 조회)
```json
{
  "content": [
    {
      "commentNo": 3,
      "userId": "user1",
      "userNick": "사용자1",
      "userType": 1,
      "userTypeName": "User",
      "commentContent": "댓글 내용",
      "commentWriteDate": "2025-05-21T15:50:45",
      "userImg": null,
      "boardNo": 37
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

#### Request 예시 2 (댓글 10개만 작성일 기준 내림차순 조회)
```javascript
axios.get(`${API_BASE_URL}/boards/37/comments?page=0&size=10&sortBy=commentWriteDate&direction=desc`)
```

#### Response 예시 2 (댓글 10개만 작성일 기준 내림차순 조회)
```json
{
  "content": [
    {
      "commentNo": 3,
      "userId": "user1",
      "userNick": "사용자1",
      "userType": 1,
      "userTypeName": "User",
      "commentContent": "댓글 내용",
      "commentWriteDate": "2025-05-21T15:50:45",
      "userImg": null,
      "boardNo": 37
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### 댓글 등록

#### Request 예시
```javascript
axios.post(`${API_BASE_URL}/boards/37/comments`, {
    "commentContent": "댓글 내용"
}, { withCredentials: true })
```

#### Response 예시
```json
{
  "commentNo": 3,
  "userId": "user1",
  "userNick": "사용자1",
  "userType": 1,
  "userTypeName": "User",
  "commentContent": "댓글 내용",
  "commentWriteDate": "2025-05-21T15:50:44.6371739",
  "userImg": null,
  "boardNo": 37
}
```

---

### 댓글 수정

#### Request 예시
```javascript
axios.put(`${API_BASE_URL}/comments/3`, {
    "commentContent": "댓글 내용"
}, { withCredentials: true })
```

---

### 댓글 삭제

#### Request 예시
```javascript
axios.delete(`${API_BASE_URL}/comments/3`, { withCredentials: true })
```
