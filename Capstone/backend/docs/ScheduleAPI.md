## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md)
- **Schedule API**
- [Path API](PathAPI.md)

# Schedule API
주요 기능:
- 스케줄 생성
- 스케줄 조회
- 스케줄 상세 정보 조회
- 스케줄 저장
- 스케줄 수정
- 스케줄 삭제
- 스케줄에 참여한 회원 조회
- 스케줄 공유
- 스케줄 공유 취소

---

## API 목록

<details>
<summary>스케줄 생성 ✏️</summary>

**POST** `/schedule/create`

> 사용자로부터 입력받은 데이터를 통해 스케줄을 생성합니다.  
> 쿼리 파라미터 종류는 다음과 같습니다.

| 파라미터                     | 설명                                             | 값 예시                |
|--------------------------|------------------------------------------------|---------------------|
| selectedPlace            | 선택한 장소들(최소 1개 이상 필요)                           | {...}               |
| stayMinutes              | 장소별 머무는 시간(분 단위)                               | 60                  |
| scheduleName             | 스케줄 이름                                         | 종로구 데이트             |
| scheduleAbout            | 스케줄 설명                                         | 종로구 데이트하기 좋은 스케줄    |
| scheduleStartTime        | 스케줄 시작 시간(input type="datetime-local")         | 2025-06-01T10:00:00 |
| scheduleEndTime          | 스케줄 종료 시간(input type="datetime-local")         | 2025-06-01T18:00:00 |
| transport                | 이동 수단                                          | 도보, 자동차             |
| additionalRecommendation | 추가 추천 여부                                       | true/false          |
| totalPlaceCount          | (추가 추천 여부가 true면) 선택한 장소를 포함한 스케줄에 포함될 총 장소 개수 | 4                   |
| theme                    | (추가 추천 여부가 true면) 추천에 사용될 테마                   | date 등              |
| stayMinutesMean          | (추가 추천 여부가 true면) 추천될 장소들의 평균 머무는 시간(분 단위)     | 60                  |

> theme 종류는 다음과 같습니다.

| theme    | 설명   | 카테고리                             |
|----------|------|----------------------------------|
| tour     | 관광   | tour                             |
| nature   | 자연힐링 | tour-nature                      |
| history  | 역사탐방 | tour-tradition                   |
| food     | 음식투어 | food                             |
| shopping | 쇼핑   | shopping                         |
| date     | 데이트  | tour-park, tour-theme-park, cafe |


#### 요청 바디
```json
{
  "selectedPlace": [
    {
      "contentId": "1945693",
      "address": "서울특별시 종로구 인사동10길 11-4",
      "name": "전통다원",
      "latitude": "37.5745839959",
      "longitude": "126.9857145803",
      "category": "cafe",
      "stayMinutes": 60
    }
  ],
  "scheduleName": "test",
  "scheduleAbout": "testabout",
  "scheduleStartTime": "2025-05-14T10:00:00",
  "scheduleEndTime": "2025-05-14T18:00:00",
  "startContentId": "1945693",
  "transport": "자동차",
  "additionalRecommendation": true,
  "totalPlaceCount": 4,
  "theme": "date",
  "stayMinutesMean": 60
}
```

#### 응답 바디
```json
{
  "scheduleName": "test",
  "scheduleAbout": "testabout",
  "details": [
    {
      "scheduleContent": "전통다원 방문",
      "scheduleAddress": "서울특별시 종로구 인사동10길 11-4",
      "latitude": 37.5745839959,
      "longitude": 126.9857145803,
      "scheduleStartTime": "2025-05-14T10:00:00",
      "scheduleEndTime": "2025-05-14T11:00:00"
    },
    {
      "scheduleContent": "다동커피집 방문",
      "scheduleAddress": "중구 다동길 24-8",
      "latitude": 37.5673387,
      "longitude": 126.9806302,
      "scheduleStartTime": "2025-05-14T11:30:00",
      "scheduleEndTime": "2025-05-14T12:30:00"
    },
    {
      "scheduleContent": "진옥화할매원조닭한마리 방문",
      "scheduleAddress": "서울특별시 종로구 종로40가길 18 (종로5가) ",
      "latitude": 37.5704292825,
      "longitude": 127.0057128756,
      "scheduleStartTime": "2025-05-14T13:00:00",
      "scheduleEndTime": "2025-05-14T14:00:00"
    },
    {
      "scheduleContent": "장충단공원 방문",
      "scheduleAddress": "서울특별시 중구 동호로 261 (장충동2가) ",
      "latitude": 37.5588195041,
      "longitude": 127.0048160637,
      "scheduleStartTime": "2025-05-14T14:30:00",
      "scheduleEndTime": "2025-05-14T15:30:00"
    }
  ]
}
```
</details>

---

<details>
<summary>스케줄 조회</summary>

**GET** `/schedules`

> 회원의 스케줄을 조회합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/schedules`, {
        withCredentials: true,
    })
```

#### 응답 바디
```json
[
  {
    "scheduleNo": 1,
    "scheduleName": "스터디 회의",
    "scheduleAbout": "다음 주 프로젝트 스터디 회의 진행",
    "scheduleCreatedDate": "2025-03-04T10:00:00",
    "userId": "user1"
  }
]
```
</details>

---

<details>
<summary>스케줄 상세 정보 조회</summary>

**GET** `/schedules/{scheduleNo}/details`

> 회원의 스케줄의 상세 정보를 조회합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .get(`${API_BASE_URL}/schedules/${scheduleNo}/details`, {
        withCredentials: true,
    })
