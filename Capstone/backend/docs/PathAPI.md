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
- 보행자 길찾기
- 자동차 길찾기
- 대중교통 길찾기

---

## API 목록

<details>
<summary>보행자, 자동차 길찾기</summary>

**POST** `/path/pedestrian`

**POST** `/path/car`

> /schedules/create로부터 받은 details 리스트를 그대로 입력받아 보행자, 자동차 경로를 반환합니다.

#### 요청 바디
```json
[
  {
    "scheduleContent": "전통다원 방문",
    "scheduleAddress": "서울특별시 종로구 인사동10길 11-4",
    "latitude": 37.5745839959,
    "longitude": 126.9857145803,
    "scheduleStartTime": "2025-06-01T10:00:00",
    "scheduleEndTime": "2025-06-01T11:00:00"
  },
  {
    "scheduleContent": "다동커피집 방문",
    "scheduleAddress": "중구 다동길 24-8",
    "latitude": 37.5673387,
    "longitude": 126.9806302,
    "scheduleStartTime": "2025-06-01T11:30:00",
    "scheduleEndTime": "2025-06-01T12:30:00"
  },
  {
    "scheduleContent": "진옥화할매원조닭한마리 방문",
    "scheduleAddress": "서울특별시 종로구 종로40가길 18 (종로5가) ",
    "latitude": 37.5704292825,
    "longitude": 127.0057128756,
    "scheduleStartTime": "2025-06-01T13:00:00",
    "scheduleEndTime": "2025-06-01T14:00:00"
  },
  {
    "scheduleContent": "장충단공원 방문",
    "scheduleAddress": "서울특별시 중구 동호로 261 (장충동2가) ",
    "latitude": 37.5588195041,
    "longitude": 127.0048160637,
    "scheduleStartTime": "2025-06-01T14:30:00",
    "scheduleEndTime": "2025-06-01T15:30:00"
  }
]
```

#### 응답 바디
```json
[
  {
    "origin": {
      "x": 126.9857145803,
      "y": 37.5745839959
    },
    "destination": {
      "x": 126.9806302,
      "y": 37.5673387
    },
    "distance": 1282,
    "time": 19,
    "coordinates": [
      [
        126.98553681609398,
        37.574673306663165
      ],
      [
        126.98536460075329,
        37.57498437850854
      ],
      [
        126.98536460075329,
        37.57498437850854
      ], ... 이하 생략
    ]
  },
  {
    "origin": {
      "x": 126.9806302,
      "y": 37.5673387
    },
    "destination": {
      "x": 127.0057128756,
      "y": 37.5704292825
    },
    "distance": 2649,
    "time": 35,
    "coordinates": [
      [
        126.98058747487346,
        37.56734073717281
      ], ... 이하 생략
    ]
  }, ... 이하 생략
]
```

#### 보충 설명

- 클라이언트에서는 다음 예시와 같이 불러와서 선을 그릴 수 있습니다.
```javascript
// Axios를 사용해 보행자 경로를 요청함
axios
    .post(`${API_BASE_URL}/path/pedestrian`, details) // API로 details를 POST 요청
    .then((response) => {
        const result = response.data; // 응답 데이터에서 경로 정보 추출

        result.forEach((route) => {
            const fullPath = []; // 이 경로에 해당하는 전체 좌표 리스트

            // 각 경로의 좌표를 Kakao LatLng 객체로 변환
            route.coordinates.forEach((coordinate) => {
                // API에서 [경도, 위도] 형식으로 들어오므로 [위도, 경도] 순서로 LatLng 객체 생성
                fullPath.push(new kakao.maps.LatLng(coordinate[1], coordinate[0]));
            });

            // 경로 위에 마우스를 올렸을 때 표시할 인포윈도우 생성
            const infowindow = new kakao.maps.InfoWindow({
                content: `<div style="padding:6px;font-size:14px;">${
                    route.time + "분" // 해당 경로의 예상 소요 시간 표시
                }</div>`,
            });

            // 생성된 좌표 리스트를 이용해 폴리라인(선)을 생성
            const polyline = new kakao.maps.Polyline({
                path: fullPath, // 경로 좌표 배열
                strokeWeight: 5, // 선의 두께
                strokeColor: "#4D524C", // 선의 색상
                strokeOpacity: 0.8, // 선의 투명도
                strokeStyle: "dashed", // 선의 스타일: 점선
            });

            // 마우스 오버 시 인포윈도우를 표시하는 이벤트 리스너 등록
            kakao.maps.event.addListener(
                polyline,
                "mouseover",
                function (mouseEvent) {
                    var latlng = mouseEvent.latLng; // 마우스가 위치한 지도상의 좌표
                    infowindow.setPosition(latlng); // 인포윈도우 위치 설정
                    infowindow.open(mapObj); // 지도 객체 위에 인포윈도우 표시
                }
            );

            // 마우스가 선에서 벗어나면 인포윈도우 닫기
            kakao.maps.event.addListener(polyline, "mouseout", function () {
                infowindow.close(); // 인포윈도우 닫기
            });

            // 생성한 폴리라인을 지도에 표시
            polyline.setMap(mapObj);

            // 폴리라인을 상태에 저장 (예: 추후 삭제나 수정할 때 사용)
            setPolylines((prev) => [...prev, polyline]);
        });
    })
    .catch((error) => {
        // 요청 실패 시 콘솔에 에러 메시지 출력
        console.error(`❌ 경로 요청 실패: ${error}`);
    });
```
</details>

