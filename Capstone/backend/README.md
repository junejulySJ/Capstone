### 수정된 부분은 이모지 ✏️ 로 표시해 놓았습니다.

## 🚀 기본 URL

`http://localhost:8080/api`
- 각 API 문서의 주소는 모두 `http://localhost:8080/api` 가 생략된 형태로 작성하였습니다.

프론트엔드: `http://localhost:3000`

## 📚 목차
- **Home**
- [User API](docs/UserAPI.md) [(Example)](docs/UserAPIDetail.md) ✏️
- [Auth API](docs/AuthAPI.md)
- [Friendship API](docs/FriendshipAPI.md)
- [Map API](docs/MapAPI.md) [(Example)](docs/MapAPIDetail.md) ✏️
- [Schedule API](docs/ScheduleAPI.md) [(Example)](docs/ScheduleAPIDetail.md) ✏️
- [Path API](docs/PathAPI.md) [(Example)](docs/PathAPIDetail.md)
- [Board API](docs/BoardAPI.md) [(Example)](docs/BoardAPIDetail.md) ✏️
- [Comment API](docs/CommentAPI.md) [(Example)](docs/CommentAPIDetail.md) ✏️
- [Group API](docs/GroupAPI.md) [(Example)](docs/GroupAPIDetail.md) ✏️
- [GroupBoard API](docs/GroupBoardAPI.md) [(Example)](docs/GroupBoardAPIDetail.md)
- [GroupComment API](docs/GroupCommentAPI.md) [(Example)](docs/GroupCommentAPIDetail.md) ✏️

# ※아래 방법 중 하나를 진행해야 정상적으로 실행됩니다.

## ※처음부터 테이블을 다시 생성하려면 [방법1](#방법1)을 따라가세요 (기존 테이블의 레코드는 전부 사라지니 백업 필수)

## ※기존 테이블을 그대로 사용하면서 변경사항을 추가하려면 [방법2](#방법2)를 따라가세요

# 방법1
1. 다음 sql문을 적용합니다. RDS에는 해당 쿼리로 적용되어있습니다. **(기존 테이블 내용은 전부 사라지니 백업 필수)**
```mysql
-- 데이터베이스 생성
DROP DATABASE IF EXISTS capstone_db;
CREATE DATABASE capstone_db;
USE capstone_db;

-- 사용자 계정 생성, 권한 부여
-- CREATE USER 'scott'@'localhost' IDENTIFIED BY '123456';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON capstone_db.* TO 'scott'@'localhost';

-- 테이블 생성
create table `user_role` (
    `user_type` int not null,
    `user_type_name` varchar(50) null,
    primary key (`user_type`)
);

create table `category` (
    `category_no` int not null,
    `category_name` varchar(50) null,
    primary key (`category_no`)
);

create table `user` (
    `user_id` varchar(50) not null,
    `user_email` varchar(255) null,
    `user_passwd` varchar(128) null,
    `user_nick` varchar(50) null,
    `user_img` varchar(255) null,
    `user_address` varchar(255) null,
    `user_type` int not null,
    `only_friends_can_see_activity` tinyint null,
    `email_notification_agree` tinyint null,
    `push_notification_agree` tinyint null,
    primary key (`user_id`)
);
alter table `user` add constraint `fk_user_role_to_user_1` foreign key (`user_type`) references `user_role` (`user_type`);

create table `board` (
    `board_no` int not null auto_increment,
    `board_title` varchar(255) null,
    `board_description` varchar(255) null,
    `board_content` text null,
    `board_view_count` int null,
    `board_write_date` datetime null,
    `board_update_date` datetime null,
    `user_id` varchar(50) not null,
    `category_no` int not null,
    `schedule_no` int null,
    primary key (`board_no`)
);
alter table `board` add constraint `fk_user_to_board_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `board` add constraint `fk_category_to_board_1` foreign key (`category_no`) references `category` (`category_no`);
alter table `board` add constraint `fk_schedule_to_board_1` foreign key (`schedule_no`) references `schedule` (`schedule_no`);

