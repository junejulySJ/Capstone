### 수정된 부분은 이모지 ✏️ 로 표시해 놓았습니다.

## 🚀 기본 URL

`http://localhost:8080/api`
- 각 API 문서의 주소는 모두 `http://localhost:8080/api` 가 생략된 형태로 작성하였습니다.

프론트엔드: `http://localhost:3000`

## 📚 목차
- **Home**
- [User API](docs/UserAPI.md)
- [Auth API](docs/AuthAPI.md)
- [Friendship API](docs/FriendshipAPI.md)
- [Map API](docs/MapAPI.md) ✏️

## 주의사항 ✏️
- 비밀번호 암호화 방식이 변경되어 기존 사용자로 로그인을 하려면 다음과 같은 sql문을 적용해주세요.
- 적용하면 기존대로 123456으로 로그인이 가능해집니다.
```mysql
UPDATE user
SET user_passwd = '$2a$10$36vCUK5vsfQ6/HK2HGatdOJ3x3RoH.6da2WESPsQ8zVKHqMB96Mle'
WHERE user_id IN (
  'admin1', 'admin2', 'admin3',
  'user1', 'user2', 'user3', 'user4', 'user5',
  'user6', 'user7', 'user8', 'user9', 'user10'
);
```