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
<summary>스케줄 생성</summary>

**POST** `/schedule/create`

> 사용자로부터 입력받은 데이터를 통해 스케줄을 생성합니다.

#### 요청 바디
```json
{
  "selectedPlace": [
    {
      "contentId": "127269",
      "address": "서울특별시 강남구 청담동 66",
      "title": "청담근린공원",
      "latitude": "37.5213524935",
      "longitude": "127.0526155502",
      "cat3": "A02020700",
      "stayMinutes": 60
    },
    {
      "contentId": "2994661",
      "address": "서울특별시 강남구 강남대로 426 (역삼동) ",
      "title": "일상비일상의틈",
      "latitude": "37.5006405461",
      "longitude": "127.0267851551",
      "cat3": "A02030400",
      "stayMinutes": 60
    },
    {
      "contentId": "2867691",
      "address": "대한민국 서울특별시 강남구 봉은사로 524",
      "title": "메가박스 코엑스",
      "latitude": "37.5126572",
      "longitude": "127.0586523",
      "cat3": "A02020200",
      "stayMinutes": 60
    },
    {
      "contentId": "2867691",
      "address": "서울특별시 강남구 압구정로42길 25-10 1~2층",
      "title": "우텐더",
      "latitude": "37.5270487520",
      "longitude": "127.0358085855",
      "cat3": "A05020100",
      "stayMinutes": 60
    }
  ],
  "scheduleName": "test",
  "scheduleAbout": "testabout",
  "scheduleStartTime": "2025-05-07T10:00:00",
  "scheduleEndTime": "2025-05-07T16:00:00",
  "startContentId": "2994661",
  "additionalRecommendation": false
}
```

#### 응답 바디
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
<summary>스케줄 공유</summary>

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
<summary>스케줄 공유 취소</summary>

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