create table `board_file` (
    `file_no` int not null auto_increment,
    `file_name` varchar(50) null,
    `file_url` varchar(255) null,
    `board_no` int not null,
    primary key (`file_no`)
);
alter table `board_file` add constraint `fk_board_to_board_file_1` foreign key (`board_no`) references `board` (`board_no`);

create table `schedule` (
    `schedule_no` int not null auto_increment,
    `schedule_name` varchar(50) null,
    `schedule_about` text null,
    `schedule_created_date` datetime null,
    `user_id` varchar(50) not null,
    primary key (`schedule_no`)
);
alter table `schedule` add constraint `fk_user_to_schedule_1` foreign key (`user_id`) references `user` (`user_id`);

create table `schedule_detail` (
    `schedule_detail_no` int not null auto_increment,
    `schedule_content` text null,
    `schedule_address` varchar(255) null,
    `latitude` decimal(9, 6) null,
    `longitude` decimal(9, 6) null,
    `schedule_start_time` datetime null,
    `schedule_end_time` datetime null,
    `schedule_no` int not null,
    primary key (`schedule_detail_no`)
);
alter table `schedule_detail` add constraint `fk_schedule_to_schedule_detail_1` foreign key (`schedule_no`) references `schedule` (`schedule_no`);

create table `board_like` (
    `board_no` int not null,
    `user_id` varchar(50) not null,
    primary key (`board_no`, `user_id`)
);
alter table `board_like` add constraint `fk_board_to_board_like_1` foreign key (`board_no`) references `board` (`board_no`);
alter table `board_like` add constraint `fk_user_to_board_like_1` foreign key (`user_id`) references `user` (`user_id`);

create table `comment` (
    `comment_no` int not null auto_increment,
    `comment_content` text null,
    `comment_write_date` datetime null,
    `user_id` varchar(50) not null,
    `board_no` int not null,
    primary key (`comment_no`)
);
alter table `comment` add constraint `fk_user_to_comment_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `comment` add constraint `fk_board_to_comment_1` foreign key (`board_no`) references `board` (`board_no`);

create table `board_hate` (
    `board_no` int not null,
    `user_id` varchar(50) not null,
    primary key (`board_no`, `user_id`)
);
alter table `board_hate` add constraint `fk_board_to_board_hate_1` foreign key (`board_no`) references `board` (`board_no`);
alter table `board_hate` add constraint `fk_user_to_board_hate_1` foreign key (`user_id`) references `user` (`user_id`);

create table `friendship` (
    `friendship_no` int not null auto_increment,
    `user_id` varchar(50) not null,
    `opponent_id` varchar(50) not null,
    `status` enum('ACCEPTED', 'WAITING') not null,
    `is_from` tinyint null,
    `counterpart_friendship_no` int null,
    primary key (`friendship_no`)
);
alter table `friendship` add constraint `fk_user_to_friendship_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `friendship` add constraint `fk_user_to_friendship_2` foreign key (`opponent_id`) references `user` (`user_id`);

create table `place_category` (
    `place_category_no` int auto_increment not null,
    `place_category_code` varchar(20) null,
    `place_category_name` varchar(20) null,
    primary key (`place_category_no`)
);

create table `place_category_detail` (
    `place_category_detail_no` int auto_increment not null,
    `place_category_detail_code` varchar(30) null,
    `place_category_detail_name` varchar(20) null,
    `parent_no` int not null,
    `content_type_id` varchar(2) null,
    `cat1` varchar(3) null,
    `cat2` varchar(5) null,
    `cat3` varchar(9) null,
    `additional_search` tinyint null,
    `search_type` varchar(30) null,
    primary key (`place_category_detail_no`)
);
alter table `place_category_detail` add constraint `fk_place_category_to_place_category_detail_1` foreign key (`parent_no`) references `place_category` (`place_category_no`);

