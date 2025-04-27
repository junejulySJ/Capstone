## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- **Friendship API**

# Friendship API
주요 기능:
- 친구 목록 조회
- 보낸 친구 요청 조회
- 받은 친구 요청 조회
- 친구 추가
- 친구 요청 수락

---

## API 목록

<details>
<summary>친구 목록 조회</summary>

**GET** `/user/friends`

> 친구 목록을 조회합니다.

#### 요청 코드
- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user/friends`, {
        withCredentials: true,
    })
```

#### 응답 바디
```json
[
  {
    "friendshipNo": 1,
    "userId": "user1",
    "opponentId": "user2",
    "opponentNick": "사용자2",
    "status": "ACCEPTED",
    "counterpartFriendshipNo": 2,
    "from": true
  },
  {
    "friendshipNo": 3,
    "userId": "user1",
    "opponentId": "user3",
    "opponentNick": "사용자3",
    "status": "ACCEPTED",
    "counterpartFriendshipNo": 4,
    "from": true
  }
]
```

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아닌 경우
</details>

---

<details>
<summary>보낸 친구 요청 조회</summary>

**GET** `/user/friends/sent`

> 내가 친구 요청을 보낸 회원 목록을 조회합니다.

#### 요청 코드
- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user/friends/sent`, {
        withCredentials: true,
    })
```

#### 응답 바디
```json
[
  {
    "friendshipNo": 7,
    "userId": "user1",
    "opponentId": "user5",
    "opponentNick": "사용자5",
    "status": "WAITING",
    "counterpartFriendshipNo": 8,
    "from": true
  }
]
```

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아닌 경우
</details>

---

<details>
<summary>받은 친구 요청 조회</summary>

**GET** `/user/friends/received`

> 나에게 친구 요청을 보낸 회원 목록을 조회합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user/friends/received`, {
        withCredentials: true,
    })
```

#### 응답 바디
```json
[
  {
    "friendshipNo": 8,
    "userId": "user5",
    "opponentId": "user1",
    "opponentNick": "사용자1",
    "status": "WAITING",
    "counterpartFriendshipNo": 7,
    "from": false
  }
]
```
</details>

---

<details>
<summary>친구 추가</summary>

**POST** `/user/friends/add`

> 특정한 회원에게 친구를 요청합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .post(`${API_BASE_URL}/user/friends/add`, {
        "opponentId": "user2",
    }, {
        withCredentials: true,
    })
```
</details>

---

<details>
<summary>친구 요청 수락</summary>

**GET** `/user/friends/approve/{friendshipNo}`

> 친구 요청을 수락합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user/friends/approve/{friendshipNo}`, {
        withCredentials: true,
    })
```
</details>