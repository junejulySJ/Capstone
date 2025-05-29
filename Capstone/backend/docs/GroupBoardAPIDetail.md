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
- [GroupBoard API](GroupBoardAPI.md) **(Example)**

# GroupBoard API 예시
## API 바로가기
| API 호출                                                                 | 설명           |
|------------------------------------------------------------------------|--------------|
| [GET /groups/{groupNo}/boards](#그룹-전체-게시글-조회)                          | 그룹 전체 게시글 조회 |
| [POST /groups/{groupNo}/boards](#그룹-게시글-등록)                            | 그룹 게시글 등록    |
| [DELETE /groups/{groupNo}/boards/{groupBoardNo}](#그룹-게시글-삭제)           | 그룹 게시글 삭제    |
| [POST /groups/{groupNo}/boards/{groupBoardNo}/comments](#그룹-게시글-댓글-등록) | 그룹 게시글 댓글 등록 |

---

## API 상세

### 그룹 전체 게시글 조회

#### Request 예시
```javascript
axios.get(`${API_BASE_URL}/groups/3/boards`, { withCredentials: true })
```

#### Response 예시
```json
[
  {
    "groupBoardNo": 4,
    "groupBoardTitle": "게시글 테스트",
    "groupBoardContent": "게시글 내용 테스트",
    "userId": "user1",
    "userNick": "사용자1",
    "groupNo": 3,
    "groupTitle": "testgroup2",
    "groupBoardWriteDate": "2025-05-29T00:43:19",
    "groupBoardUpdateDate": "2025-05-29T00:43:19",
    "comments": []
  },
  {
    "groupBoardNo": 3,
    "groupBoardTitle": "groupboardtest",
    "groupBoardContent": "groupboardtestcontent",
    "userId": "user2",
    "userNick": "사용자2",
    "groupNo": 3,
    "groupTitle": "testgroup2",
    "groupBoardWriteDate": "2025-05-24T21:14:59",
    "groupBoardUpdateDate": "2025-05-24T21:14:59",
    "comments": [
      {
        "groupCommentNo": 3,
        "userId": "user2",
        "userNick": "사용자2",
        "groupCommentContent": "testcomment",
        "groupCommentWriteDate": "2025-05-24T21:44:09",
        "userImg": null,
        "groupBoardNo": 3
      },
      {
        "groupCommentNo": 4,
        "userId": "user1",
        "userNick": "사용자1",
        "groupCommentContent": "테스트 댓글",
        "groupCommentWriteDate": "2025-05-29T00:13:08",
        "userImg": "https://capstone-meetingmap.s3.eu-north-1.amazonaws.com/8c0405c9-6369-4dec-ae70-4e197217fbb4_ai-generated-9510467_640.jpg",
        "groupBoardNo": 3
      }
    ]
  }
]
```

---

### 그룹 게시글 등록

#### Request 예시
```javascript
axios.post(`${API_BASE_URL}/groups/3/boards`, {
    "groupBoardTitle": "groupboardtest",
    "groupBoardContent": "groupboardtestcontent"
}, { withCredentials: true })
```

---

### 그룹 게시글 삭제

#### Request 예시
```javascript
axios.delete(`${API_BASE_URL}/groups/3/boards/3`, { withCredentials: true })
```

---

### 그룹 게시글 댓글 등록

#### Request 예시
```javascript
axios.post(`${API_BASE_URL}/groups/3/boards/3/comments`, {
    "groupCommentContent": "testcomment"
}, { withCredentials: true })
```

#### Response 예시
```json
{
    "groupCommentNo": 3,
    "userId": "user2",
    "userNick": "사용자2",
    "groupCommentContent": "testcomment",
    "groupCommentWriteDate": "2025-05-24T21:44:08.9765259",
    "userImg": null,
    "groupBoardNo": 3
}
```