create table `group` (
    `group_no` int auto_increment not null,
    `group_title` varchar(30) null,
    `group_description` varchar(50) null,
    `group_created_date` datetime null,
    `group_created_user_id` varchar(50) not null,
    primary key (`group_no`)
);
alter table `group` add constraint `fk_user_to_group_1` foreign key (`group_created_user_id`) references `user` (`user_id`);

create table `group_member` (
    `group_member_no` int auto_increment not null,
    `group_no` int not null,
    `user_id` varchar(50) not null,
    primary key (`group_member_no`)
);
alter table `group_member` add constraint `fk_group_to_group_member_1` foreign key (`group_no`) references `group` (`group_no`);
alter table `group_member` add constraint `fk_user_to_group_member_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `group_member` add constraint `uq_group_user` unique (`group_no`, `user_id`);

create table `group_invitation` (
    `invitation_no` int auto_increment not null,
    `group_no` int not null,
    `sender_id` varchar(50) not null,
    `receiver_id` varchar(50) not null,
    `status` enum('WAITING', 'ACCEPTED', 'REJECTED') not null,
    `invited_date` datetime null,
    primary key (`invitation_no`)
);
alter table `group_invitation` add constraint `fk_group_to_group_invitation_1` foreign key (`group_no`) references `group` (`group_no`);
alter table `group_invitation` add constraint `fk_user_to_group_invitation_1` foreign key (`sender_id`) references `user` (`user_id`);
alter table `group_invitation` add constraint `fk_user_to_group_invitation_2` foreign key (`receiver_id`) references `user` (`user_id`);
alter table `group_invitation` add constraint `uq_group_receiver` unique (`group_no`, `receiver_id`);

create table `group_board` (
    `group_board_no` int auto_increment not null,
    `group_board_title` varchar(255) null,
    `group_board_content` text null,
    `group_board_write_date` datetime null,
    `group_board_update_date` datetime null,
    `user_id` varchar(50) not null,
    `group_no` int not null,
    primary key (`group_board_no`)
);
alter table `group_board` add constraint `fk_user_to_group_board_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `group_board` add constraint `fk_group_to_group_board_1` foreign key (`group_no`) references `group` (`group_no`);

