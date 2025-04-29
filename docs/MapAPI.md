## 📚 목차
- [Home](../README.md)
- [User API](UserAPI.md)
- [Auth API](AuthAPI.md)
- [Friendship API](FriendshipAPI.md)
- **Map API**

# MAP API
주요 기능:
- 지역/시군구 코드 반환
- 지도 출력
- 장소 세부 정보 출력

---

## API 목록

<details>
<summary>지역/시군구 코드 반환</summary>

**GET** `/map/region`

> 지역 코드나 시군구 코드를 반환합니다.

#### 요청 코드
- 지역 코드를 얻으려는 경우
```javascript
axios
    .get(`${API_BASE_URL}/map/region`)
```
- 시군구 코드를 얻으려는 경우(지역 코드 필요)
```javascript
axios
    .get(`${API_BASE_URL}/map/region?code=${areaCode}`)
```
#### 응답 바디(지역 코드를 얻으려는 경우)
```json
[
  {
    "code": "1",
    "name": "서울"
  },
  {
    "code": "2",
    "name": "인천"
  },
  {
    "code": "3",
    "name": "대전"
  },
  {
    "code": "4",
    "name": "대구"
  }
]
```

#### 응답 바디(시군구 코드를 얻으려는 경우)
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

#### 실패 응답
- **403 Forbidden** : 로그인 중이 아닌 경우
</details>

---

<details>
<summary>지도 출력 ✏️</summary>

**GET** `/map`

> 사용자가 선택한 방법으로 주변의 장소를 조회합니다.

1. 지역구를 기반으로 조회할 경우
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=area&areaCode=${areaCode}&sigunguCode=${sigunguCode}&contentTypeId=${contentTypeId}`)
```

#### 응답 바디
```json
[
  {
    "addr": "서울특별시 강남구 봉은사로 524 (삼성동) 지하1층",
    "contentid": "2559938",
    "contenttypeid": "12",
    "createdtime": "20180907015112",
    "firstimage": "http://tong.visitkorea.or.kr/cms/resource/26/2559926_image2_1.jpg",
    "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/26/2559926_image2_1.jpg",
    "mapx": "127.0571487082",
    "mapy": "37.5129952125",
    "mlevel": "6",
    "modifiedtime": "20250327160800",
    "tel": "",
    "title": "브이알존 코엑스 직영점",
    "zipcode": "06164"
  },
  {
    "addr": "서울특별시 강남구 역삼동 (역삼동)",
    "contentid": "264570",
    "contenttypeid": "12",
    "createdtime": "20050623224701",
    "firstimage": "http://tong.visitkorea.or.kr/cms/resource/08/1984608_image2_1.jpg",
    "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/08/1984608_image3_1.jpg",
    "mapx": "127.0281573537",
    "mapy": "37.4970465429",
    "mlevel": "6",
    "modifiedtime": "20250327142817",
    "tel": "",
    "title": "강남",
    "zipcode": "06232"
  }
]
```

2. 현재 위치를 기반으로 조회할 경우
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=location&latitude=${latitude}&longitude=${longitude}&contentTypeId=${contentTypeId}`)
```

#### 응답 바디
```json
[
  {
    "addr": "서울특별시 성북구 화랑로32길 146-20 (석관동) ",
    "contentid": "250396",
    "contenttypeid": "12",
    "createdtime": "20071025090000",
    "dist": "655.2861710198858",
    "firstimage": "http://tong.visitkorea.or.kr/cms/resource/85/2690685_image2_1.jpg",
    "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/85/2690685_image3_1.jpg",
    "mapx": "127.0594514371",
    "mapy": "37.6051289940",
    "mlevel": "6",
    "modifiedtime": "20250318205732",
    "tel": "",
    "title": "서울 의릉(경종·선의왕후) [유네스코 세계유산]"
  },
  {
    "addr": "서울특별시 성북구 화랑로32길 146-37 (석관동) ",
    "contentid": "3385777",
    "contenttypeid": "14",
    "createdtime": "20241011144959",
    "dist": "759.3744934829083",
    "firstimage": "http://tong.visitkorea.or.kr/cms/resource/89/3382889_image2_1.JPG",
    "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/89/3382889_image3_1.JPG",
    "mapx": "127.0579849067",
    "mapy": "37.6050484213",
    "mlevel": "6",
    "modifiedtime": "20250318205558",
    "tel": "",
    "title": "서울 의릉 역사문화관"
  }
]
```

