## 📚 목차
- **Home**
- [User API](docs/UserAPI.md) [(Example)](docs/UserAPIDetail.md)
- [Auth API](docs/AuthAPI.md)
- [Board API](docs/BoardAPI.md) [(Example)](docs/BoardAPIDetail.md)
- [Friendship API](docs/FriendshipAPI.md)
- [Map API](docs/MapAPI.md) [(Example)](docs/MapAPIDetail.md)
- [Path API](docs/PathAPI.md) [(Example)](docs/PathAPIDetail.md)
- [Schedule API](docs/ScheduleAPI.md) [(Example)](docs/ScheduleAPIDetail.md)
- [Comment API](docs/CommentAPI.md) [(Example)](docs/CommentAPIDetail.md)
- [Group API](docs/GroupAPI.md) [(Example)](docs/GroupAPIDetail.md)
- [GroupBoard API](docs/GroupBoardAPI.md) [(Example)](docs/GroupBoardAPIDetail.md)

## 📝 REST API 목록
| API 호출                                                                                      | 기능                 |
|---------------------------------------------------------------------------------------------|--------------------|
| [POST /user/check-id](docs/UserAPI.md#아이디-중복-검사)                                            | 아이디 중복 검사          |
| [POST /user/register](docs/UserAPI.md#회원가입)                                                 | 회원가입               |
| [GET /user](docs/UserAPI.md#회원-정보-조회)                                                       | 회원 정보 조회           |
| [GET /user/list](docs/UserAPI.md#전체-회원-조회)                                                  | 전체 회원 조회           |
| [PUT /user](docs/UserAPI.md#회원-정보-변경)                                                       | 회원 정보 변경           |
| [GET /user/boards](docs/UserAPI.md#작성한-글-조회)                                                | 작성한 글 조회           |
| [GET /user/boards/liked](docs/UserAPI.md#좋아요한-글-조회)                                         | 좋아요한 글 조회          |
| [GET /user/boards/scraped](docs/UserAPI.md#저장한-글-조회)                                        | 저장한 글 조회           |
| [GET /user/groups](docs/UserAPI.md#속한-그룹-조회)                                                | 속한 그룹 조회           |
| [POST /user/password](docs/UserAPI.md#비밀번호-변경)                                              | 비밀번호 변경            |
| [DELETE /user](docs/UserAPI.md#회원-탈퇴)                                                       | 회원 탈퇴              |
| [POST /auth/login](docs/AuthAPI.md#로그인)                                                     | 로그인                |
| [POST /auth/kakao](docs/AuthAPI.md#카카오-로그인)                                                 | 카카오 로그인            |
| [POST /auth/logout](docs/AuthAPI.md#로그아웃)                                                   | 로그아웃               |
| [GET /boards](docs/BoardAPI.md#게시글-조회)                                                      | 게시글 조회             |
| [GET /boards/category](docs/BoardAPI.md#카테고리-조회)                                            | 카테고리 조회            |
| [GET /boards/{boardNo}](docs/BoardAPI.md#게시글-상세-조회)                                         | 게시글 상세 조회          |
| [POST /boards](docs/BoardAPI.md#게시글-등록)                                                     | 게시글 등록             |
| [PUT /boards/{boardNo}](docs/BoardAPI.md#게시글-수정)                                            | 게시글 수정             |
| [DELETE /boards/{boardNo}](docs/BoardAPI.md#게시글-삭제)                                         | 게시글 삭제             |
| [POST /boards/{boardNo}/like](docs/BoardAPI.md#좋아요-토글)                                      | 좋아요 토글             |
| [POST /boards/{boardNo}/hate](docs/BoardAPI.md#싫어요-토글)                                      | 싫어요 토글             |
| [POST /boards/{boardNo}/scrap](docs/BoardAPI.md#저장스크랩-토글)                                   | 저장(스크랩) 토글         |
| [GET /user/friends](docs/FriendshipAPI.md#친구-목록-조회)                                         | 게시글 조회             |
| [GET /user/friends/sent](docs/FriendshipAPI.md#보낸-친구-요청-조회)                                 | 보낸 친구 요청 조회        |
| [GET /user/friends/received](docs/FriendshipAPI.md#받은-친구-요청-조회)                             | 받은 친구 요청 조회        |
| [POST /user/friends/add](docs/FriendshipAPI.md#친구-추가)                                       | 친구 추가              |
| [POST /user/friends/approve](docs/FriendshipAPI.md#친구-요청-수락)                                | 친구 요청 수락           |
| [DELETE /user/friends](docs/FriendshipAPI.md#친구-삭제)                                         | 친구 삭제              |
| [GET /map/category](docs/MapAPI.md#카테고리-조회)                                                 | 카테고리 조회            |
| [GET /map](docs/MapAPI.md#지도-출력)                                                            | 지도 출력              |
| [GET /map/detail](docs/MapAPI.md#장소-세부-정보-출력)                                               | 장소 세부 정보 출력        |
| [GET /map/autocomplete](docs/MapAPI.md#장소명-자동완성)                                            | 장소명 자동완성           |
| [POST /path/pedestrian](docs/PathAPI.md#스케줄로-보행자-길찾기)                                       | 스케줄로 보행자 길찾기       |
| [POST /path/car](docs/PathAPI.md#스케줄로-자동차-길찾기)                                              | 스케줄로 자동차 길찾기       |
| [POST /path/transit](docs/PathAPI.md#스케줄로-대중교통-길찾기)                                         | 스케줄로 대중교통 길찾기      |
| [GET /path/pedestrian](docs/PathAPI.md#장소-이름으로-보행자-길찾기)                                     | 장소 이름으로 보행자 길찾기    |
| [GET /path/car](docs/PathAPI.md#장소-이름으로-자동차-길찾기)                                            | 장소 이름으로 자동차 길찾기    |
| [GET /path/transit](docs/PathAPI.md#장소-이름으로-대중교통-길찾기)                                       | 장소 이름으로 대중교통 길찾기   |
| [GET /schedules](docs/ScheduleAPI.md#자신이-만든-스케줄-조회)                                         | 자신이 만든 스케줄 조회      |
| [POST /schedules/create](docs/ScheduleAPI.md#스케줄-생성)                                        | 스케줄 생성             |
| [POST /schedules](docs/ScheduleAPI.md#스케줄-저장)                                               | 스케줄 저장             |
| [PUT /schedules](docs/ScheduleAPI.md#스케줄-수정)                                                | 스케줄 수정             |
| [DELETE /schedules/{scheduleNo}](docs/ScheduleAPI.md#스케줄-삭제)                                | 스케줄 삭제             |
| [GET /boards/{boardNo}/comments](docs/CommentAPI.md#특정-게시글-댓글-조회)                           | 특정 게시글 댓글 조회       |
| [POST /boards/{boardNo}/comments](docs/CommentAPI.md#댓글-등록)                                 | 댓글 등록              |
| [PUT /comments/{commentNo}](docs/CommentAPI.md#댓글-수정)                                       | 댓글 수정              |
| [DELETE /comments/{commentNo}](docs/CommentAPI.md#댓글-삭제)                                    | 댓글 삭제              |
| [GET /groups/{groupNo}](docs/GroupAPI.md#그룹-조회)                                             | 그룹 조회              |
| [GET /groups/{groupNo}/members](docs/GroupAPI.md#그룹-멤버-조회)                                  | 그룹 멤버 조회           |
| [GET /groups/members](docs/GroupAPI.md#소속되어있는-전체-그룹-멤버-조회)                                  | 소속되어있는 전체 그룹 멤버 조회 |
| [POST /groups](docs/GroupAPI.md#그룹-생성)                                                      | 그룹 생성              |
| [PUT /groups/{groupNo}](docs/GroupAPI.md#그룹-수정)                                             | 그룹 수정              |
| [DELETE /groups/{groupNo}](docs/GroupAPI.md#그룹-삭제)                                          | 그룹 삭제              |
| [DELETE /groups/{groupNo}/members/{deleteUserId}](docs/GroupAPI.md#그룹-멤버-강제-탈퇴)             | 그룹 멤버 강제 탈퇴        |
| [POST /groups/invitations](docs/GroupAPI.md#그룹-초대)                                          | 그룹 초대              |
| [GET /groups/invitations](docs/GroupAPI.md#그룹-초대-목록-조회)                                     | 그룹 초대 목록 조회        |
| [POST /groups/invitations/{invitationNo}/{status}](docs/GroupAPI.md#그룹-초대-수락거절)             | 그룹 초대 수락/거절        |
| [POST /groups/{groupNo}/schedules](docs/GroupAPI.md#그룹-내-스케줄-공유)                            | 그룹 내 스케줄 공유        |
| [GET /groups/{groupNo}/schedules](docs/GroupAPI.md#그룹-내-공유된-스케줄-조회)                         | 그룹 내 공유된 스케줄 조회    |
| [DELETE /groups/{groupNo}/schedules/{scheduleNo}](docs/GroupAPI.md#공유-스케줄-삭제)               | 공유 스케줄 삭제          |
| [GET /groups/{groupNo}/boards](docs/GroupBoardAPI.md#그룹-전체-게시글-조회)                          | 그룹 전체 게시글 조회       |
| [POST /groups/{groupNo}/boards](docs/GroupBoardAPI.md#그룹-게시글-등록)                            | 그룹 게시글 등록          |
| [DELETE /groups/{groupNo}/boards/{groupBoardNo}](docs/GroupBoardAPI.md#그룹-게시글-삭제)           | 그룹 게시글 삭제          |
| [POST /groups/{groupNo}/boards/{groupBoardNo}/comments](docs/GroupBoardAPI.md#그룹-게시글-댓글-등록) | 그룹 게시글 댓글 등록       |