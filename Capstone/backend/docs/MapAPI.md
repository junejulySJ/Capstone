## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- **Map API**
- [Schedule API](ScheduleAPI.md)
- [Path API](PathAPI.md)

# MAP API
주요 기능:
- 지도 출력
- 장소 세부 정보 출력
- 장소명 자동완성

---

<details>
<summary>카테고리 조회 ✏️</summary>

**GET** `/map/category`
> 카테고리 목록을 조회합니다.  
> 세부 카테고리는 퀴리 파라미터에 카테고리 코드를 넣어서 조회할 수 있습니다.  
> 카테고리 종류는 다음과 같습니다.

| 카테고리 코드                   | 설명    | 부모 카테고리  |
|---------------------------|-------|----------|
| tour                      | 관광지   | X        |
| tour-nature               | 자연    | tour     |
| tour-tradition            | 역사    | tour     |
| tour-park                 | 공원    | tour     |
| tour-theme-park           | 테마파크  | tour     |
| food                      | 음식점   | X        |
| food-korean               | 한식    | food     |
| food-western              | 양식    | food     |
| food-japanese             | 일식    | food     |
| food-chinese              | 중식    | food     |
| food-other                | 기타    | food     |
| cafe                      | 카페    | X        |
| convenience-store         | 편의점   | X        |
| shopping                  | 쇼핑    | X        |
| shopping-permanent-market | 상설시장  | shopping |
| shopping-department-store | 백화점   | shopping |
| culture                   | 문화시설  | X        |
| event                     | 공연/행사 | X        |

