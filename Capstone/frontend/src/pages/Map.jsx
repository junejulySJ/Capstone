import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
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
  const [departure, setDeparture] = useState(null);
  const [destination, setDestination] = useState(null);
  const [transportMode, setTransportMode] = useState('car');
  const [routeList, setRouteList] = useState([]);
  const [selectedRouteIdx, setSelectedRouteIdx] = useState(null);
  const [polylines, setPolylines] = useState([]);
  const [showSidebar, setShowSidebar] = useState(false);
  const [categoryMarkers, setCategoryMarkers] = useState([]);
  const [selectedPlaces, setSelectedPlaces] = useState([]);
  const [addedList, setAddedList] = useState([]);
  const location = useLocation();
  const [sort, setSort] = useState();
  const [search, setSearch] = useState();
  const [departures, setDepartures] = useState([]);
  const [start, setStart] = useState();
  const [end, setEnd] = useState();
  const [middlePoint, setMiddlePoint] = useState();

  useEffect(() => {
    const container = document.getElementById('map');
    const map = new kakao.maps.Map(container, {
      center: new kakao.maps.LatLng(37.554722, 126.970833),
      level: 5,
    });
    setMapObj(map);
  }, []);

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const search = searchParams.get("search") || "";
    const sort = searchParams.get("sort") || "";
    const departure = searchParams.get("start") || "";
    const destination = searchParams.get("end") || "";
    const departures = searchParams.getAll("name") || "";

    setSearch(search);
    setSort(sort);
    setDeparture(departure);
    setDestination(destination);
    setDepartures(departures);
  }, [location.search]);

  useEffect(() => {
    const fetchData = async () => {
      const allPlaces = [];
      const markers = [];

      if (!search || !sort) return;

      try {
        const res = await axios.get(`${API_BASE_URL}/map?search=${search}&sort=${sort}${(departure ? `&start=${departure}` : ``)}${(destination ? `&end=${destination}` : ``)}${(departures.length ? `&${departures.map((d) => (`name=${d}`)).join('&')}` : ``)}`);
        const start = res.data?.start || null;
        const end = res.data?.end || null;
        const middlePoint = res.data?.middlePoint || null;
        const items = res.data?.list || [];

        for (const place of items) {
          if (!place.longitude || !place.latitude) continue;

          const lat = parseFloat(place.latitude);
          const lng = parseFloat(place.longitude);
          const marker = new kakao.maps.Marker({
            map: mapObj,
            position: new kakao.maps.LatLng(lat, lng),
            title: place.name
          });
          markers.push(marker);
        }

        allPlaces.push(...items);

        setStart(start);
        setEnd(end);
        setMiddlePoint(middlePoint);
      } catch (err) {
        console.error(`❌ 전체 요청 실패:`, err);
      }

      setSelectedPlaces(allPlaces.slice(0, 50));
      setShowSidebar(true);
    };

    fetchData();
  }, [departure, destination, departures, sort])

  useEffect(() => {
    if (!mapObj || !start || !end) return;

    const bounds = new kakao.maps.LatLngBounds();
    const startPosition = new kakao.maps.LatLng(start.latitude, start.longitude);
    const endPosition = new kakao.maps.LatLng(end.latitude, end.longitude);

    new kakao.maps.Marker({ map: mapObj, position: startPosition });
    new kakao.maps.Marker({ map: mapObj, position: endPosition });

    bounds.extend(startPosition);
    bounds.extend(endPosition);
    mapObj.setBounds(bounds);
    loadRoutes();
  }, [mapObj, start, end, transportMode]);

  const loadRoutes = async () => {
    clearPolylines(polylines);
    setPolylines([]);
    setSelectedRouteIdx(null);
    try {
      const pathType = transportMode === 'walk' ? 'pedestrian' : transportMode === 'transit' ? 'transit' : 'car';
      const res = await axios.get(`${API_BASE_URL}/path/${pathType}?name=${departure}&name=${destination}`);
      const result = transportMode === 'walk' ? [res.data[0]] : res.data.slice(0, 5);
      setRouteList(result);
      if (transportMode !== 'transit') {
        const line = drawPolyline(mapObj, result[0].coordinates, (pathType === 'pedestrian' ? '#4D524C' : '#007bff'));
        setPolylines([line]);
      }
    } catch (err) {
      console.error('경로 API 오류:', err);
    }
  };

  const handleRouteClick = (routeIdx, planIdx = 0) => {
    const selectedKey = `${routeIdx}-${planIdx}`;
    if (selectedRouteIdx === selectedKey) {
      clearPolylines(polylines);
      setPolylines([]);
      setSelectedRouteIdx(null);
      return;
    }
    clearPolylines(polylines);
    const selected = routeList[routeIdx];
    if (transportMode === 'transit') {
      const lines = drawTransitPlan(mapObj, selected.plan[planIdx]);
      setPolylines(lines);
    } else {
      const line = drawPolyline(mapObj, selected.coordinates);
      setPolylines([line]);
    }
    setSelectedRouteIdx(selectedKey);
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
        const res = await axios.get(`${API_BASE_URL}/map?search=${search}&sort=${sort}${(departure ? `&start=${departure}` : ``)}${(destination ? `&end=${destination}` : ``)}${(departures.length ? `&${departures.map((d) => (`name=${d}`)).join('&')}` : ``)}&category=${detailCode}`);
        const items = res.data?.list || [];

        for (const place of items) {
          if (!place.longitude || !place.latitude) continue;

          const lat = parseFloat(place.latitude);
          const lng = parseFloat(place.longitude);
          const marker = new kakao.maps.Marker({
            map: mapObj,
            position: new kakao.maps.LatLng(lat, lng),
            title: place.name
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
    if (addedList.find(p => p.name === place.name)) return;
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
          <h4>출발지: {start?.name || '없음'}</h4>
          <h4>도착지: {end?.name || '없음'}</h4>
        </div>
      </div>

      <div className="schedule-button-wrapper">
        <button
          className="schedule-create-button"
          onClick={() => {
            if (end) {
              navigate('/schedule', {
                state: {
                  addedList, 
                  end: {
                    latitude: end.latitude,
                    longitude: end.longitude
                  }
                }
              });
            } else {
              navigate('/schedule', {
                state: {
                  addedList, 
                  end: {
                    latitude: middlePoint.latitude,
                    longitude: middlePoint.longitude
                  }
                }
              });
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
              <li key={index}>{place.name} ({place.address})</li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Map;
