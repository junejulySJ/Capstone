## 📚 목차
- **Home**
- [User API](docs/UserAPI.md) [(Example)](docs/UserAPIDetail.md)
- [Auth API](docs/AuthAPI.md)
- [Friendship API](docs/FriendshipAPI.md)
- [Map API](docs/MapAPI.md) [(Example)](docs/MapAPIDetail.md)
- [Schedule API](docs/ScheduleAPI.md) [(Example)](docs/ScheduleAPIDetail.md)
- [Path API](docs/PathAPI.md) [(Example)](docs/PathAPIDetail.md)
- [Board API](docs/BoardAPI.md) [(Example)](docs/BoardAPIDetail.md)
- [Comment API](docs/CommentAPI.md) [(Example)](docs/CommentAPIDetail.md)
- [Group API](docs/GroupAPI.md) [(Example)](docs/GroupAPIDetail.md)
- [GroupBoard API](docs/GroupBoardAPI.md) [(Example)](docs/GroupBoardAPIDetail.md)
- [GroupComment API](docs/GroupCommentAPI.md) [(Example)](docs/GroupCommentAPIDetail.md)

## 📝 REST API 목록
| API 호출                                                  | 기능                 |
|---------------------------------------------------------|--------------------|
| `POST /user/check-id`                                   | 아이디 중복 검사          |
| `POST /user/register`                                   | 회원가입               |
| `GET /user`                                             | 회원 정보 조회           |
| `GET /user/list`                                        | 전체 회원 조회           |
| `PUT /user`                                             | 회원 정보 변경           |
| `GET /user/boards`                                      | 작성한 글 조회           |
| `GET /user/boards/liked`                                | 좋아요한 글 조회          |
| `GET /user/boards/scraped`                              | 저장한 글 조회           |
| `GET /user/groups`                                      | 속한 그룹 조회           |
| `POST /user/password`                                   | 비밀번호 변경            |
| `DELETE /user`                                          | 회원 탈퇴              |
| `POST /auth/login`                                      | 로그인                |
| `POST /auth/kakao`                                      | 카카오 로그인            |
| `POST /auth/logout`                                     | 로그아웃               |
| `GET /boards`                                           | 게시글 조회             |
| `GET /boards/category`                                  | 카테고리 조회            |
| `GET /boards/{boardNo}`                                 | 게시글 상세 조회          |
| `POST /boards`                                          | 게시글 작성             |
| `PUT /boards/{boardNo}`                                 | 게시글 수정             |
| `DELETE /boards/{boardNo}`                              | 게시글 삭제             |
| `POST /boards/{boardNo}/like`                           | 좋아요 토글             |
| `POST /boards/{boardNo}/hate`                           | 싫어요 토글             |
| `POST /boards/{boardNo}/scrap`                          | 저장(스크랩) 토글         |
| `GET /user/friends`                                     | 친구 목록 조회           |
| `GET /user/friends/received`                            | 받은 친구 요청 조회        |
| `POST /user/friends/add`                                | 친구 추가              |
| `POST /user/friends/approve`                            | 친구 요청 수락           |
| `DELETE /user/friends`                                  | 친구 삭제              |
| `GET /map/category`                                     | 카테고리 조회            |
| `GET /map`                                              | 장소 조회              |
| `GET /map/detail`                                       | 장소 세부 정보 조회        |
| `GET /map/autocomplete`                                 | 장소명 자동완성           |
| `POST /path/pedestrian`                                 | 스케줄로 보행자 길찾기       |
| `POST /path/car`                                        | 스케줄로 자동차 길찾기       |
| `POST /path/transit`                                    | 스케줄로 대중교통 길찾기      |
| `GET /path/pedestrian`                                  | 장소 이름으로 보행자 길찾기    |
| `GET /path/car`                                         | 장소 이름으로 자동차 길찾기    |
| `GET /path/transit`                                     | 장소 이름으로 대중교통 길찾기   |
| `GET /schedules`                                        | 자신이 만든 스케줄 조회      |
| `GET /schedules/{scheduleNo}/members`                   | 스케줄에 참여한 회원 조회     |
| `POST /schedules/create`                                | 스케줄 생성             |
| `POST /schedules`                                       | 스케줄 저장             |
| `PUT /schedules`                                        | 스케줄 수정             |
| `DELETE /schedules/{scheduleNo}`                        | 스케줄 삭제             |
| `POST /schedules/share`                                 | 스케줄 공유             |
| `POST /schedules/unshare`                               | 스케줄 공유 취소          |
| `GET /boards/{boardNo}/comments`                        | 특정 게시글 댓글 조회       |
| `POST /boards/{boardNo}/comments`                       | 댓글 등록              |
| `PUT /comments/{commentNo}`                             | 댓글 수정              |
| `DELETE /comments/{commentNo}`                          | 댓글 삭제              |
| `GET /groups/{groupNo}`                                 | 그룹 조회              |
| `GET /groups/{groupNo}/members`                         | 그룹 멤버 조회           |
| `GET /groups/members`                                   | 소속되어있는 전체 그룹 멤버 조회 |
| `POST /groups`                                          | 그룹 생성              |
| `PUT /groups/{groupNo}`                                 | 그룹 수정              |
| `DELETE /groups/{groupNo}`                              | 그룹 삭제              |
| `DELETE /groups/{groupNo}/members/{deleteUserId}`       | 그룹 멤버 강제 탈퇴        |
| `POST /groups/invitations`                              | 그룹 초대              |
| `GET /groups/invitations`                               | 그룹 초대 목록 조회        |
| `POST /groups/invitations/{invitationNo}/{status}`      | 그룹 초대 수락/거절        |
| `POST /groups/{groupNo}/schedules`                      | 그룹 내 스케줄 공유        |
| `GET /groups/{groupNo}/schedules`                       | 그룹 내 공유된 스케줄 조회    |
| `DELETE /groups/{groupNo}/schedules/{scheduleNo}`       | 공유 스케줄 삭제          |
| `GET /groups/{groupNo}/boards`                          | 그룹 전체 게시글 조회       |
| `POST /groups/{groupNo}/boards`                         | 그룹 게시글 등록          |
| `DELETE /groups/{groupNo}/boards/{groupBoardNo}`        | 그룹 게시글 삭제          |
| `POST /groups/{groupNo}/boards/{groupBoardNo}/comments` | 그룹 게시글 댓글 등록       |