create table `group_comment` (
    `group_comment_no` int auto_increment not null,
    `group_comment_content` text null,
    `group_comment_write_date` datetime null,
    `user_id` varchar(50) not null,
    `group_board_no` int not null,
    primary key (`group_comment_no`)
);
alter table `group_comment` add constraint `fk_user_to_group_comment_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `group_comment` add constraint `fk_group_board_to_group_comment_1` foreign key (`group_board_no`) references `group_board` (`group_board_no`);

create table `group_schedule` (
    `group_no` int not null,
    `schedule_no` int not null,
    primary key (`group_no`, `schedule_no`)
);
alter table `group_schedule` add constraint `fk_group_to_group_schedule_1` foreign key (`group_no`) references `group` (`group_no`);
alter table `group_schedule` add constraint `fk_schedule_to_group_schedule_1` foreign key (`schedule_no`) references `schedule` (`schedule_no`);

create table `board_scrap` (
    `scrap_no` int auto_increment not null,
    `user_id` varchar(50) not null,
    `board_no` int not null,
    `scrap_date` datetime null,
    primary key (`scrap_no`)
);
alter table `board_scrap` add constraint `fk_user_to_board_scrap_1` foreign key (`user_id`) references `user` (`user_id`);
alter table `board_scrap` add constraint `fk_board_to_board_scrap_1` foreign key (`board_no`) references `board` (`board_no`);
alter table `board_scrap` add constraint `uq_board_scrap` unique (`user_id`, `board_no`);

-- 게시판 view 생성
create view `board_view` as
select
    b.`board_no`,
    b.`user_id`,
    u.`user_nick`,
    u.`user_type`,
    ur.`user_type_name`,
    b.`board_title`,
    b.`board_description`,
    b.`board_view_count`,
    b.`board_write_date`,
    b.`board_update_date`,
    ifnull((select count(*) from `board_like` bl where bl.`board_no` = b.`board_no`), 0) as `board_like`,
    ifnull((select count(*) from `board_hate` bh where bh.`board_no` = b.`board_no`), 0) as `board_hate`,
    b.`category_no`,
    c.`category_name`,
    ifnull((select count(*) from `comment` cm where cm.`board_no` = b.`board_no`), 0) as `comment_count`,
    u.`user_img`,
    -- 썸네일 (대표 이미지 1개만)
    (select bf.`file_url`
     from `board_file` bf
     where bf.`board_no` = b.`board_no`
     order by bf.`file_no`
     limit 1
    ) as `thumbnail_url`
from `board` b
    join `user` u on b.`user_id` = u.`user_id`
    join `user_role` ur on u.`user_type` = ur.`user_type`
    join `category` c on b.`category_no` = c.`category_no`;

insert into `user` (`user_id`, `user_passwd`, `user_type`, `user_email`, `user_nick`, 'only_friends_can_see_activity', 'email_notification_agree', 'push_notification_agree')
values
    ('user1', '$2a$10$36vCUK5vsfQ6/HK2HGatdOJ3x3RoH.6da2WESPsQ8zVKHqMB96Mle', 1, 'user1@example.com', '사용자1', 0, 0, 0);

insert into `user_role` (`user_type`, `user_type_name`)
values
    (0, 'Admin'),
    (1, 'User');

insert into `place_category` (`place_category_code`, `place_category_name`) values
    ('tour', '관광지'),
    ('food', '음식점'),
    ('cafe', '카페'),
    ('convenience-store', '편의점'),
    ('shopping', '쇼핑'),
    ('culture', '문화시설'),
    ('event', '공연/행사'),
    ('other', '기타');

insert into `place_category_detail` (`place_category_detail_code`, `place_category_detail_name`, `parent_no`, `content_type_id`, `cat1`, `cat2`, `cat3`, `additional_search`, `search_type`) values
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
2. 프로필 사진 변경을 사용해보려면 `Apidog->외부 API->S3 API->Headers`의 키를 `resources->cloud.aws.credentials.access-key`와 `cloud.aws.credentials.secret-key`에 붙여넣습니다.
---
3. AI 추천 스케줄 기능을 사용해보려면 `Apidog->외부 API->OpenAI API->Headers->Authorization`의 키를 'Bearer '를 제거해 resources->application.properties의 openai.api.key에 붙여넣습니다.


---


# 방법2
1. 다음 sql문을 적용합니다.(**기존 주의사항 1~3에 해당**, 이미 적용했으면 2번으로)
```mysql
UPDATE user
SET user_passwd = '$2a$10$36vCUK5vsfQ6/HK2HGatdOJ3x3RoH.6da2WESPsQ8zVKHqMB96Mle'
WHERE user_id IN (
  'admin1', 'admin2', 'admin3',
  'user1', 'user2', 'user3', 'user4', 'user5',
  'user6', 'user7', 'user8', 'user9', 'user10'
);

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

ALTER TABLE BOARD ADD COLUMN BOARD_DESCRIPTION VARCHAR(255);

DROP VIEW `BOARD_VIEW`;

