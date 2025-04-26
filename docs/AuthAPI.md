## 📚 목차
- Home(./READ)
- [User API](UserAPI.md)
- **Auth API**

# Auth API
주요 기능:
- 로그인 ✏️
- 카카오 로그인
---

## API 목록

### 1. 로그인
**POST** `/auth/login` ✏️

> 일반 로그인을 진행합니다.

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

### 1. 로그인
**POST** `/auth/kakao` ✏️

> 카카오 로그인을 진행합니다.
#### ※ 카카오 로그인 버튼 클릭시 다음과 같은 주소로 연결되게 할 것
`https://kauth.kakao.com/oauth/authorize?client_id=d88db5d8494588ec7e3f5e9aa95b78d8&redirect_uri=http://localhost:8080/api/auth/kakao/callback&response_type=code`
#### 이후 해당 주소에서 동의하고 계속하기 클릭시 자동으로 다음과 같은 주소로 리다이렉트됩니다
`http://localhost:8080/api/auth/kakao/callback?code=YJ38DptytEk_DdgL...`

#### 성공 응답
- Authorization 헤더: `Bearer eyJhbGciOiJIUzI1Ni...`  
  **※ JWT 토큰을 저장할 때 `Bearer ` 부분은 제외하고 저장할 것**