3. 중간 위치를 기반으로 조회할 경우(좌표 평균 알고리즘)
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=middle-point&address=${address1}&address=${address2}&address=${address3}&contentTypeId=0`)
```
※address 파라미터는 여러 개 가능합니다.

#### 응답 바디
```json
{
  "addresses": [
    "서울 도봉구 창동 677",
    "서울특별시 관악구 보라매로 62",
    "서울특별시 동대문구 답십리로56길 105"
  ],
  "coordinates": [
    {
      "x": "127.040085321587",
      "y": "37.6491528337101"
    },
    {
      "x": "126.927602560192",
      "y": "37.4948015028766"
    },
    {
      "x": "127.058401410042",
      "y": "37.5677702572124"
    }
  ],
  "middleX": "127.008696430607",
  "middleY": "37.5705748645997",
  "list": [
    {
      "addr": "서울특별시 중구 을지로 281 ",
      "contentid": "2968672",
      "contenttypeid": "15",
      "createdtime": "20230413140011",
      "dist": "501.92090540033377",
      "firstimage": "http://tong.visitkorea.or.kr/cms/resource/00/3304500_image2_1.jpg",
      "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/00/3304500_image3_1.jpg",
      "mapx": "127.0095709797",
      "mapy": "37.5661076320",
      "mlevel": "6",
      "modifiedtime": "20250421144055",
      "tel": "02-2088-4957",
      "title": "DDP 봄축제 : 디자인 테마파크"
    },
    {
      "addr": "서울특별시 중구 을지로 281 (을지로7가) 동대문디자인플라자",
      "contentid": "2738786",
      "contenttypeid": "15",
      "createdtime": "20210901224706",
      "dist": "501.92090540033377",
      "firstimage": "http://tong.visitkorea.or.kr/cms/resource/26/3114826_image2_1.jpg",
      "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/26/3114826_image3_1.jpg",
      "mapx": "127.0095709797",
      "mapy": "37.5661076320",
      "mlevel": "6",
      "modifiedtime": "20250417103756",
      "tel": "070-5143-5662",
      "title": "서울세계도시문화축제"
    }
  ]
}
```
4. 중간 위치를 기반으로 조회할 경우(그라함 스캔(Graham Scan)과 무게 중심 알고리즘) ✏️
#### 요청 코드
```javascript
axios
    .get(`${API_BASE_URL}/map?search=middle-point2&address=${address1}&address=${address2}&address=${address3}&contentTypeId=0`)
```
※address 파라미터는 여러 개 가능합니다.

#### 응답 바디
```json
{
  "addresses": [
    "서울 도봉구 창동 677",
    "서울특별시 관악구 보라매로 62",
    "서울특별시 동대문구 답십리로56길 105"
  ],
  "coordinates": [
    {
      "x": "127.040085321587",
      "y": "37.6491528337101"
    },
    {
      "x": "126.927602560192",
      "y": "37.4948015028766"
    },
    {
      "x": "127.058401410042",
      "y": "37.5677702572124"
    }
  ],
  "middleX": "127.008696430607",
  "middleY": "37.5705748645997",
  "list": [
    {
      "addr": "서울특별시 종로구 종로33길 15 (연지동) ",
      "contentid": "509267",
      "contenttypeid": "14",
      "createdtime": "20080311185754",
      "dist": "697.6459496347278",
      "firstimage": "http://tong.visitkorea.or.kr/cms/resource/25/3481925_image2_1.jpg",
      "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/25/3481925_image3_1.jpg",
      "mapx": "127.0010160404",
      "mapy": "37.5718651424",
      "mlevel": "6",
      "modifiedtime": "20250428153821",
      "tel": "",
      "title": "두산갤러리"
    },
    {
      "addr": "서울특별시 중구 을지로 281 ",
      "contentid": "2968672",
      "contenttypeid": "15",
      "createdtime": "20230413140011",
      "dist": "501.92090540033377",
      "firstimage": "http://tong.visitkorea.or.kr/cms/resource/00/3304500_image2_1.jpg",
      "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/00/3304500_image3_1.jpg",
      "mapx": "127.0095709797",
      "mapy": "37.5661076320",
      "mlevel": "6",
      "modifiedtime": "20250421144055",
      "tel": "02-2088-4957",
      "title": "DDP 봄축제 : 디자인 테마파크"
    }
  ]
}
```
</details>

---

<details>
<summary>장소 세부 정보 출력</summary>

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