CREATE VIEW `BOARD_VIEW` AS
SELECT
  B.`BOARD_NO`,
  B.`USER_ID`,
  U.`USER_NICK`,
  U.`USER_TYPE`,
  UR.`USER_TYPE_NAME`,
  B.`BOARD_TITLE`,
  B.`BOARD_DESCRIPTION`,
  B.`BOARD_VIEW_COUNT`,
  B.`BOARD_WRITE_DATE`,
  B.`BOARD_UPDATE_DATE`,
  IFNULL((SELECT COUNT(*) FROM `BOARD_LIKE` BL WHERE BL.`BOARD_NO` = B.`BOARD_NO`), 0) AS `BOARD_LIKE`,
  IFNULL((SELECT COUNT(*) FROM `BOARD_HATE` BH WHERE BH.`BOARD_NO` = B.`BOARD_NO`), 0) AS `BOARD_HATE`,
  B.`CATEGORY_NO`,
  C.`CATEGORY_NAME`,
  IFNULL((SELECT COUNT(*) FROM `COMMENT` CM WHERE CM.`BOARD_NO` = B.`BOARD_NO`), 0) AS `COMMENT_COUNT`,
  U.`USER_IMG`
FROM `BOARD` B
       JOIN `USER` U ON B.`USER_ID` = U.`USER_ID`
       JOIN `USER_ROLE` UR ON U.`USER_TYPE` = UR.`USER_TYPE`
       JOIN `CATEGORY` C ON B.`CATEGORY_NO` = C.`CATEGORY_NO`;
```
2. 다음 sql문을 적용합니다. (이미 적용했으면 3번으로)
```mysql
CREATE TABLE `GROUP` (
    `GROUP_NO` INT AUTO_INCREMENT NOT NULL,
    `GROUP_TITLE` VARCHAR(30) NULL,
    `GROUP_DESCRIPTION` VARCHAR(50) NULL,
    `GROUP_CREATED_DATE` DATETIME NULL,
    `GROUP_CREATED_USER_ID` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`GROUP_NO`)
);
ALTER TABLE `GROUP` ADD CONSTRAINT `FK_USER_TO_GROUP_1` FOREIGN KEY (`GROUP_CREATED_USER_ID`) REFERENCES `USER` (`USER_ID`);

CREATE TABLE `GROUP_MEMBER` (
    `GROUP_MEMBER_NO` INT AUTO_INCREMENT NOT NULL,
    `GROUP_NO` INT NOT NULL,
    `USER_ID` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`GROUP_MEMBER_NO`)
);
ALTER TABLE `GROUP_MEMBER` ADD CONSTRAINT `FK_GROUP_TO_GROUP_MEMBER_1` FOREIGN KEY (`GROUP_NO`) REFERENCES `GROUP` (`GROUP_NO`);
ALTER TABLE `GROUP_MEMBER` ADD CONSTRAINT `FK_USER_TO_GROUP_MEMBER_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`);
ALTER TABLE `GROUP_MEMBER` ADD CONSTRAINT `UQ_GROUP_USER` UNIQUE (`GROUP_NO`, `USER_ID`);

CREATE TABLE `GROUP_INVITATION` (
    `INVITATION_NO` INT AUTO_INCREMENT NOT NULL,
    `GROUP_NO` INT NOT NULL,
    `SENDER_ID` VARCHAR(50) NOT NULL,
    `RECEIVER_ID` VARCHAR(50) NOT NULL,
    `STATUS` ENUM('WAITING', 'ACCEPTED', 'REJECTED') NOT NULL,
    `INVITED_DATE` DATETIME NULL,
    PRIMARY KEY (`INVITATION_NO`)
);
ALTER TABLE `GROUP_INVITATION` ADD CONSTRAINT `FK_GROUP_TO_GROUP_INVITATION_1` FOREIGN KEY (`GROUP_NO`) REFERENCES `GROUP` (`GROUP_NO`);
ALTER TABLE `GROUP_INVITATION` ADD CONSTRAINT `FK_USER_TO_GROUP_INVITATION_1` FOREIGN KEY (`SENDER_ID`) REFERENCES `USER` (`USER_ID`);
ALTER TABLE `GROUP_INVITATION` ADD CONSTRAINT `FK_USER_TO_GROUP_INVITATION_2` FOREIGN KEY (`RECEIVER_ID`) REFERENCES `USER` (`USER_ID`);
ALTER TABLE `GROUP_INVITATION` ADD CONSTRAINT `UQ_GROUP_RECEIVER` UNIQUE (`GROUP_NO`, `RECEIVER_ID`);

