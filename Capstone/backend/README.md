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

## 주의사항 2 ✏️
- 카테고리에 따른 contentTypeId와 cat 매핑 테이블이 필요합니다.
- 다음의 sql문을 적용해 기존 content_type 테이블을 삭제하고 새로운 테이블을 만들어주세요.
```mysql
DROP TABLE content_type;

CREATE TABLE place_category (
    place_category_no INT AUTO_INCREMENT,
    place_category_code VARCHAR(20),
    place_category_name VARCHAR(20),
    PRIMARY KEY (place_category_no)
);

INSERT INTO place_category (place_category_code, place_category_name) VALUES
  ('tour', '관광지'),
  ('food', '음식점'),
  ('cafe', '카페'),
  ('convenience-store', '편의점'),
  ('shopping', '쇼핑'),
  ('culture', '문화시설'),
  ('event', '공연/행사'),
  ('other', '기타');

CREATE TABLE place_category_detail (
   place_category_detail_no INT AUTO_INCREMENT,
   place_category_detail_code VARCHAR(30),
   place_category_detail_name VARCHAR(20),
   parent_no INT NOT NULL,
   content_type_id VARCHAR(2),
   cat1 VARCHAR(3),
   cat2 VARCHAR(5),
   cat3 VARCHAR(9),
   additional_search TINYINT,
   search_type VARCHAR(30),
   PRIMARY KEY (place_category_detail_no),
   FOREIGN KEY (parent_no) references place_category(place_category_no)
);

INSERT INTO place_category_detail (place_category_detail_code, place_category_detail_name, parent_no, content_type_id, cat1, cat2, cat3, additional_search, search_type) VALUES
 ('tour-nature', '자연', 1, '12', 'A01', null, null, false, null),
 ('tour-tradition', '역사', 1, '12', 'A02', 'A0201', null, false, null),
 ('tour-park', '공원', 1, '12', 'A02', 'A0202', 'A02020700', false, null),
 ('tour-theme-park', '테마파크', 1, '12', 'A02', 'A0202', 'A02020600', false, null),
 ('food-korean', '한식', 2, '39', 'A05', 'A0502', 'A05020100', false, null),
 ('food-western', '양식', 2, '39', 'A05', 'A0502', 'A05020200', false, null),
 ('food-japanese', '일식', 2, '39', 'A05', 'A0502', 'A05020300', false, null),
 ('food-chinese', '중식', 2, '39', 'A05', 'A0502', 'A05020400', false, null),
 ('food-other', '기타', 2, '39', 'A05', 'A0502', 'A05020700', false, null),
 ('cafe', '카페', 3, '39', 'A05', 'A0502', 'A05020900', true, 'cafe'),
 ('convenience-store', '편의점', 4, null, null, null, null, true, 'convenience_store'),
 ('shopping-permanent-market', '상설시장', 5, '38', 'A04', 'A0401', 'A04010200', false, null),
 ('shopping-department-store', '백화점', 5, null, null, null, null, true, 'department_store'),
 ('culture', '문화시설', 6, '14', 'A02', 'A0206', null, true, 'movie_theater'),
 ('event', '공연/행사', 7, '15', 'A02', null, null, false, null),
 ('other', '기타', 8, null, null, null, null, false, null);
```