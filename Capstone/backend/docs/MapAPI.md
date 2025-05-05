## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- **Map API**

# MAP API
주요 기능:
- 시군구 코드 반환
- 타입 코드 반환
- 분류 코드 반환
- 지도 출력
- 장소 세부 정보 출력

---

## API 목록

<details>
<summary>시군구 코드 반환 ✏️</summary>

**GET** `/map/region`

> 시군구 코드를 반환합니다.

#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map/region`)
```
#### 응답 바디
```json
[
  {
    "code": "1",
    "name": "강남구"
  },
  {
    "code": "2",
    "name": "강동구"
  },
  {
    "code": "3",
    "name": "강북구"
  },
  {
    "code": "4",
    "name": "강서구"
  }
]
```
</details>

---

<details>
<summary>타입 코드 반환 ✏️</summary>

**GET** `/map/type`

> 타입 코드를 반환합니다.

#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map/type`)
```
#### 응답 바디
```json
[
  {
    "code": "1",
    "name": "관광지"
  },
  {
    "code": "2",
    "name": "문화시설"
  },
  {
    "code": "3",
    "name": "행사/공연/축제"
  },
  {
    "code": "4",
    "name": "레포츠"
  },
  {
    "code": "5",
    "name": "쇼핑"
  },
  {
    "code": "6",
    "name": "음식점"
  }
]
```
</details>

---

<details>
<summary>분류 코드 반환 ✏️</summary>

**GET** `/map/category`

> 대/중/소분류 코드를 반환합니다.  
> typeCode 파라미터는 필수 파라미터입니다.  
> cat1과 cat2를 파라미터에 포함시키지 않으면 typeCode에 맞는 대분류(cat1),  
> cat1만 파라미터에 포함시키면 typeCode에 맞는 중분류(cat2),  
> cat1과 cat2를 파라미터에 포함시키면 소분류(cat3)를 반환합니다.

#### 요청 코드 1
```javascript
axios
    .get(`${API_BASE_URL}/map/category?typeCode=1`)
```
#### 응답 바디 1
```json
[
  {
    "code": "A01",
    "name": "자연"
  },
  {
    "code": "A02",
    "name": "인문(문화/예술/역사)"
  }
]
```

#### 요청 코드 2
```javascript
axios
    .get(`${API_BASE_URL}/map/category?typeCode=1&cat1=A05&cat2=A0502`)
```
#### 응답 바디 2
```json
[
  {
    "code": "A05020100",
    "name": "한식"
  },
  {
    "code": "A05020200",
    "name": "서양식"
  },
  {
    "code": "A05020300",
    "name": "일식"
  },
  {
    "code": "A05020400",
    "name": "중식"
  },
  {
    "code": "A05020700",
    "name": "이색음식점"
  },
  {
    "code": "A05020900",
    "name": "카페/전통찻집"
  },
  {
    "code": "A05021000",
    "name": "클럽"
  }
]
```
</details>

---

<details>
<summary>지도 출력 ✏️</summary>

**GET** `/map`

> 사용자가 선택한 방법으로 주변의 장소를 조회합니다.  
> 파라미터 종류는 다음과 같습니다.  

| 파라미터        | 설명                                                  | 값 예시                                                                                        |
|-------------|-----------------------------------------------------|---------------------------------------------------------------------------------------------|
| search      | 장소 선택 방법                                            | area(지역구), location(위치), address(주소), middle-point(2)(중간지점)                                 |
| sort        | 정렬 방법                                               | title_asc(가나다순 오름차순 정렬), rating_asc(평점순 오름차순 정렬), user_ratings_total_dsc(총 평점 개수 내림차순 정렬) 등 |
| sigunguCode | 시군구 코드(search=area일 때만 필요)                          | 1(강남구), 2(강동구) 등                                                                            |
| latitude    | 위도(search=location일 때만 필요)                          | 37.6092635094031                                                                            |
| longitude   | 경도(search=location일 때만 필요)                          | 127.06471287129368                                                                          |
| address     | 주소(search=address, middle-point(2)일 때만 필요, 여러 개 가능) | 서울특별시 관악구 보라매로 62, 서울특별시 동대문구 답십리로56길 105 등                                                 |
| typeCode    | 타입 종류(필수X)                                          | 1(관광지), 2(문화시설), 3(행사/공연/축제), 4(레포츠), 5(쇼핑), 6(음식점)                                         |
| cat1        | 대분류(필수X)                                            | A05(음식)                                                                                     |
| cat2        | 중분류(필수X)                                            | A0502(음식점)                                                                                  |
| cat3        | 소분류(필수X)                                            | A05020900(카페/전통찻집)                                                                          |

### 1. 지역구를 기반으로 조회할 경우
#### 요청 코드 ✏️
```javascript
axios
    .get(`${API_BASE_URL}/map?search=area&sort=rating_dsc&sigunguCode=${sigunguCode}&theme=${theme}`)