CREATE TABLE `GROUP_BOARD` (
	`GROUP_BOARD_NO` INT AUTO_INCREMENT NOT NULL,
    `GROUP_BOARD_TITLE` VARCHAR(255) NULL,
    `GROUP_BOARD_CONTENT` TEXT NULL,
    `GROUP_BOARD_WRITE_DATE` DATETIME NULL,
    `GROUP_BOARD_UPDATE_DATE` DATETIME NULL,
    `USER_ID` VARCHAR(50) NOT NULL,
    `GROUP_NO` INT NOT NULL,
    PRIMARY KEY (`GROUP_BOARD_NO`)
);
ALTER TABLE `GROUP_BOARD` ADD CONSTRAINT `FK_USER_TO_GROUP_BOARD_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`);
ALTER TABLE `GROUP_BOARD` ADD CONSTRAINT `FK_GROUP_TO_GROUP_BOARD_1` FOREIGN KEY (`GROUP_NO`) REFERENCES `GROUP` (`GROUP_NO`);

CREATE TABLE `GROUP_COMMENT` (
	`GROUP_COMMENT_NO` INT AUTO_INCREMENT NOT NULL,
    `GROUP_COMMENT_CONTENT` TEXT NULL,
    `GROUP_COMMENT_WRITE_DATE` DATETIME NULL,
    `USER_ID` VARCHAR(50) NOT NULL,
    `GROUP_BOARD_NO` INT NOT NULL,
    PRIMARY KEY (`GROUP_COMMENT_NO`)
);
ALTER TABLE `GROUP_COMMENT` ADD CONSTRAINT `FK_USER_TO_GROUP_COMMENT_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`);
ALTER TABLE `GROUP_COMMENT` ADD CONSTRAINT `FK_GROUP_BOARD_TO_GROUP_COMMENT_1` FOREIGN KEY (`GROUP_BOARD_NO`) REFERENCES `GROUP_BOARD` (`GROUP_BOARD_NO`);

CREATE TABLE `GROUP_SCHEDULE` (
                                `GROUP_NO` INT NOT NULL,
                                `SCHEDULE_NO` INT NOT NULL,
                                PRIMARY KEY (`GROUP_NO`, `SCHEDULE_NO`)
);
ALTER TABLE `GROUP_SCHEDULE` ADD CONSTRAINT `FK_GROUP_TO_GROUP_SCHEDULE_1` FOREIGN KEY (`GROUP_NO`) REFERENCES `GROUP` (`GROUP_NO`);
ALTER TABLE `GROUP_SCHEDULE` ADD CONSTRAINT `FK_SCHEDULE_TO_GROUP_SCHEDULE_1` FOREIGN KEY (`SCHEDULE_NO`) REFERENCES `SCHEDULE` (`SCHEDULE_NO`);

DROP TABLE `GROUP_USER`;
```

3. 프로필 사진 변경을 사용해보려면 `Apidog->외부 API->S3 API->Headers`의 키를 `resources->cloud.aws.credentials.access-key`와 `cloud.aws.credentials.secret-key`에 붙여넣습니다.
---
4. AI 추천 스케줄 기능을 사용해보려면 `Apidog->외부 API->OpenAI API->Headers->Authorization`의 키를 'Bearer '를 제거해 resources->application.properties의 openai.api.key에 붙여넣습니다.
---
5. 다음 sql문을 적용합니다.
```mysql
ALTER TABLE `USER` ADD COLUMN `ONLY_FRIENDS_CAN_SEE_ACTIVITY` TINYINT NULL;
ALTER TABLE `USER` ADD COLUMN `EMAIL_NOTIFICATION_AGREE` TINYINT NULL;
ALTER TABLE `USER` ADD COLUMN `PUSH_NOTIFICATION_AGREE` TINYINT NULL;

