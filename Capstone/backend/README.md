### 수정된 부분은 이모지 ✏️ 로 표시해 놓았습니다.

## 🚀 기본 URL

`http://localhost:8080/api`
- 각 API 문서의 주소는 모두 `http://localhost:8080/api` 가 생략된 형태로 작성하였습니다.

프론트엔드: `http://localhost:3000`

## 📚 목차
- **Home**
- [User API](docs/UserAPI.md)
- [Auth API](docs/AuthAPI.md)
- [Friendship API](docs/FriendshipAPI.md) ✏️
- [Map API](docs/MapAPI.md)
- [Schedule API](docs/ScheduleAPI.md) ✏️
- [Path API](docs/PathAPI.md)

## 주의사항 1
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

## 주의사항 2
- contentTypeId를 DB에서 가져와 typeCode와 매칭하도록 하기 위해 테이블을 추가했습니다.
- 클라이언트쪽에서는 contentTypeId가 아닌 typeCode를 사용하면 됩니다.
```mysql
CREATE TABLE content_type (
    content_type_no INT AUTO_INCREMENT,
    content_type_id VARCHAR(2),
    content_type_name VARCHAR(30),
    PRIMARY KEY (content_type_no)
);

INSERT INTO content_type (content_type_id, content_type_name) VALUES
('12', '관광지'),
('14', '문화시설'),
('15', '행사/공연/축제'),
('28', '레포츠'),
('38', '쇼핑'),
('39', '음식점');
```