```

#### 응답 바디 ✏️
```json
[
  {
    "addr": "서울특별시 강남구 압구정로42길 25-10 1~2층",
    "areaCode": "1",
    "sigunguCode": "1",
    "contentId": "2867691",
    "typeCode": "6",
    "cat1": "A05",
    "cat2": "A0502",
    "cat3": "A05020100",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/81/2867681_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/81/2867681_image3_1.jpg",
    "mapX": "127.0358085855",
    "mapY": "37.5270487520",
    "title": "우텐더",
    "rating": "4.5",
    "userRatingsTotal": "313"
  },
  {
    "addr": "서울특별시 강남구 도산대로45길 15 (신사동) 지하 1층",
    "areaCode": "1",
    "sigunguCode": "1",
    "contentId": "2870076",
    "typeCode": "6",
    "cat1": "A05",
    "cat2": "A0502",
    "cat3": "A05020200",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/62/3096562_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/62/3096562_image3_1.jpg",
    "mapX": "127.0351337538",
    "mapY": "37.5232825107",
    "title": "파시0914",
    "rating": "4.4",
    "userRatingsTotal": "75"
  }
]
```

### 2. 현재 위치를 기반으로 조회할 경우
#### 요청 코드 ✏️
```javascript
axios
    .get(`${API_BASE_URL}/map?search=location&sort=title_asc&latitude=${latitude}&longitude=${longitude}`)
```

#### 응답 바디 ✏️
```json
[
  {
    "addr": "중랑구 상봉로 131",
    "areaCode": "1",
    "sigunguCode": "25",
    "contentId": "google_ChIJrU-lPSy7fDUR6SoM24EYR40",
    "typeCode": "2",
    "cat1": "A02",
    "cat2": "A0202",
    "cat3": "A02020200",
    "firstImage": "https://lh3.googleusercontent.com/place-photos/AJnk2cwNU6qD0ikELv-0pzImIJMdDbfqjoc3iJuE4khvF6id9rw6PthEm8peVnwCzXjbdmIFUpU0XqYCa2_um7tE6WYRVgLa7c6Ry1lAjzhYbwuSgwQKt1QJ48Xyrqya7dNNLcEj6sD4D1_5kI8h=s1600-w800",
    "firstImage2": "https://lh3.googleusercontent.com/place-photos/AJnk2cwNU6qD0ikELv-0pzImIJMdDbfqjoc3iJuE4khvF6id9rw6PthEm8peVnwCzXjbdmIFUpU0XqYCa2_um7tE6WYRVgLa7c6Ry1lAjzhYbwuSgwQKt1QJ48Xyrqya7dNNLcEj6sD4D1_5kI8h=s1600-w200",
    "mapX": "127.0923835",
    "mapY": "37.5977689",
    "title": "CGV 상봉",
    "rating": "4.1",
    "userRatingsTotal": "2488"
  },
  {
    "addr": "서울특별시 강북구 월계로 173 (번동) 북서울꿈의숲",
    "areaCode": "1",
    "sigunguCode": "3",
    "contentId": "3114696",
    "typeCode": "3",
    "cat1": "A02",
    "cat2": "A0207",
    "cat3": "A02070200",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/55/3487455_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/55/3487455_image3_1.jpg",
    "mapX": "127.0445440464",
    "mapY": "37.6197242510",
    "title": "강북구 어린이날 대축제",
    "rating": "5.0",
    "userRatingsTotal": "2"
  }
]
```

### 3. 특정 주소를 기반으로 조회할 경우 ✏️
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=address&sort=user_ratings_total_dsc&address=${address}`)
```

#### 응답 바디
```json
[
  {
    "addr": "서울특별시 동작구 장승배기로 94 (노량진동)",
    "areaCode": "1",
    "sigunguCode": "12",
    "contentId": "130770",
    "typeCode": "2",
    "cat1": "A02",
    "cat2": "A0206",
    "cat3": "A02060900",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/53/3488953_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/53/3488953_image3_1.jpg",
    "mapX": "126.9405206377",
    "mapY": "37.5059328296",
    "title": "서울특별시교육청 동작도서관",
    "rating": "4.4",
    "userRatingsTotal": "114"
  },
  {
    "addr": "서울특별시 영등포구 신길로 275 (영등포동)",
    "areaCode": "1",
    "sigunguCode": "20",
    "contentId": "1603237",
    "typeCode": "1",
    "cat1": "A02",
    "cat2": "A0202",
    "cat3": "A02020700",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/51/1567951_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/51/1567951_image3_1.jpg",
    "mapX": "126.9108425219",
    "mapY": "37.5153173200",
    "title": "영등포근린공원",
    "rating": "4.3",
    "userRatingsTotal": "10"
  }
]
```

### 4. 중간 위치를 기반으로 조회할 경우(좌표 평균 알고리즘 / 그라함 스캔(Graham Scan)과 무게 중심 알고리즘)
#### 요청 코드(좌표 평균 알고리즘) ✏️
```javascript
axios
    .get(`${API_BASE_URL}/map?search=middle-point&sort=rating_dsc&address=${address1}&address=${address2}&address=${address3}`)
