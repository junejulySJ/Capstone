## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- **Auth API**
- [Friendship API](FriendshipAPI.md)

# Auth API
주요 기능:
- 로그인
- 카카오 로그인
- 로그아웃

---

## API 목록

<details>
<summary>로그인 ✏️</summary>

**POST** `/auth/login` ✏️

> 일반 로그인을 진행합니다.

#### 요청 바디
```json
{
  "userId": "exampleUser",
  "userPasswd": "password123"
}
```

#### 성공 응답 ✏️
- accessToken 쿠키: `eyJhbGciOiJIUzI1NiJ9.eyJ1c2Vy...`  
**※ 이후 로그인이 필요한 api로 axios 요청시 `withCredentials: true`만 추가로 넣어주면 됩니다.**

#### 실패 응답
- **401 Unauthorized** : 아이디 또는 패스워드 불일치
</details>

---

<details>
<summary>카카오 로그인 ✏️</summary>

**POST** `/auth/kakao` ✏️

> 카카오 로그인을 진행합니다.
1. 카카오 로그인 버튼 클릭시 다음과 같은 주소로 연결되게 합니다
`https://kauth.kakao.com/oauth/authorize?client_id=d88db5d8494588ec7e3f5e9aa95b78d8&redirect_uri=http://localhost:3000/auth/kakao/callback&response_type=code`
2. React에서 `/auth/kakao/callback` Route를 추가하고 관련 컴포넌트를 제작합니다.
3. 컴포넌트에서 다음과 같이 작성합니다.(예시)
```javascript
import React, { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAppContext } from "../../context/AppContext"; // ✅ context 불러오기
import { useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../constants.js";

export default function KakaoLogin() {
  const navigate = useNavigate();
  const location = useLocation();

  const { setUser } = useAppContext(); // ✅ context에 로그인 정보 저장할 함수

  useEffect(() => {
    // URLSearchParams로 쿼리 파라미터 추출
    const queryParams = new URLSearchParams(location.search);
    const code = queryParams.get("code"); // 'code' 파라미터 추출

    if (code) {
      // 카카오 코드 서버로 전송
      axios
              .get(`${API_BASE_URL}/auth/kakao?code=${code}`, {
                withCredentials: true,
              })
              .then((response) => {
                axios
                        .get(`${API_BASE_URL}/user`, {
                          withCredentials: true,
                        })
                        .then((response2) => {
                          setUser(response2.data);
                          navigate("/");
                        })
                        .catch((error) => {
                          console.error("사용자 조회에 실패했습니다.", error);
                        });
              })
              .catch((error) => {
                console.error("카카오 로그인 오류:", error);
              });
    }
  }, [location.search, navigate]);

  return (
          <div>
            <h1>카카오 로그인 처리 중...</h1>
          </div>
  );
}
```
</details>

---

<details>
<summary>로그아웃 ✏️</summary>

**POST** `/auth/logout` ✏️

> 로그아웃을 진행합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/auth/logout`, {
        withCredentials: true,
    })
```
</details>