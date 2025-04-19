# User API

`User API`는 사용자 관리 기능을 제공하는 REST API입니다.  
주요 기능:
- 아이디 중복 검사
- 회원가입
- 회원 정보 조회
- 전체 회원 조회 (관리자만 접근 가능)

## 기본 URL

http://localhost:8081/api

프론트엔드: `http://localhost:8081`  

개발 중: `http://localhost:3000`도 사용 가능  
(CORS 허용, package.json에서 "proxy": "http://localhost:8081/", 제거 필요)

---

## API 목록

### 1. 아이디 중복 검사

**POST** `/check-id`

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

#### 상태 코드
- 200 OK : 성공적으로 검사 완료

---

### 2. 회원가입
**POST** `/register`

> 신규 회원을 등록합니다.

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
- Location 헤더: /users/{userId}
```json
{
  "userId": "exampleUser"
}
```

#### 실패 응답
- **409 Conflict** : 이미 존재하는 사용자 ID
```json
"이미 존재하는 회원입니다."
```

---

### 3. 회원 정보 조회
**GET** `/users/{userId}`

> 특정 회원 정보를 조회합니다.  
**로그인된 사용자 본인만** 조회할 수 있습니다.

#### 요청 헤더
- 세션 기반 인증 필요
```javascript
axios
    .get(`${API_BASE_URL}/users/${userId}`, {
        withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
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
- **401 Unauthorized** : 로그인 중이 아니거나 본인이 아닌 다른 사용자의 정보 요청 시
```json
"권한이 없습니다."
```
- **404 Not Found** : 관리자로 로그인 중에 존재하지 않는 사용자 ID를 요청 시  
(현재 관리자 검증 로직이 없어 수정 예정)

### 4. 전체 회원 조회 (관리자 전용)
**GET** `/users`

> 모든 회원 정보를 조회합니다.  
관리자 계정으로 로그인된 경우만 접근할 수 있습니다.

#### 요청 헤더
- 세션 기반 인증 필요
```javascript
axios
    .get(`${API_BASE_URL}/users/`, {
        withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
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
    "userType": 1
  }
]
```

#### 실패 응답
- **401 Unauthorized** : 로그인 중이 아니거나 관리자가 아닌 경우
```json
"권한이 없습니다."
```
- **404 Not Found** : 세션의 회원 id가 db에 존재하지 않는 경우(일어날 일이 거의 없음)