```
#### 요청 코드(그라함 스캔(Graham Scan)과 무게 중심 알고리즘) ✏️
```javascript
axios
    .get(`${API_BASE_URL}/map?search=middle-point2&sort=rating_dsc&address=${address1}&address=${address2}&address=${address3}`)
```
※address 파라미터는 여러 개 가능합니다.

#### 응답 바디
```json
{
  "addresses": [
    "서울 동작구 보라매로5길 28",
    "서울특별시 은평구 통일로 684",
    "서울특별시 송파구 올림픽로 424"
  ],
  "coordinates": [
    {
      "x": "126.922659687269",
      "y": "37.4938817761934"
    },
    {
      "x": "126.934953878791",
      "y": "37.6094070491111"
    },
    {
      "x": "127.115517876627",
      "y": "37.5203396980951"
    }
  ],
  "middleX": "126.99104381422902",
  "middleY": "37.54120950779987",
  "list": [
    {
      "addr": "서울특별시 중구 회현동1가 100-115",
      "areaCode": "1",
      "sigunguCode": "24",
      "contentId": "129418",
      "typeCode": "1",
      "cat1": "A02",
      "cat2": "A0205",
      "cat3": "A02050400",
      "firstImage": "http://tong.visitkorea.or.kr/cms/resource/66/2947266_image2_1.jpg",
      "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/66/2947266_image3_1.jpg",
      "mapX": "126.9798156648",
      "mapY": "37.5552528034",
      "title": "백범 김구 선생 동상",
      "rating": "4.9",
      "userRatingsTotal": "11"
    },
    {
      "addr": "서울특별시 중구 다산로 101-3 (신당동) ",
      "areaCode": "1",
      "sigunguCode": "24",
      "contentId": "3463600",
      "typeCode": "1",
      "cat1": "A02",
      "cat2": "A0203",
      "cat3": "A02030400",
      "firstImage": "http://tong.visitkorea.or.kr/cms/resource/64/3463464_image2_1.jpg",
      "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/64/3463464_image3_1.jpg",
      "mapX": "127.0092047089",
      "mapY": "37.5533202222",
      "title": "춘풍양조장",
      "rating": "4.9",
      "userRatingsTotal": "33"
    }
  ]
}
```
</details>

---

<details>
<summary>장소 세부 정보 출력(수정 예정)</summary>

**GET** `/map/detail`

> 특정한 장소의 세부 정보를 출력합니다.

#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map/detail?contentId=${contentId}`)
```

#### 응답 바디
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
</details>