#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map/category?category=${categoryCode}`)
```

</details>

---

## API 목록

<details>
<summary>지도 출력 ✏️</summary>

**GET** `/map`

> 사용자가 선택한 방법으로 주변의 장소를 조회합니다.  
> 쿼리 파라미터 종류는 다음과 같습니다.  

| 파라미터      | 설명                                                     | 값 예시                                                                                        |
|-----------|--------------------------------------------------------|---------------------------------------------------------------------------------------------|
| search    | 장소 선택 방법                                               | location(위치), destination(도착지), middle-point(중간지점)                                          |
| sort      | 정렬 방법                                                  | title_asc(가나다순 오름차순 정렬), rating_asc(평점순 오름차순 정렬), user_ratings_total_dsc(총 평점 개수 내림차순 정렬) 등 |
| latitude  | 위도(search=location일 때만 필요)                             | 37.6092635094031                                                                            |
| longitude | 경도(search=location일 때만 필요)                             | 127.06471287129368                                                                          |
| name      | 장소 이름(search=destination이면 1개, middle-point이면 여러 개 필요) | 시청역, 올림픽공원 등                                                                                |
| category  | 카테고리(필수X)                                              | 아래 참조                                                                                       |




### 1. 현재 위치를 기반으로 조회할 경우
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=location&sort=title_asc&latitude=${latitude}&longitude=${longitude}`)
```

#### 응답 바디
```json
[
  {
    "address": "서울특별시 중구 남대문로 52-5 (명동2가) ",
    "sigunguCode": "24",
    "contentId": "134746",
    "category": "food-chinese",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/96/3474896_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/96/3474896_image3_1.jpg",
    "latitude": "37.5621214856",
    "longitude": "126.9818402861",
    "name": "개화",
    "rating": "3.9",
    "userRatingsTotal": "867"
  },
  {
    "address": "서울특별시 중구 무교로 24 (무교동) 2층",
    "sigunguCode": "24",
    "contentId": "133276",
    "category": "food-korean",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/18/3474918_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/18/3474918_image3_1.jpg",
    "latitude": "37.5681540761",
    "longitude": "126.9794958849",
    "name": "곰국시집",
    "rating": "4.1",
    "userRatingsTotal": "849"
  }
]
```

### 2. 도착지를 기반으로 조회할 경우
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=destination&sort=user_ratings_total_dsc&name=${placeName}`)
```

#### 응답 바디
```json
[
  {
    "address": "서울특별시 종로구 인사동10길 11-4 ",
    "sigunguCode": "23",
    "contentId": "1945693",
    "category": "cafe",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/52/3474852_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/52/3474852_image3_1.jpg",
    "latitude": "37.5745839959",
    "longitude": "126.9857145803",
    "name": "전통다원",
    "rating": "4.3",
    "userRatingsTotal": "454"
  },
  {
    "address": "서울특별시 종로구 사직로9길 22 (필운동) ",
    "sigunguCode": "23",
    "contentId": "2783352",
    "category": "cafe",
    "firstImage": "http://tong.visitkorea.or.kr/cms/resource/84/2790084_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/84/2790084_image3_1.jpg",
    "latitude": "37.5774250096",
    "longitude": "126.9677078075",
    "name": "스태픽스",
    "rating": "4.2",
    "userRatingsTotal": "412"
  }
]
```

### 3. 중간 위치를 기반으로 조회할 경우
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=middle-point&sort=rating_dsc&name=${placeName1}&name=${placeName2}&name=${placeName3}`)
```
※name 파라미터는 여러 개 가능합니다.

#### 응답 바디
```json
{
  "names": [
    "동작구민회관",
    "녹번동근린공원",
    "올림픽공원"
  ],
  "coordinates": [
    {
      "x": "126.922743463895",
      "y": "37.4938972382326"
    },
    {
      "x": "126.93185185285346",
      "y": "37.60353994592752"
    },
    {
      "x": "127.120812783275",
      "y": "37.5205340628851"
    }
  ],
  "middleX": "126.99180270000781",
  "middleY": "37.53932374901508",
  "list": [
    {
      "address": "서울특별시 중구 명동8나길 28 (충무로1가) ",
      "sigunguCode": "24",
      "contentId": "1489369",
      "category": "food-korean",
      "firstImage": "http://tong.visitkorea.or.kr/cms/resource/38/3474938_image2_1.jpg",
      "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/38/3474938_image3_1.jpg",
      "latitude": "37.5614854780",
      "longitude": "126.9834734887",
      "name": "오다리집",
      "rating": "4.7",
      "userRatingsTotal": "3915"
    },
    {
      "address": "서울특별시 중구 세종대로 76 ",
      "sigunguCode": "24",
      "contentId": "398344",
      "category": "food-korean",
      "firstImage": "http://tong.visitkorea.or.kr/cms/resource/75/1290675_image2_1.jpg",
      "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/75/1290675_image3_1.jpg",
      "latitude": "37.5629101933",
      "longitude": "126.9768490516",
      "name": "현대칼국수",
      "rating": "4.4",
      "userRatingsTotal": "337"
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
> 현재 구글 지도로 검색한 지역은 출력이 되지 않습니다.

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

---

<details>
<summary>장소명 자동완성</summary>

**GET** `/map/autocomplete`

> 장소명을 자동완성해 목록을 보여줍니다.  
> 서울 지역의 장소만 보여줍니다.

#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map/autocomplete?name=${placeName}`)
```

#### 응답 바디
```json
[
  {
    "id": "11156260",
    "placeName": "창경궁",
    "address": "서울 종로구 창경궁로 185"
  },
  {
    "id": "11002870",
    "placeName": "창경궁 대온실",
    "address": "서울 종로구 창경궁로 185"
  },
  {
    "id": "1932803950",
    "placeName": "창경궁 매표소",
    "address": "서울 종로구 창경궁로 185"
  },
  {
    "id": "946945721",
    "placeName": "힐스테이트창경궁아파트",
    "address": "서울 종로구 율곡로 236"
  },
  {
    "id": "8116578",
    "placeName": "창경궁 춘당지",
    "address": "서울 종로구 창경궁로 185"
  },
  {
    "id": "1808045382",
    "placeName": "창경궁초밥",
    "address": "서울 종로구 창경궁로 229"
  },
  {
    "id": "7873650",
    "placeName": "창경궁 명정전",
    "address": "서울 종로구 창경궁로 185"
  },
  {
    "id": "457839741",
    "placeName": "휴스턴창경궁오피스텔(C동)",
    "address": "서울 종로구 창경궁로20길 14"
  }
]
```
</details>