CREATE TABLE `BOARD_SCRAP` (
    `SCRAP_NO` INT AUTO_INCREMENT NOT NULL,
    `USER_ID` VARCHAR(50) NOT NULL,
    `BOARD_NO` INT NOT NULL,
    `SCRAP_DATE` DATETIME NULL,
    PRIMARY KEY (`SCRAP_NO`)
);
ALTER TABLE `BOARD_SCRAP` ADD CONSTRAINT `FK_USER_TO_BOARD_SCRAP_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER`(`USER_ID`);
ALTER TABLE `BOARD_SCRAP` ADD CONSTRAINT `FK_BOARD_TO_BOARD_SCRAP_1` FOREIGN KEY (`BOARD_NO`) REFERENCES `BOARD`(`BOARD_NO`);
ALTER TABLE `BOARD_SCRAP` ADD CONSTRAINT `UQ_BOARD_SCRAP` UNIQUE (`USER_ID`, `BOARD_NO`);

DROP VIEW `BOARD_VIEW`;

CREATE VIEW `BOARD_VIEW` AS
SELECT
    B.`BOARD_NO`,
    B.`USER_ID`,
    U.`USER_NICK`,
    U.`USER_TYPE`,
    UR.`USER_TYPE_NAME`,
    B.`BOARD_TITLE`,
    B.`BOARD_DESCRIPTION`,
    B.`BOARD_VIEW_COUNT`,
    B.`BOARD_WRITE_DATE`,
    B.`BOARD_UPDATE_DATE`,
    IFNULL((SELECT COUNT(*) FROM `BOARD_LIKE` BL WHERE BL.`BOARD_NO` = B.`BOARD_NO`), 0) AS `BOARD_LIKE`,
    IFNULL((SELECT COUNT(*) FROM `BOARD_HATE` BH WHERE BH.`BOARD_NO` = B.`BOARD_NO`), 0) AS `BOARD_HATE`,
    B.`CATEGORY_NO`,
    C.`CATEGORY_NAME`,
    IFNULL((SELECT COUNT(*) FROM `COMMENT` CM WHERE CM.`BOARD_NO` = B.`BOARD_NO`), 0) AS `COMMENT_COUNT`,
    U.`USER_IMG`,
    -- 썸네일 (대표 이미지 1개만)
    (SELECT BF.`FILE_URL`
     FROM `BOARD_FILE` BF
     WHERE BF.`BOARD_NO` = B.`BOARD_NO`
     ORDER BY BF.`FILE_NO`
     LIMIT 1
    ) AS `THUMBNAIL_URL`
FROM `BOARD` B
    JOIN `USER` U ON B.`USER_ID` = U.`USER_ID`
    JOIN `USER_ROLE` UR ON U.`USER_TYPE` = UR.`USER_TYPE`
    JOIN `CATEGORY` C ON B.`CATEGORY_NO` = C.`CATEGORY_NO`;

ALTER TABLE `BOARD` ADD COLUMN `SCHEDULE_NO` INT NULL;
ALTER TABLE `BOARD` ADD CONSTRAINT `FK_SCHEDULE_TO_BOARD_1` FOREIGN KEY (`SCHEDULE_NO`) REFERENCES `SCHEDULE` (`SCHEDULE_NO`);
```
---
6. user 테이블의 모든 계정(레코드)들에 ONLY_FRIENDS_CAN_SEE_ACTIVITY, EMAIL_NOTIFICATION_AGREE, PUSH_NOTIFICATION_AGREE 값을 모두 0으로 채워주셔야 합니다.
7. 앞으로 테이블이 추가나 변경될 상황이 생기면 여기에 추가할 예정입니다...