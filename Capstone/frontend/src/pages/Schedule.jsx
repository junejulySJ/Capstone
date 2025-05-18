import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import './Schedule.css';
import CategorySidebar from '../components/CategorySidebar';
import { themeSchedules } from '../data/scheduleDummy';
import RouteSummary from '../components/RouteSummary';
import { drawPolyline, drawTransitPlan, clearPolylines } from '../components/RouteDrawer';
import { categoryList, categoryDetailCodes } from './Map';
import { API_BASE_URL} from '../constants.js'

const { kakao } = window;

const Schedule = () => {
  const location = useLocation();
  const [mapObj, setMapObj] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [estimatedTime, setEstimatedTime] = useState('');
  const [showCreateSection, setShowCreateSection] = useState(false);
  const [showRecommendSection, setShowRecommendSection] = useState(false);
  const [sidebarVisible, setSidebarVisible] = useState(false);
  const [selectedTheme, setSelectedTheme] = useState('date');
  
  const departures = location.state?.departures || [];
  const destinationCoord = location.state?.destinationCoord || null;

  const { addedList = [] } = location.state || {};
  const [scheduleItems, setScheduleItems] = useState(() => addedList);
  const { end = [] } = location.state || {};
  const [scheduleItemStayMinutes, setScheduleItemStayMinutes] = useState(["", "", "", "", "", "", "", ""]);
  const [scheduleName, setScheduleName] = useState();
  const [scheduleAbout, setScheduleAbout] = useState();
  const [scheduleDate, setScheduleDate] = useState();
  const [startTime, setStartTime] = useState();
  const [endTime, setEndTime] = useState();
  const [scheduleStartTime, setScheduleStartTime] = useState();
  const [scheduleEndTime, setScheduleEndTime] = useState();
  const [transport, setTransport] = useState("pedestrian");
  const [additionalRecommendation, setAdditionalRecommendation] = useState(false);
  const [totalPlaceCount, setTotalPlaceCount] = useState(() => scheduleItems.length);
  const [theme, setTheme] = useState("tour");
  const [stayMinutesMean, setStayMinutesMean] = useState(60);
  const [placeMarkers, setPlaceMarkers] = useState([]);
  const [showCreateScheduleSection, setShowCreateScheduleSection] = useState(false);
  const [createdSchedule, setCreatedSchedule] = useState(null);
  const [createScheduleLoading, setCreateScheduleLoading] = useState(false);
  const [transportMode, setTransportMode] = useState('car');
  const [routeList, setRouteList] = useState([]);
  const [selectedRouteIdx, setSelectedRouteIdx] = useState(null);
  const [polylines, setPolylines] = useState([]);
  const [transferMarkers, setTransferMarkers] = useState();
  const [createScheduleError, setCreateScheduleError] = useState();
  const [selectedCategoryPlaces, setSelectedCategoryPlaces] = useState();


  useEffect(() => {
    const container = document.getElementById('map');
    const map = new kakao.maps.Map(container, {
      center: new kakao.maps.LatLng(37.554722, 126.970833),
      level: 5,
    });
    setMapObj(map);
  }, []);

  useEffect(() => {
    // 장소들 마커 출력
    if (!scheduleItems || !mapObj) return;

    placeMarkers.forEach(placeMarker => {
      placeMarker.marker.setMap(null);
      placeMarker.infowindow.close();
    });

    const bounds = new kakao.maps.LatLngBounds();

    const markers = [];
    scheduleItems.forEach((item) => {
      const coords = new window.kakao.maps.LatLng(item.latitude, item.longitude);
      const marker = new window.kakao.maps.Marker({
        map: mapObj,
        position: coords,
        title: item.name
      });
      marker.setMap(mapObj);
      
      // 인포윈도우 생성
      const infowindow = new kakao.maps.InfoWindow({
        content: `<div>${item.name}</div>`
      })

      // 마커에 마우스 오버/아웃 이벤트 등록(인포윈도우 표시)
      kakao.maps.event.addListener(marker, "mouseover", () => infowindow.open(mapObj, marker));
      kakao.maps.event.addListener(marker, "mouseout", () => infowindow.close());

      markers.push({ marker, infowindow });
      bounds.extend(coords);
    });
    setPlaceMarkers(markers);
    mapObj.setBounds(bounds);
  }, [scheduleItems, mapObj]);
  useEffect(() => {
    setPlaceByCategory();
  }, [selectedCategory])

  useEffect(() => {
    if (scheduleDate && startTime) {
      const combined = `${scheduleDate}T${startTime}`;
      setScheduleStartTime(combined);
    }
  }, [scheduleDate, startTime]);

  useEffect(() => {
    if (scheduleDate && endTime) {
      const combined = `${scheduleDate}T${endTime}`;
      setScheduleEndTime(combined);
    }
  }, [scheduleDate, endTime]);

  useEffect(() => {
    if (!createdSchedule) return;
    loadRoutes();
  }, [createdSchedule, transportMode])

  const loadRoutes = async () => {
    clearPolylines(polylines);
    setPolylines([]);
    setSelectedRouteIdx(null);
    try {
      const pathType = transportMode === 'walk' ? 'pedestrian' : transportMode === 'transit' ? 'transit' : 'car';
      const res = await axios.post(`${API_BASE_URL}/path/${pathType}`, createdSchedule);
      // 응답 데이터를 출발지별로 묶음
      const result = res.data.map((routes) => ({
        from: routes?.origin?.name,
        routes: [routes],
      }));
      setRouteList(result);
      if (transportMode !== 'transit') {
        const line = drawPolyline(mapObj, result[0].routes[0].coordinates, (pathType === 'pedestrian' ? '#4D524C' : '#007bff'), (pathType === 'pedestrian' ? 'dashed' : 'solid'));
        setPolylines([line]);
      }
    } catch (err) {
      console.error('경로 API 오류:', err);
    }
  };

  const handleEstimate = () => {
    setEstimatedTime('차량: 35분 | 대중교통: 45분');
  };

  const toggleSidebar = (cat) => {
    if (selectedCategory === cat.code && sidebarVisible) {
      setSidebarVisible(false);
    } else {
      setSelectedCategory(cat.code);
      setSidebarVisible(true);
    }
  };

  const setPlaceByCategory = async () => {
    const detailCodes = categoryDetailCodes[selectedCategory];
    if (!detailCodes) return;

    const allPlaces = [];
    for (const detailCode of detailCodes) {
      try {
        const res = await axios.get(`${API_BASE_URL}/map?search=location&sort=rating_dsc&latitude=${end.latitude}&longitude=${end.longitude}&category=${detailCode}`);
        const items = res.data?.list || [];

        allPlaces.push(...items);

        setSelectedCategoryPlaces(allPlaces);
      } catch (err) {
        console.error(`❌ ${detailCode} 요청 실패:`, err);
      }
    }
  }

  const addToSchedule = (place) => {    
    if (!scheduleItems.some(item => item.name === place.name)) {
      setScheduleItems([...scheduleItems, place]);
    }
  };

  const removeFromSchedule = (name) => {
    setScheduleItems(scheduleItems.filter(item => item.name !== name));
  };

  const handleCreateSchedule = async () => {
    setShowCreateScheduleSection(false);
    setCreateScheduleLoading(true);
    try {
      const res = await axios.post(`${API_BASE_URL}/schedules/create`,
        {
          "selectedPlace": scheduleItems.map((item, index) => (
            {
              contentId: item.contentId,
              address: item.address,
              name: item.name,
              latitude: item.latitude,
              longitude: item.longitude,
              category: item.category,
              stayMinutes: scheduleItemStayMinutes[index]
            }
          )),
          "scheduleStartTime": scheduleStartTime,
          "scheduleEndTime": scheduleEndTime,
          "transport": transport,
          "additionalRecommendation": additionalRecommendation,
          "totalPlaceCount": totalPlaceCount,
          "theme": theme,
          "stayMinutesMean": stayMinutesMean,
          "pointCoordinate": {
            "latitude": end.latitude,
            "longitude": end.longitude
        }
      });
      const result = res.data;
      setScheduleItems(result.places);

      const updatedStayMinutes = [...scheduleItemStayMinutes];
      result.places.forEach((place, index) => {
        if (updatedStayMinutes[index] === "") {
          updatedStayMinutes[index] = stayMinutesMean;
        }
      });
      setScheduleItemStayMinutes(updatedStayMinutes);
      
      setCreatedSchedule(result.schedules);
      setTransportMode(transport === 'pedestrian' ? 'walk' : transportMode === 'transit' ? 'transit' : 'car');
      setShowCreateScheduleSection(true);
    } catch (err) {
      if (err.response.data.message) {
        setCreateScheduleError(err.response.data.message);
      } else {
        console.error('스케줄 API 오류:', err);
      }
    } finally {
      setCreateScheduleLoading(false);
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

  return (
    <div className="schedule-wrapper">
      {sidebarVisible && (
        <CategorySidebar
          category={selectedCategory}
          places={selectedCategoryPlaces}
          onClose={() => setSidebarVisible(false)}
          onAddPlace={addToSchedule}
        />
      )}

      <div className="schedule-container">
        <h2 className="schedule-title">Schedule</h2>
        <p className="schedule-sub">카카오맵 기반 지도 화면 완성 및 딥정 추가 기능이 구현될 예정입니다.</p>

        <div id="map" className="map-placeholder"></div>

        {showCreateScheduleSection && (
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
        )}

        <div className="button-row">
          <button onClick={() => setShowCreateSection((prev) => !prev)} className="action-btn">
            Create Schedule
          </button>
          <button onClick={() => setShowRecommendSection((prev) => !prev)} className="action-btn">
            Recommended Schedules
          </button>
          <button onClick={() => handleCreateSchedule()} className="action-btn">
            일정 생성
          </button>
        </div>

        {showCreateSection && (
          <div className="section-box">
            <h3>일정 만들기</h3>
            {
              <ul>
                <li>장소 목록</li>
                {scheduleItems.length !== 0 ? (scheduleItems.map((item, index) => (
                  <li key={index} className="schedule-item" onMouseOver={() => placeMarkers[index].infowindow.open(mapObj, placeMarkers[index].marker)} onMouseOut={() => placeMarkers[index].infowindow.close()}>
                    {item.name} ({item.category})
                    <input type="number" name="stayMinutes" placeholder="머무는 시간" value={scheduleItemStayMinutes[index]} onChange={(e) => {
                      const newArray = [...scheduleItemStayMinutes];
                      newArray[index] = e.target.value;
                      setScheduleItemStayMinutes(newArray);
                    }} />
                    <button className="delete-btn" onClick={() => removeFromSchedule(item.name)}>삭제</button>
                  </li>
                ))) : <></>}
                <li>---</li>
                <li>스케줄 날짜: 
                  <input type="date" value={scheduleDate} onChange={(e) => setScheduleDate(e.target.value)} required />
                </li>
                <li>스케줄 시작 시간: 
                  <input type="time" name="startTime" value={startTime} onChange={(e) => setStartTime(e.target.value)} required/>
                </li>
                <li>스케줄 종료 시간: 
                  <input type="time" name="endTime" value={endTime} onChange={(e) => setEndTime(e.target.value)} required/>
                </li>
                <li>이동수단: <select name="transport" value={transport} onChange={(e) => setTransport(e.target.value)}>
                  <option value="pedestrian">도보</option>
                  <option value="car">자동차</option>
                  <option value="transit">대중교통</option>
                  </select></li>
                <li>추가 추천 여부: <input type="checkbox" name="additionalRecommendation" checked={additionalRecommendation} onChange={(e) => setAdditionalRecommendation(e.target.checked)} /></li>
                {additionalRecommendation && (
                  <>
                    <li>총 장소 수(선택한 장소 + 추천 받을 장소): <input type="number" name="totalPlaceCount" value={totalPlaceCount} min={scheduleItems.length} max={7} onChange={(e) => setTotalPlaceCount(e.target.value)} /></li>
                    <li>테마: <select name="theme" value={theme} onChange={(e) => setTheme(e.target.value)}>
                      <option value="tour">관광</option>
                      <option value="nature">자연 힐링</option>
                      <option value="history">역사 탐방</option>
                      <option value="food">음식 투어</option>
                      <option value="shopping">쇼핑</option>
                      <option value="date">데이트</option>
                      </select></li>
                    <li>평균 머무는 시간: <input type="number" name="stayMinutesMean" value={stayMinutesMean} onChange={(e) => setStayMinutesMean(e.target.value)} /></li>
                  </>
                )}
              </ul>
            }
          </div>
        )}

        {showRecommendSection && (
          <div className="section-box">
            <h3>추천 스케줄</h3>
            <div className="theme-buttons">
              {Object.keys(themeSchedules).map((theme) => (
                <button
                  key={theme}
                  className={`theme-btn ${selectedTheme === theme ? 'active' : ''}`}
                  onClick={() => setSelectedTheme(theme)}>
                  {theme}
                </button>
              ))}
            </div>
            <ul>
              {themeSchedules[selectedTheme].map((item, index) => (
                <li key={index}>🔹 {item.time} - {item.activity}</li>
              ))}
            </ul>
          </div>
        )}
        {createScheduleLoading && <p>일정 생성 중...</p>}
        {createScheduleError && <p>{createScheduleError}</p>}
        {showCreateScheduleSection && createdSchedule && (
          <div className="section-box">
            <h3>일정 생성 결과</h3>
            <input type="text" placeholder='스케줄 제목' value={scheduleName} onChange={(e) => setScheduleName(e.target.value)} />
            <input type="text" placeholder='스케줄 설명' value={scheduleAbout} onChange={(e) => setScheduleAbout(e.target.value)} />
            <h4>{new Date(createdSchedule[0].scheduleStartTime).toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' })}</h4>
            <ul>
              {createdSchedule.map((item, index) => (
                <li key={index}>🔹 {new Date(item.scheduleStartTime).toLocaleTimeString('ko-KR', { hour: 'numeric', minute: '2-digit', hour12: true })} ~ {new Date(item.scheduleEndTime).toLocaleTimeString('ko-KR', { hour: 'numeric', minute: '2-digit', hour12: true })} - {item.scheduleContent}</li>
              ))}
            </ul>
            <button>스케줄 저장</button>
          </div>
        )}

        <div className="category-icons">
          {categoryList.map((cat) => (
            <button
              key={cat.code}
              className={`category-btn ${selectedCategory === cat.code ? 'active' : ''}`}
              onClick={() => toggleSidebar(cat)}>
              {cat.name}
            </button>
          ))}
        </div>

        <div className="input-group">
          <label>출력 장소</label>
          <button onClick={handleEstimate}>예상 시간 계산</button>
          <div className="estimated-time">{estimatedTime}</div>
        </div>

      </div>
    </div>
  );
};

export default Schedule;
