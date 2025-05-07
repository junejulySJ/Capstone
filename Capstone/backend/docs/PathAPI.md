## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md)
- [Schedule API](ScheduleAPI.md)
- **Path API**

# Path API
주요 기능:
- 경로 조회

---

## API 목록

<details>
<summary>경로 조회</summary>

**POST** `/path`

> /schedules/create로부터 받은 details 리스트를 그대로 입력받아 경로를 반환합니다.

#### 요청 바디
```json
[
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
```

#### 응답 바디
```json
[
  {
    "origin": {
      "x": 127.02677537562212,
      "y": 37.500637376706884
    },
    "destination": {
      "x": 127.05864220451676,
      "y": 37.512654400527545
    },
    "distance": 3833,
    "roads": [
      {
        "vertexes": [
          127.02689497474098,
          37.50100779442158,
          127.02704140092499,
          37.501054053284975
        ]
      },
      {
        "vertexes": [
          127.02704140092499,
          37.501054053284975,
          127.02708803786926,
          37.50094631083679,
          ...이하생략
        ]
      }
    ]
  },
  {
    "origin": {
      "x": 127.05864220451676,
      "y": 37.512654400527545
    },
    "destination": {
      "x": 127.03580005160447,
      "y": 37.52704917400584
    },
    "distance": 3577,
    "roads": [
      {
        "vertexes": [
          127.0582709895475,
          37.513390306576476,
          ...이하생략
        ]
      }
    ]
  }
]
```
</details>