import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import './Map.css';
import CategorySidebar from '../components/CategorySidebar';
import RouteSummary from '../components/RouteSummary';
import { drawPolyline, drawTransitPlan, clearPolylines } from '../components/RouteDrawer';
import { API_BASE_URL} from '../constants.js'

const { kakao } = window;

export const categoryList = [
  { code: 'tour', name: '관광지' },
  { code: 'food', name: '음식점' },
  { code: 'cafe', name: '카페' },
  { code: 'convenience-store', name: '편의점' },
  { code: 'shopping', name: '쇼핑' },
  { code: 'culture', name: '문화시설' },
  { code: 'event', name: '공연/행사' }
];

// 정확한 분류코드 구조
export const categoryDetailCodes = {
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
  const [transferMarkers, setTransferMarkers] = useState();

  useEffect(() => {
    const interval = setInterval(() => {
      if (window.kakao && window.kakao.maps) {
        clearInterval(interval);
        const container = document.getElementById('map');
        const map = new window.kakao.maps.Map(container, {
          center: new window.kakao.maps.LatLng(37.554722, 126.970833),
          level: 5,
        });
        setMapObj(map);
      }
    }, 100);

    return () => clearInterval(interval);
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
    if (!mapObj) return;
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
        setCategoryMarkers(markers);
      } catch (err) {
        console.error(`❌ 전체 요청 실패:`, err);
      }

      setSelectedPlaces(allPlaces.slice(0, 50));
      setShowSidebar(true);
    };

    fetchData();
  }, [departure, destination, departures, sort, mapObj, search])

  useEffect(() => {
    if (!mapObj || (!start && !end)) return;

    if (transferMarkers) {
      transferMarkers.forEach((marker) => {
        marker.setMap(null);
      });
      setTransferMarkers([]);
    }

    const bounds = new kakao.maps.LatLngBounds();

    if (Array.isArray(start)) { //중간지점 조회면
      start.forEach((s) => {
        const startPosition = new kakao.maps.LatLng(s.latitude, s.longitude);

        new kakao.maps.Marker({ map: mapObj, position: startPosition });

        bounds.extend(startPosition);
      })
    } else {
      const startPosition = new kakao.maps.LatLng(start.latitude, start.longitude);
      const endPosition = new kakao.maps.LatLng(end.latitude, end.longitude);
      
      // 출발지: 초록색, 도착지: 빨간색, 1.3배 크기
      const startImage = new kakao.maps.MarkerImage(
        'https://cdn-icons-png.flaticon.com/512/447/447031.png',
        new kakao.maps.Size(33, 44)
      );
      
      const endImage = new kakao.maps.MarkerImage(
        'https://cdn-icons-png.flaticon.com/512/684/684908.png',
        new kakao.maps.Size(33, 44)
      );
      
      new kakao.maps.Marker({ map: mapObj, position: startPosition, title: start.name, image: startImage });
      new kakao.maps.Marker({ map: mapObj, position: endPosition, title: end.name, image: endImage });
      

      bounds.extend(startPosition);
      bounds.extend(endPosition);
    }

    mapObj.setBounds(bounds);
    loadRoutes();
  }, [mapObj, start, end, transportMode]);

  const loadRoutes = async () => {
    clearPolylines(polylines);
    setPolylines([]);
    setSelectedRouteIdx(null);
    if (Array.isArray(start)) { //중간지점 조회면
      try {
        const pathType = transportMode === 'walk' ? 'pedestrian' : transportMode === 'transit' ? 'transit' : 'car';
        const res = await axios.get(`${API_BASE_URL}/path/${pathType}?${start.map((d) => (`name=${d.name}`)).join('&')}`);
        // 응답 데이터를 출발지별로 묶음
        const result = res.data.map((routes, idx) => ({
          from: start[idx].name,
          routes: [routes],
        }));
        setRouteList(result);
        if (transportMode !== 'transit') {
          const line = drawPolyline(mapObj, result[0].routes[0].coordinates, (pathType === 'pedestrian' ? '#4D524C' : '#007bff'));
          setPolylines([line]);
        }
      } catch (err) {
        console.error('경로 API 오류:', err);
      }
    } else {
      try {
        const pathType = transportMode === 'walk' ? 'pedestrian' : transportMode === 'transit' ? 'transit' : 'car';
        const res = await axios.get(`${API_BASE_URL}/path/${pathType}?start=${start.name}&end=${end.name}`);
        const result = [{
          from: start.name,
          routes: [res.data[0]],
        }];
        setRouteList(result);
        if (transportMode !== 'transit') {
          const line = drawPolyline(mapObj, result[0].routes[0].coordinates, (pathType === 'pedestrian' ? '#4D524C' : '#007bff'), (pathType === 'pedestrian' ? 'dashed' : 'solid'));
          setPolylines([line]);
        }
      } catch (err) {
        console.error('경로 API 오류:', err);
      }
    }
  };

  const handleRouteClick = (groupIdx, routeIdx, planIdx = 0) => {
    const pathType = transportMode === 'walk' ? 'pedestrian' : transportMode === 'transit' ? 'transit' : 'car';
    const selectedKey = `${groupIdx}-${routeIdx}-${planIdx}`;
    if (selectedRouteIdx === selectedKey) {
      clearPolylines(polylines);
      setPolylines([]);
      setSelectedRouteIdx(null);
      return;
    }
    clearPolylines(polylines);
    if (transferMarkers) {
      transferMarkers.forEach((marker) => {
        marker.setMap(null);
      });
      setTransferMarkers([]);
    }
    const selectedGroup = routeList[groupIdx];
    const selected = selectedGroup.routes[routeIdx];
    if (transportMode === 'transit') {
      const lines = drawTransitPlan(mapObj, selected.plan[planIdx]);
      setPolylines(lines);
      // 환승(도보->버스, 지하철->다른 지하철 등) 지역마다 마커 찍기
      const markers = [];
      selected.plan[planIdx].detail.forEach(d => {
        const marker = new window.kakao.maps.Marker({ map: mapObj, position: new window.kakao.maps.LatLng(d.start.y, d.start.x) });
        markers.push(marker);
      });
      setTransferMarkers(markers);

    } else {
      const line = drawPolyline(mapObj, selected.coordinates, (pathType === 'pedestrian' ? '#4D524C' : '#007bff'), (pathType === 'pedestrian' ? 'dashed' : 'solid'));
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

<div className="route-box" style={{ gridArea: 'route-box' }}>
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
        <div className="right-box" style={{ gridArea: 'right-box' }}>
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
        <div className="location-box">
        {Array.isArray(start) ? (
          <>
            <h4>출발지:</h4>
            <ul>
              {start.map((s, index) => (
                <li key={index}>{s.name}</li>
              ))}
            </ul>
          </>
        ) : (
          <h4>출발지: {start?.name || '없음'}</h4>
        )}

        {middlePoint && (
          <h4>중간지점: {middlePoint.address}</h4>
        )}

        {end && <h4>도착지: {end.name}</h4>}
      </div>
      </div>


    </div>
  );
};

export default Map;
