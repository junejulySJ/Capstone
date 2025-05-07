## 📚 목차
- [Home](../README.md)
- **User API**
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md)
- [Schedule API](ScheduleAPI.md)
- [Path API](PathAPI.md)

# User API
주요 기능:
- 아이디 중복 검사
- 회원가입
- 회원 정보 조회 (현재 로그인한 회원 정보 접근)
- 전체 회원 조회 (관리자만 접근 가능)
- 회원 정보 수정

---

## API 목록

<details>
<summary>아이디 중복 검사</summary>

**POST** `/user/check-id`

> 사용자가 입력한 아이디가 이미 존재하는지 검사합니다.

#### 요청 바디
```json
{
  "userId": "exampleUser"
}
```
#### 응답 바디
```json
{
  "available": true
}
```
</details>

---

<details>
<summary>회원가입</summary>

**POST** `/user/register`

> 신규 회원을 등록합니다.

#### 요청 바디
```json
{
  "userId": "exampleUser",
  "userEmail": "hong@example.com",
  "userPasswd": "password123",
  "userNick": "홍길동",
  "userAddress": "서울특별시 중구 세종대로 110"
}
```

#### 성공 응답
- **HTTP 201 Created**
#### 응답 바디
```json
{
  "userId": "exampleUser",
  "userEmail": "hong@example.com",
  "userNick": "홍길동",
  "userImg": null,
  "userAddress": "서울특별시 중구 세종대로 110",
  "userType": 1
}
```

#### 실패 응답
- **409 Conflict** : 이미 존재하는 사용자 ID
</details>

---

<details>
<summary>회원 정보 조회</summary>

**GET** `/user`

> 회원 본인의 정보를 조회합니다.

#### 요청 코드
- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user`, {
        withCredentials: true,
    })
```

#### 성공 응답
```json
{
  "userId": "exampleUser",
  "userEmail": "hong@example.com",
  "userNick": "홍길동",
  "userImg": "hong.png",
  "userAddress": "서울특별시 중구 세종대로 110",
  "userType": 1
}
```

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아닌 경우
- **404 Not Found** : 존재하지 않는 사용자 ID를 요청 시
</details>

---

<details>
<summary>전체 회원 조회</summary>

**GET** `/user/list`

> 모든 회원 정보를 조회합니다.  
관리자 계정으로 로그인된 경우만 접근할 수 있습니다.

#### 요청 코드
- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/user`, {
    withCredentials: true,
})
```

#### 성공 응답
```json
[
  {
    "userId": "exampleUser1",
    "userEmail": "hong@example.com",
    "userNick": "홍길동",
    "userImg": "hong.png",
    "userAddress": "서울특별시 중구 세종대로 110",
    "userType": 1
  },
  {
    "userId": "exampleUser2",
    "userEmail": "lee@example.com",
    "userNick": "이순신",
    "userImg": "lee.png",
    "userAddress": "서울특별시 종로구 세종대로 172",
    "userType": 0
  }
]
```

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아니거나 관리자가 아닌 경우
</details>

---

<details>
<summary>회원 정보 변경 ✏️</summary>

**PUT** `/user`

> 회원 본인의 정보를 변경합니다.

#### 요청 코드
- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .put(`${API_BASE_URL}/user`, {
        "userEmail": "hong1@example.com",
        "userNick": "홍길동",
        "userAddress": "서울특별시 용산구 남산공원길 105",
    }, {
    withCredentials: true,
})
```

#### 성공 응답
```json
{
    "userId": "exampleUser1",
    "userEmail": "hong1@example.com",
    "userNick": "홍길동",
    "userImg": "hong.png",
    "userAddress": "서울특별시 용산구 남산공원길 105",
    "userType": 1
  }
```

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아닌 경우
</details>