```

#### 응답 바디
```json
[
  {
    "scheduleDetailNo": 1,
    "scheduleContent": "스터디 장소 도착",
    "scheduleAddress": "서울특별시 마포구 와우산로 94",
    "latitude": 37.550900,
    "longitude": 126.925300,
    "scheduleStartTime": "2025-03-04T13:00:00",
    "scheduleEndTime": "2025-03-04T13:30:00",
    "scheduleNo": 1
  },
  {
    "scheduleDetailNo": 2,
    "scheduleContent": "팀 회의 시작",
    "scheduleAddress": "홍대입구역 근처 카페",
    "latitude": 37.550400,
    "longitude": 126.926000,
    "scheduleStartTime": "2025-03-04T13:30:00",
    "scheduleEndTime": "2025-03-04T15:00:00",
    "scheduleNo": 1
  }
]
```
</details>

---

<details>
<summary>스케줄 저장</summary>

**POST** `/schedules`

> 생성한 스케줄을 저장합니다.
> /schedules/create로 만든 데이터를 그대로 바디에 넣으면 됩니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .post(`${API_BASE_URL}/schedules`, {
        withCredentials: true,
    })
```

#### 요청 바디
```json
{
  "scheduleName": "test",
  "scheduleAbout": "testabout",
  "details": [
    {
      "scheduleContent": "일상비일상의틈 방문",
      "scheduleAddress": "서울특별시 강남구 강남대로 426 (역삼동) ",
      "latitude": 37.5006405461,
      "longitude": 127.0267851551,
      "scheduleStartTime": "2025-05-07T10:00:00",
      "scheduleEndTime": "2025-05-07T11:00:00"
    },
    {
      "scheduleContent": "메가박스 코엑스 방문",
      "scheduleAddress": "대한민국 서울특별시 강남구 봉은사로 524",
      "latitude": 37.5126572,
      "longitude": 127.0586523,
      "scheduleStartTime": "2025-05-07T11:47:00",
      "scheduleEndTime": "2025-05-07T12:47:00"
    },
    {
      "scheduleContent": "우텐더 방문",
      "scheduleAddress": "서울특별시 강남구 압구정로42길 25-10 1~2층",
      "latitude": 37.5270487520,
      "longitude": 127.0358085855,
      "scheduleStartTime": "2025-05-07T13:26:00",
      "scheduleEndTime": "2025-05-07T14:26:00"
    },
    {
      "scheduleContent": "청담근린공원 방문",
      "scheduleAddress": "서울특별시 강남구 청담동 66",
      "latitude": 37.5213524935,
      "longitude": 127.0526155502,
      "scheduleStartTime": "2025-05-07T14:50:00",
      "scheduleEndTime": "2025-05-07T15:50:00"
    }
  ]
}
```
</details>

---

<details>
<summary>스케줄 수정</summary>

**PUT** `/schedules`

> 스케줄을 수정해서 저장합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .put(`${API_BASE_URL}/schedules/${scheduleNo}`, {
        withCredentials: true,
    })
```

#### 요청 바디
```json
{
  "scheduleName": "test",
  "scheduleAbout": "testabout",
  "details": [
    {
      "scheduleContent": "일상비일상의틈 방문",
      "scheduleAddress": "서울특별시 강남구 강남대로 426 (역삼동) ",
      "latitude": 37.5006405461,
      "longitude": 127.0267851551,
      "scheduleStartTime": "2025-05-07T10:00:00",
      "scheduleEndTime": "2025-05-07T11:00:00"
    },
    {
      "scheduleContent": "메가박스 코엑스 방문",
      "scheduleAddress": "대한민국 서울특별시 강남구 봉은사로 524",
      "latitude": 37.5126572,
      "longitude": 127.0586523,
      "scheduleStartTime": "2025-05-07T11:47:00",
      "scheduleEndTime": "2025-05-07T12:47:00"
    },
    {
      "scheduleContent": "우텐더 방문",
      "scheduleAddress": "서울특별시 강남구 압구정로42길 25-10 1~2층",
      "latitude": 37.5270487520,
      "longitude": 127.0358085855,
      "scheduleStartTime": "2025-05-07T13:26:00",
      "scheduleEndTime": "2025-05-07T14:26:00"
    },
    {
      "scheduleContent": "청담근린공원 방문",
      "scheduleAddress": "서울특별시 강남구 청담동 66",
      "latitude": 37.5213524935,
      "longitude": 127.0526155502,
      "scheduleStartTime": "2025-05-07T14:50:00",
      "scheduleEndTime": "2025-05-07T15:50:00"
    }
  ]
}
```
</details>

---

<details>
<summary>스케줄 삭제</summary>

**DELETE** `/schedules`

> 스케줄을 삭제합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .delete(`${API_BASE_URL}/schedules/${scheduleNo}`, {
        withCredentials: true,
    })
```
</details>

---

<details>
<summary>스케줄 공유 ✏️</summary>

**POST** `/schedules/share`

> 스케줄을 공유합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .post(`${API_BASE_URL}/schedules/share`, {
        withCredentials: true,
    })
```

#### 요청 바디
```json
{
  "scheduleNo": 7,
  "userIds": [
    "user3"
  ]
}
```
</details>

---

<details>
<summary>스케줄 공유 취소 ✏️</summary>

**POST** `/schedules/unshare`

> 스케줄 공유를 취소합니다.

- 로그인을 진행해 JWT 쿠키가 있어야 함
```javascript
axios
    .post(`${API_BASE_URL}/schedules/unshare`, {
        withCredentials: true,
    })
```

#### 요청 바디
```json
{
  "scheduleNo": 7,
  "userIds": [
    "user3"
  ]
}
```
</details>