---

<details>
<summary>대중교통 길찾기</summary>

**POST** `/path/transit`

> /schedules/create로부터 받은 details 리스트를 그대로 입력받아 대중교통 경로를 반환합니다.

#### 요청 바디
```json
[
  {
    "scheduleContent": "전통다원 방문",
    "scheduleAddress": "서울특별시 종로구 인사동10길 11-4",
    "latitude": 37.5745839959,
    "longitude": 126.9857145803,
    "scheduleStartTime": "2025-06-01T10:00:00",
    "scheduleEndTime": "2025-06-01T11:00:00"
  },
  {
    "scheduleContent": "다동커피집 방문",
    "scheduleAddress": "중구 다동길 24-8",
    "latitude": 37.5673387,
    "longitude": 126.9806302,
    "scheduleStartTime": "2025-06-01T11:30:00",
    "scheduleEndTime": "2025-06-01T12:30:00"
  },
  {
    "scheduleContent": "진옥화할매원조닭한마리 방문",
    "scheduleAddress": "서울특별시 종로구 종로40가길 18 (종로5가) ",
    "latitude": 37.5704292825,
    "longitude": 127.0057128756,
    "scheduleStartTime": "2025-06-01T13:00:00",
    "scheduleEndTime": "2025-06-01T14:00:00"
  },
  {
    "scheduleContent": "장충단공원 방문",
    "scheduleAddress": "서울특별시 중구 동호로 261 (장충동2가) ",
    "latitude": 37.5588195041,
    "longitude": 127.0048160637,
    "scheduleStartTime": "2025-06-01T14:30:00",
    "scheduleEndTime": "2025-06-01T15:30:00"
  }
]
```

#### 응답 바디
```json
[
  {
    "origin": {
      "x": 126.9857145803,
      "y": 37.5745839959
    },
    "destination": {
      "x": 126.9806302,
      "y": 37.5673387
    },
    "plan": [
      {
        "totalDistance": 1537,
        "totalTime": 777,
        "totalWalkTime": 446,
        "transferCount": 0,
        "fare": 1500,
        "pathType": 2,
        "detail": [
          {
            "mode": "도보",
            "timeline": "조계사.종로경찰서까지 도보로 이동",
            "time": 4,
            "distance": 313,
            "routeColor": "4D524C",
            "routeStyle": "dashed",
            "coordinates": [
              [
                126.985535,
                37.574673
              ],
              ...
              이하
              생략
            ]
          },
          {
            "mode": "버스",
            "timeline": "조계사.종로경찰서 승차 - 간선:109 - 간선:606 - 종로1가 하차",
            "time": 5,
            "distance": 1183,
            "routeColor": "0068B7",
            "routeStyle": "solid",
            "coordinates": [
              [
                126.984833,
                37.576325
              ],
              ...
              이하
              생략
            ]
          },
          {
            "mode": "도보",
            "timeline": "도착지까지 도보로 이동",
            "time": 2,
            "distance": 234,
            "routeColor": "4D524C",
            "coordinates": [
              [
                126.98109,
                37.56614
              ],
              ...
              이하
              생략
            ]
          },
          ...
          이하
          생략
        ]
      },
      {
        "totalDistance": 2388,
        "totalTime": 1076,
        "totalWalkTime": 836,
        "transferCount": 0,
        "fare": 1400,
        "pathType": 1,
        "detail": [
          {
            "mode": "도보",
            "timeline": "종각까지 도보로 이동",
            "time": 8,
            "distance": 516,
            "routeColor": "4D524C",
            "routeStyle": "dashed",
            "coordinates": [
              [
                126.98059,
                37.56734
              ],
              ...
              이하
              생략
            ]
          },
          {
            "mode": "지하철",
            "timeline": "종각 승차 - 수도권1호선 - 종로5가 하차",
            "time": 4,
            "distance": 1659,
            "routeColor": "0052A4",
            "routeStyle": "solid",
            "coordinates": [
              [
                126.982975,
                37.570175
              ],
              ...
              이하
              생략
            ]
          },
          {
            "mode": "도보",
            "timeline": "도착지까지 도보로 이동",
            "time": 5,
            "distance": 418,
            "routeColor": "4D524C",
            "routeStyle": "dashed",
            "coordinates": [
              [
                127.001915,
                37.571007
              ],
              ...
              이하
              생략
            ]
          }
        ]
      }
    ]
  },
  ...
  이하
  생략
]
```
</details>