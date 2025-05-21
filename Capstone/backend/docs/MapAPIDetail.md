## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md) [(Example)](UserAPIDetail.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- [Map API](MapAPI.md) **(Example)**
- [Schedule API](ScheduleAPI.md) [(Example)](ScheduleAPIDetail.md)
- [Path API](PathAPI.md) [(Example)](PathAPIDetail.md)
- [Board API](BoardAPI.md) [(Example)](BoardAPIDetail.md)
- [Comment API](CommentAPI.md) [(Example)](CommentAPIDetail.md)

# Schedule API 예시
## API 바로가기
| API 호출                               | 설명                 |
|--------------------------------------|--------------------|
| [GET /map/category](#카테고리-조회)        | 카테고리 조회            |
| [GET /map](#지도-출력)                   | 지도 출력              |
| [GET /map/detail](#장소-세부-정보-출력수정-예정) | 장소 세부 정보 출력(수정 예정) |
| [GET /map/autocomplete](#장소명-자동완성)   | 장소명 자동완성           |

---

## API 상세

### 카테고리 조회

#### Request 예시 1 (카테고리)
```javascript
axios.get(`${API_BASE_URL}/map/category`)
```

#### Response 예시 1 (카테고리)
```json
[
    {
        "code": "tour",
        "name": "관광지"
    },
    {
        "code": "food",
        "name": "음식점"
    },
    {
        "code": "cafe",
        "name": "카페"
    },
    {
        "code": "convenience-store",
        "name": "편의점"
    },
    {
        "code": "shopping",
        "name": "쇼핑"
    },
    {
        "code": "culture",
        "name": "문화시설"
    },
    {
        "code": "event",
        "name": "공연/행사"
    }
]
```

#### Request 예시 2 (세부 카테고리)
```javascript
axios.get(`${API_BASE_URL}/map/category?category=tour`)
```

#### Response 예시 2 (세부 카테고리)
```json
[
    {
        "code": "tour-nature",
        "name": "자연"
    },
    {
        "code": "tour-tradition",
        "name": "역사"
    },
    {
        "code": "tour-park",
        "name": "공원"
    },
    {
        "code": "tour-theme-park",
        "name": "테마파크"
    }
]
```

---

### 지도 출력

#### Request 예시 1 (출발지-도착지)
```javascript
axios.get(`${API_BASE_URL}/map?search=destination&sort=user_ratings_total_dsc&start=한성대학교&end=종로구 관훈동 18&category=cafe`)
```

#### Response 예시 1 (출발지-도착지)
```json
{
  "start": {
    "name": "한성대학교",
    "address": "서울 성북구 삼선교로16길 116",
    "latitude": "37.5825624632779",
    "longitude": "127.010225523923"
  },
  "end": {
    "name": "명신당필방",
    "address": "서울 종로구 인사동길 34",
    "latitude": "37.57356831591039",
    "longitude": "126.9856982140611"
  },
  "list": [
    {
      "address": "종로구 관훈동 18",
      "sigunguCode": "23",
      "contentId": "google_ChIJv3WOusKifDURkNGSz-MjBAw",
      "category": "cafe",
      "thumbnail": "https://lh3.googleusercontent.com/place-photos/AJnk2cym7sgAzlB4C_F8_VbXTi9JhfjM5agM1d2rQ7DxsaV0jNN6fySIJfM-U-opSGaYCim7L47cDr2jU8RioU4X-cmsqtRyxRVcLxwvCBv0bUKoktNSDGC_4BNcIpMuPdKQAlkqCJLGWdyNy_CN2aA=s1600-w800",
      "latitude": "37.5735896",
      "longitude": "126.9856075",
      "name": "다경향실",
      "rating": "4.4",
      "userRatingsTotal": "7"
    },
    {
      "address": "서울특별시 종로구 사직로9길 22 (필운동) ",
      "sigunguCode": "23",
      "contentId": "2783352",
      "category": "cafe",
      "thumbnail": "http://tong.visitkorea.or.kr/cms/resource/84/2790084_image2_1.jpg",
      "latitude": "37.5774250096",
      "longitude": "126.9677078075",
      "name": "스태픽스",
      "rating": "4.2",
      "userRatingsTotal": "412"
    }
  ]
}
```

#### Request 예시 2 (중간 지점)
```javascript
axios.get(`${API_BASE_URL}/map?search=middle-point&sort=rating_dsc&name=동작구민회관&name=녹번동근린공원&name=올림픽공원&category=food-korean`)
```
※name 파라미터는 여러 개 가능합니다.

#### Response 예시 2 (중간 지점)
```json
{
  "start": [
    {
      "name": "동작구민회관",
      "address": "서울 동작구 보라매로5길 28",
      "latitude": "37.4938972382326",
      "longitude": "126.922743463895"
    },
    {
      "name": "녹번동근린공원",
      "address": "",
      "latitude": "37.60353994592752",
      "longitude": "126.93185185285346"
    },
    {
      "name": "올림픽공원",
      "address": "서울 송파구 올림픽로 424",
      "latitude": "37.5205340628851",
      "longitude": "127.120812783275"
    }
  ],
  "middlePoint": {
    "address": "서울 용산구 이태원동 212-27",
    "latitude": "126.99180270000781",
    "longitude": "37.53932374901508"
  },
  "list": [
    {
      "address": "서울특별시 중구 명동8나길 28 (충무로1가) ",
      "sigunguCode": "24",
      "contentId": "1489369",
      "category": "food-korean",
      "thumbnail": "http://tong.visitkorea.or.kr/cms/resource/38/3474938_image2_1.jpg",
      "latitude": "37.5614854780",
      "longitude": "126.9834734887",
      "name": "오다리집",
      "rating": "4.7",
      "userRatingsTotal": "3942"
    },
    {
      "address": "서울특별시 중구 세종대로 76 ",
      "sigunguCode": "24",
      "contentId": "398344",
      "category": "food-korean",
      "thumbnail": "http://tong.visitkorea.or.kr/cms/resource/75/1290675_image2_1.jpg",
      "latitude": "37.5629101933",
      "longitude": "126.9768490516",
      "name": "현대칼국수",
      "rating": "4.4",
      "userRatingsTotal": "337"
    }
  ]
}
```

#### Request 예시 3 (위치)
```javascript
axios.get(`${API_BASE_URL}/map?search=location&sort=title_asc&latitude=37.5745839959&longitude=126.9857145803&category=food`)
```

#### Response 예시 3 (위치)
```json
{
  "current": {
    "address": "서울특별시 종로구 인사동10길 11-4",
    "latitude": "37.5745839959",
    "longitude": "126.9857145803"
  },
  "list": [
    {
      "address": "서울특별시 중구 남대문로 52-5 (명동2가) ",
      "contentId": "134746",
      "category": "food-chinese",
      "thumbnail": "http://tong.visitkorea.or.kr/cms/resource/96/3474896_image2_1.jpg",
      "latitude": "37.5621214856",
      "longitude": "126.9818402861",
      "name": "개화",
      "rating": "3.9",
      "userRatingsTotal": "867"
    },
    {
      "address": "서울특별시 중구 무교로 24 (무교동) 2층",
      "contentId": "133276",
      "category": "food-korean",
      "thumbnail": "http://tong.visitkorea.or.kr/cms/resource/18/3474918_image2_1.jpg",
      "latitude": "37.5681540761",
      "longitude": "126.9794958849",
      "name": "곰국시집",
      "rating": "4.1",
      "userRatingsTotal": "849"
    }
  ]
}
```

---

### 장소 세부 정보 출력(수정 예정)

#### Request 예시 1
```javascript
axios.get(`${API_BASE_URL}/map/detail?contentId=2559938`)
```

#### Response 예시 1
```json
{
    "contentid": "2559938",
    "contenttypeid": "12",
    "createdtime": "20180907015112",
    "homepage": "",
    "modifiedtime": "20250327160800",
    "tel": "",
    "telname": "",
    "title": "브이알존 코엑스 직영점",
    "firstimage": "http://tong.visitkorea.or.kr/cms/resource/26/2559926_image2_1.jpg",
    "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/26/2559926_image2_1.jpg",
    "addr": "서울특별시 강남구 봉은사로 524 (삼성동) 지하1층",
    "zipcode": "06164",
    "overview": "VRZONE은 단순히 360도로 보이는 가상현실체험을 제공하지 않고 VRZONE에서 직접 개발부터 유통하는 VR콘텐츠를 체험할 수 있다. 데드프리즌은 의문의 사고로 좀비들이 득실거리는 병원에서 탈출하는 FPS VR 콘텐츠이며, 퓨처스트라이크는 연구소에서 실험 중 사고로 돌연변이 곤충들이 탈출하여 도시가 황폐화되어 곤충들을 박멸하는 FPS VR 콘텐츠이다. 가디언 히어로즈는 지구를 침략하려는 외계인들과 싸워 이기는 FPS VR 콘텐츠이다. VR 라이더는 약 20여 가지의 VR 콘텐츠로 놀이기구를 탑승하는 VR체험 시뮬레이터다. 놀이기구를 VR로 재현함과 상상만 해왔던 장소를 탐험하는 상상 그 이상의 현실을 구현한다."
}
```

---

### 장소명 자동완성

#### Request 예시 1
```javascript
axios.get(`${API_BASE_URL}/map/autocomplete?name=한성대`)
```

#### Response 예시 1
```json
[
  {
    "id": "11272875",
    "placeName": "한성대학교",
    "address": "서울 성북구 삼선교로16길 116"
  },
  {
    "id": "21160826",
    "placeName": "한성대입구역 4호선",
    "address": "서울 성북구 삼선교로 지하 1"
  },
  {
    "id": "423761522",
    "placeName": "한성대학교 상상관",
    "address": "서울 성북구 삼선교로16길 116"
  },
  {
    "id": "145744318",
    "placeName": "다이소 한성대입구역2호점",
    "address": "서울 성북구 동소문로 14"
  },
  {
    "id": "17567225",
    "placeName": "한성대학교 낙산관",
    "address": "서울 성북구 삼선교로16길 118"
  },
  {
    "id": "1852838507",
    "placeName": "한성대양꼬치",
    "address": "서울 성북구 동소문로6길 14-31"
  },
  {
    "id": "655977017",
    "placeName": "한성대학교 주차장",
    "address": "서울 성북구 삼선교로16길 116"
  },
  {
    "id": "1369203836",
    "placeName": "조선부뚜막 한성대점",
    "address": "서울 성북구 동소문로6길 4-23"
  },
  {
    "id": "17563225",
    "placeName": "한성대학교 미래관",
    "address": "서울 성북구 삼선교로16길 116"
  },
  {
    "id": "1650877136",
    "placeName": "올리브영 한성대입구역점",
    "address": "서울 성북구 삼선교로 12"
  }
]
```