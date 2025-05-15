import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Map.css';
import CategorySidebar from '../components/CategorySidebar';
import RouteSummary from '../components/RouteSummary';
import { drawPolyline, drawTransitPlan, clearPolylines } from '../components/RouteDrawer';

const API_BASE_URL = 'http://localhost:8080/api';
const { kakao } = window;

const categoryList = [
  { code: 'tour', name: '관광지' },
  { code: 'food', name: '음식점' },
  { code: 'cafe', name: '카페' },
  { code: 'convenience-store', name: '편의점' },
  { code: 'shopping', name: '쇼핑' },
  { code: 'culture', name: '문화시설' },
  { code: 'event', name: '공연/행사' }
];

// 정확한 분류코드 구조
const categoryDetailCodes = {
  tour: ['tour-nature', 'tour-tradition', 'tour-park', 'tour-theme-park'],
  food: ['food-korean', 'food-western', 'food-japanese', 'food-chinese', 'food-other'],
  cafe: ['cafe'],
  'convenience-store': ['convenience-store'],
  shopping: ['shopping-permanent-market', 'shopping-department-store'],
  culture: ['culture'],
  event: ['event']
};

const Map = () => {
  const navigate = useNavigate();
  const [mapObj, setMapObj] = useState(null);
  const [departure, setDeparture] = useState({ placeName: '서울역', latitude: 37.554722, longitude: 126.970833 });
  const [destination, setDestination] = useState({ placeName: '강남역', latitude: 37.4979, longitude: 127.0276 });
  const [transportMode, setTransportMode] = useState('car');
  const [routeList, setRouteList] = useState([]);
  const [selectedRouteIdx, setSelectedRouteIdx] = useState(null);
  const [polylines, setPolylines] = useState([]);
  const [showSidebar, setShowSidebar] = useState(false);
  const [categoryMarkers, setCategoryMarkers] = useState([]);
  const [selectedPlaces, setSelectedPlaces] = useState([]);
  const [addedList, setAddedList] = useState([]);

  useEffect(() => {
    const container = document.getElementById('map');
    const map = new kakao.maps.Map(container, {
      center: new kakao.maps.LatLng(37.554722, 126.970833),
      level: 5,
    });
    setMapObj(map);
  }, []);

  useEffect(() => {
    if (!mapObj || !departure || !destination) return;
    new kakao.maps.Marker({ map: mapObj, position: new kakao.maps.LatLng(departure.latitude, departure.longitude) });
    new kakao.maps.Marker({ map: mapObj, position: new kakao.maps.LatLng(destination.latitude, destination.longitude) });
    loadRoutes();
  }, [mapObj, departure, destination, transportMode]);

  const loadRoutes = async () => {
    clearPolylines(polylines);
    setPolylines([]);
    setSelectedRouteIdx(null);
    try {
      const pathType = transportMode === 'walk' ? 'walk' : transportMode === 'transit' ? 'transit' : 'car';
      const res = await axios.post(`${API_BASE_URL}/path/${pathType}`, [departure, destination]);
      const result = transportMode === 'walk' ? [res.data[0]] : res.data.slice(0, 5);
      setRouteList(result);
      if (transportMode !== 'transit') {
        const line = drawPolyline(mapObj, result[0].coordinates);
        setPolylines([line]);
      }
    } catch (err) {
      console.error('경로 API 오류:', err);
    }
  };

  const handleRouteClick = (idx) => {
    if (selectedRouteIdx === idx) {
      clearPolylines(polylines);
      setPolylines([]);
      setSelectedRouteIdx(null);
      return;
    }
    clearPolylines(polylines);
    const selected = routeList[idx];
    if (transportMode === 'transit') {
      const lines = drawTransitPlan(mapObj, selected.plan[0]);
      setPolylines(lines);
    } else {
      const line = drawPolyline(mapObj, selected.coordinates);
      setPolylines([line]);
    }
    setSelectedRouteIdx(idx);
  };

  const handleCategoryClick = async (code) => {
    const detailCodes = categoryDetailCodes[code];
    if (!detailCodes) return;

    const allPlaces = [];
    const markers = [];

    // 기존 마커 제거
    categoryMarkers.forEach(marker => marker.setMap(null));

    for (const detailCode of detailCodes) {
      try {
        const res = await axios.get(`${API_BASE_URL}/map/category?category=${detailCode}`);
        const items = res.data?.response?.body?.items?.item || [];

        for (const place of items) {
          if (!place.mapx || !place.mapy) continue;

          const lat = parseFloat(place.mapy);
          const lng = parseFloat(place.mapx);
          const marker = new kakao.maps.Marker({
            map: mapObj,
            position: new kakao.maps.LatLng(lat, lng),
            title: place.title
          });
          markers.push(marker);
        }

        allPlaces.push(...items);
      } catch (err) {
        console.error(`❌ ${detailCode} 요청 실패:`, err);
      }
    }

    setCategoryMarkers(markers);
    setSelectedPlaces(allPlaces.slice(0, 50));
    setShowSidebar(true);
  };

  const handleAddPlace = (place) => {
    if (addedList.find(p => p.title === place.title)) return;
    if (addedList.length >= 8) return;
    setAddedList([...addedList, place]);
  };

  return (
    <div className="map-page">
      <div className="category-top-bar">
        {categoryList.map(cat => (
          <button key={cat.code} onClick={() => handleCategoryClick(cat.code)}>{cat.name}</button>
        ))}
      </div>

      <div className="map-container-wrapper">
        <div id="map" className="map-area"></div>

        {showSidebar && (
          <CategorySidebar
            places={selectedPlaces}
            onClose={() => setShowSidebar(false)}
            onAddPlace={handleAddPlace}
            addedList={addedList}
          />
        )}

        <div className="route-box">
          <div className="transport-select">
            <button onClick={() => setTransportMode('car')}>🚗 차량</button>
            <button onClick={() => setTransportMode('transit')}>🚌 대중교통</button>
            <button onClick={() => setTransportMode('walk')}>🚶 도보</button>
          </div>
          <RouteSummary
            routes={routeList}
            transportMode={transportMode}
            selectedIdx={selectedRouteIdx}
            onSelect={handleRouteClick}
          />
        </div>

        <div className="location-box">
          <h4>출발지: {departure?.placeName || '없음'}</h4>
          <h4>도착지: {destination?.placeName || '없음'}</h4>
        </div>
      </div>

      <div className="schedule-button-wrapper">
        <button
          className="schedule-create-button"
          onClick={() => {
            if (destination) {
              navigate('/schedule', {
                state: {
                  destinationCoord: {
                    lat: destination.latitude,
                    lng: destination.longitude
                  },
                  departures: [departure?.placeName || '서울역']
                }
              });
            } else {
              alert('도착지가 설정되지 않았습니다.');
            }
          }}
        >
          📅 스케줄 생성하기
        </button>
      </div>

      <div className="section-b-wrapper">
        <h3>📝 선택한 장소 목록</h3>
        {addedList.length === 0 ? (
          <p>선택한 장소가 없습니다.</p>
        ) : (
          <ul>
            {addedList.map((place, index) => (
              <li key={index}>{place.title} ({place.addr1})</li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Map;
