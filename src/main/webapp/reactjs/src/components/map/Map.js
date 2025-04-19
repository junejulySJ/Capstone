import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useLocation } from "react-router-dom";
//import { useAppContext } from "../../context/AppContext";
import { useNavigate } from "react-router-dom";
import styles from "./css/Map.module.css"; // ✅ CSS 모듈
import { API_BASE_URL } from "../../constants.js";

const { kakao } = window;

export default function Map() {
  //const { user } = useAppContext();
  const location = useLocation();
  const [areaCodes, setAreaCodes] = useState([]);
  const [sigunguCodes, setSigunguCodes] = useState([]);
  const [area, setArea] = useState("");
  const [sigungu, setSigungu] = useState("");
  const [contentType, setContentType] = useState("");
  const [places, setPlaces] = useState([]);
  const [markers, setMarkers] = useState([]);
  const [mapObj, setMapObj] = useState(null);
  const [selectedPlaces, setSelectedPlaces] = useState([]);
  const navigate = useNavigate();

  /* // user가 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
    }
  }, [user, navigate]);*/

  // 지도 초기화
  useEffect(() => {
    if (window.kakao && window.kakao.maps) {
      const container = document.getElementById("map");
      const options = {
        center: new window.kakao.maps.LatLng(37.566826, 126.9786567),
        level: 3,
      };
      setMapObj(new window.kakao.maps.Map(container, options));
    } else {
      // 아직 로드 안 됐으면 로드 될 때까지 기다리기
      const interval = setInterval(() => {
        if (window.kakao && window.kakao.maps) {
          const container = document.getElementById("map");
          const options = {
            center: new window.kakao.maps.LatLng(37.566826, 126.9786567),
            level: 3,
          };
          new window.kakao.maps.Map(container, options);
          clearInterval(interval);
        }
      }, 100); // 0.1초마다 확인
    }
  }, []);

  // 지역코드 ,시군구 코드 파라미터 보내기
  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const area = searchParams.get("areaCode") || "";
    const sigungu = searchParams.get("sigunguCode") || "";
    const contentType = searchParams.get("contentTypeId") || "";

    setArea(area);
    setSigungu(sigungu);
    setContentType(contentType);
  }, [location.search]);

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/map/region`)
      .then((res) => setAreaCodes(res.data))
      .catch((err) => console.error("지역코드 불러오기 실패:", err));
  }, []);

  useEffect(() => {
    if (!area) return;
    axios
      .get(`${API_BASE_URL}/map/region?code=${area}`)
      .then((res) => setSigunguCodes(res.data))
      .catch((err) => console.error("시군구코드 불러오기 실패:", err));
  }, [area]);

  useEffect(() => {
    if (!area || !sigungu) return;

    axios
      .get(
        `${API_BASE_URL}/map?areaCode=${area}&sigunguCode=${sigungu}&contentTypeId=${contentType}`
      )
      .then((res) => setPlaces(res.data))
      .catch((err) => console.error("정보 불러오기 실패:", err));
  }, [area, sigungu, contentType]);

  //지도에 마커 찍기
  useEffect(() => {
    if (!mapObj || places.length === 0) return;

    //기존 마커, 인포윈도우 삭제
    markers.forEach((markerObj) => {
      markerObj.marker.setMap(null);
      markerObj.infowindow.close();
    });

    const bounds = new kakao.maps.LatLngBounds();
    const createdMarkers = [];

    places.forEach((place, index) => {
      const position = new kakao.maps.LatLng(place.mapy, place.mapx);
      const marker = new kakao.maps.Marker({
        position: position,
        clickable: true, // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정
        image: new kakao.maps.MarkerImage(
          "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png",
          new kakao.maps.Size(36, 37),
          {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, index * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
          }
        ),
      });

      marker.setMap(mapObj);
      bounds.extend(position);

      // 마커 위에 표시할 인포윈도우를 생성
      const infowindow = new kakao.maps.InfoWindow({
        content: `<div class=${styles.info_window}><div><img src=${place.firstimage}></div><div>${place.title}</div>`,
      });

      // 마커에 마우스오버이벤트 등록
      kakao.maps.event.addListener(marker, "mouseover", function () {
        // 마커 위에 인포윈도우 표시
        infowindow.open(mapObj, marker);
      });

      // 마커에 마우스아웃이벤트 등록
      kakao.maps.event.addListener(marker, "mouseout", function () {
        infowindow.close();
      });

      createdMarkers.push({ marker, infowindow });
    });

    setMarkers(createdMarkers);
    mapObj.setBounds(bounds);
  }, [mapObj, places]);

  const handleAreaChange = (selectedArea) => {
    navigate(
      `/map?areaCode=${selectedArea}&sigunguCode=1&contentTypeId=${contentType}`
    );
  };

  const handleSigunguChange = (selectedSigungu) => {
    navigate(
      `/map?areaCode=${area}&sigunguCode=${selectedSigungu}&contentTypeId=${contentType}`
    );
  };

  const handleContentTypeChange = (selectedContentType) => {
    navigate(
      `/map?areaCode=${area}&sigunguCode=${sigungu}&contentTypeId=${selectedContentType}`
    );
  };

  const handleCardClick = (placeId) => {
    setSelectedPlaces(
      (prevSelected) =>
        prevSelected.includes(placeId)
          ? prevSelected.filter((contentid) => contentid !== placeId) // 선택 해제
          : [...prevSelected, placeId] // 선택
    );
  };

  return (
    <div className={styles.map_container}>
      <div id="map" className={styles.map}></div>
      <div className={styles.menu_container}>
        <table>
          <thead>
            <tr>
              <th>
                <label htmlFor="area">지역명</label>
              </th>
              <th>
                <label htmlFor="sigungu">시군구명</label>
              </th>
              <th>
                <label htmlFor="contentType">카테고리</label>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <select
                  id="area"
                  value={area}
                  onChange={(e) => handleAreaChange(e.target.value)}
                >
                  {areaCodes.map((areaCode) => (
                    <option key={areaCode.code} value={areaCode.code}>
                      {areaCode.name}
                    </option>
                  ))}
                </select>
              </td>
              <td>
                <select
                  id="sigungu"
                  value={sigungu}
                  onChange={(e) => handleSigunguChange(e.target.value)}
                >
                  {sigunguCodes.map((sigunguCode) => (
                    <option key={sigunguCode.code} value={sigunguCode.code}>
                      {sigunguCode.name}
                    </option>
                  ))}
                </select>
              </td>
              <td>
                <select
                  id="contentType"
                  value={contentType}
                  onChange={(e) => handleContentTypeChange(e.target.value)}
                >
                  <option value="0">전체</option>
                  <option value="12">관광지</option>
                  <option value="14">문화시설</option>
                  <option value="15">행사/공연/축제</option>
                  <option value="25">여행코스</option>
                  <option value="28">레포츠</option>
                  <option value="32">숙박</option>
                  <option value="38">쇼핑</option>
                  <option value="39">음식점</option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
        {places.map((place, index) => (
          <div
            key={index}
            className={`${styles.card} ${
              selectedPlaces.includes(place.contentid) ? styles.selected : ""
            }`}
            onMouseOver={() =>
              markers[index]?.infowindow.open(mapObj, markers[index].marker)
            }
            onMouseOut={() => markers[index]?.infowindow.close()}
            onClick={() => handleCardClick(place.contentid)}
          >
            <div className={styles.card_row}>
              <div className={styles.card_image}>
                {place.firstimage ? (
                  <img src={place.firstimage} alt={place.title} />
                ) : (
                  <span>-</span>
                )}
              </div>
              <div className={styles.card_body}>
                <Link to={`/map/detail?contentId=${place.contentid}`}>
                  <h5 className={styles.card_title}>{place.title}</h5>
                </Link>
                <p className={styles.card_text}>{place.addr}</p>
                <p className={[styles.card_text, styles.small_text]}>
                  {place.tel || "-"}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
