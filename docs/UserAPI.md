## 📚 목차
- User API
- Board API

# User API
주요 기능:
- 아이디 중복 검사
- 회원가입
- 로그인 <span style="color: red;">★</span>
- 회원 정보 조회 (회원 본인의 정보만 접근 가능)
- 전체 회원 조회 (관리자만 접근 가능)
---

## API 목록

### 1. 아이디 중복 검사

**POST** `/user/check-id` <span style="color: red;">★</span>

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
**POST** `/user/register` <span style="color: red;">★</span>

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
- Location 헤더: `/users/{userId}`  
  [<span style="color: red;">★</span> 응답 내용 변경]
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
```json
"이미 존재하는 회원입니다."
```

---

### 3. 로그인 <span style="color: red;">★</span>
**POST** `/login`

> 로그인을 진행합니다.

```json
{
  "userId": "exampleUser",
  "userPasswd": "password123"
}
```

#### 성공 응답
- **HTTP 201 Created**
- Location 헤더: `/user/{userId}`
- Authorization 헤더: `Bearer eyJhbGciOiJIUzI1Ni...`  
**※ JWT 토큰을 저장할 때 `Bearer ` 부분은 제외하고 저장할 것**

#### 실패 응답
- **401 Unauthorized** : 아이디 또는 패스워드 불일치

---

### 4. 회원 정보 조회
**GET** `/user/{userId}` <span style="color: red;">★</span>

> 특정 회원 정보를 조회합니다.  
> 관리자는 모든 사용자 정보를 조회 가능합니다.  
> 일반 사용자는 **로그인된 사용자 본인만** 조회할 수 있습니다.

#### 요청 코드
- JWT 토큰을 헤더에 실어 보낼 것 <span style="color: red;">★</span>
```javascript
axios
    .get(`${API_BASE_URL}/user/${userId}`, {
        headers: {
            'Authorization': `Bearer ${token}` // "Bearer "와 토큰을 헤더에 추가
        }
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
- **403 Forbidden** : 로그인 중이 아니거나 본인이 아닌 다른 사용자의 정보 요청 시 <span style="color: red;">★</span>
- **404 Not Found** : 관리자로 로그인 중에 존재하지 않는 사용자 ID를 요청 시

### 5. 전체 회원 조회
**GET** `/user` <span style="color: red;">★</span>

> 모든 회원 정보를 조회합니다.  
관리자 계정으로 로그인된 경우만 접근할 수 있습니다.

#### 요청 코드
- JWT 토큰을 헤더에 실어 보낼 것 <span style="color: red;">★</span>
```javascript
axios
    .get(`${API_BASE_URL}/user`, {
        headers: {
            'Authorization': `Bearer ${token}` // "Bearer "와 토큰을 헤더에 추가
        }
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
- **403 Forbidden** : 로그인 중이 아니거나 관리자가 아닌 경우 <span style="color: red;">★</span>