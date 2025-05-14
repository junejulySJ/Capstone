import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import './Schedule.css';
import CategorySidebar from '../components/CategorySidebar';

const API_BASE_URL = 'http://localhost:3001/api'; // 📝 [수정] 서버 API 베이스 URL 정의

const categories = ['Restaurant', 'Cafe', 'Shopping', 'Culture', 'Outdoors'];

const Schedule = () => {
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [departure, setDeparture] = useState('');
  const [destination, setDestination] = useState('');
  const [estimatedTime, setEstimatedTime] = useState('');
  const [showCreateSection, setShowCreateSection] = useState(false);
  const [showRecommendSection, setShowRecommendSection] = useState(false);
  const [sidebarVisible, setSidebarVisible] = useState(false);

  const [sigunguCode, setSigunguCode] = useState('');
  const [theme, setTheme] = useState('');
  const [mapPlaces, setMapPlaces] = useState([]);  // API 받아올 장소 리스트

  const [scheduleItems, setScheduleItems] = useState([
    { name: '서울숲', category: 'Outdoors' },
    { name: '카페 라떼아트', category: 'Cafe' }
  ]);

  const mapRef = useRef(null);  // 📝 지도 그릴 div를 위한 ref

  useEffect(() => {
    if (window.kakao && window.kakao.maps) {
      const container = mapRef.current;  // 지도를 그릴 div
      const options = {
        center: new window.kakao.maps.LatLng(37.5665, 126.9780),  // 초기 지도 중심 좌표 (서울)
        level: 5
      };
      const map = new window.kakao.maps.Map(container, options);

      // 📝 mapPlaces에 장소 데이터가 있으면 마커 찍기
      mapPlaces.forEach((place) => {
        if (place.latitude && place.longitude) {  // 좌표가 있을 때만
          new window.kakao.maps.Marker({
            map: map,
            position: new window.kakao.maps.LatLng(place.latitude, place.longitude),
            title: place.name
          });
        }
      });
    }
  }, [mapPlaces]);

    // 📝 [수정] 지도 데이터 (장소 리스트) 가져오는 fetchMapPlaces 함수 작성
  const fetchMapPlaces = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/map`, {
        params: {
          search: 'area',
          sort: 'rating_dsc',
          sigunguCode,
          theme
        }
      });
      console.log('받은 데이터:', response.data);
      setMapPlaces(response.data); // 장소 데이터 세팅
    } catch (error) {
      console.error('지도 데이터 불러오기 실패:', error);
    }
  };
  

  const handleEstimate = () => {
    if (departure && destination) {
      setEstimatedTime(`${Math.floor(Math.random() * 40) + 10}분 예상`);
    } else {
      setEstimatedTime('출발지와 도착지를 입력해주세요.');
    }
  };

  const toggleSidebar = (cat) => {
    if (selectedCategory === cat && sidebarVisible) {
      setSidebarVisible(false);
    } else {
      setSelectedCategory(cat);
      setTheme(cat); // 📝 [추가] 카테고리 선택 시 theme도 세팅
      setSidebarVisible(true);
    }
  };

  const addToSchedule = (place) => {
    if (!scheduleItems.some(item => item.name === place.name)) {
      setScheduleItems([...scheduleItems, place]);
    }
  };

  const removeFromSchedule = (name) => {
    setScheduleItems(scheduleItems.filter(item => item.name !== name));
  };

  return (
    <div className="schedule-wrapper">
      {sidebarVisible && (
        <CategorySidebar
          category={selectedCategory}
          onClose={() => setSidebarVisible(false)}
          onAdd={addToSchedule}
        />
      )}

      <div className="schedule-container">
        <h2 className="schedule-title">Schedule</h2>
        <p className="schedule-sub">카카오맵 기반 지도 화면 완성 및 딥정 추가 기능이 구현될 예정입니다.</p>

      <div className="map-placeholder" ref={mapRef} style={{ width: '100%', height: '400px' }} />
        {mapPlaces.length === 0 && (
          <p>지도 데이터를 불러오세요</p>
        )}

        <div className="category-icons">
          {categories.map((cat) => (
            <button
              key={cat}
              className={`category-btn ${selectedCategory === cat ? 'active' : ''}`}
              onClick={() => toggleSidebar(cat)}>
              {cat}
            </button>
          ))}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="출발지"
            value={departure}
            onChange={(e) => setDeparture(e.target.value)}
          />
          <input
            type="text"
            placeholder="도착지"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
          />
          <button onClick={handleEstimate} className="estimate-btn">예상 시간 계산</button>

          {/* 📝 [추가] 지도 데이터를 직접 불러오는 버튼 추가 */}
          <button onClick={fetchMapPlaces} className="estimate-btn">지도 데이터 불러오기</button>

          <div className="estimated-time">{estimatedTime}</div>
        </div>

        <div className="button-row">
          <button onClick={() => setShowCreateSection((prev) => !prev)} className="action-btn">
            Create Schedule
          </button>
          <button onClick={() => setShowRecommendSection((prev) => !prev)} className="action-btn">
            Recommended Schedules
          </button>
        </div>

        {showCreateSection && (
          <div className="section-box">
            <h3>일정 만들기</h3>
            {scheduleItems.length === 0 ? (
              <p>선택한 일정이 없습니다.</p>
            ) : (
              <ul>
                {scheduleItems.map((item, index) => (
                  <li key={index} className="schedule-item">
                    {item.name} ({item.category})
                    <button className="delete-btn" onClick={() => removeFromSchedule(item.name)}>삭제</button>
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}

        {showRecommendSection && (
          <div className="section-box">
            <h3>추천 스케줄</h3>
            <ul>
              <li>🔹 한강 → 카페거리 → 야경 명소</li>
              <li>🔹 박물관 → 맛집 → 공원 산책</li>
            </ul>
          </div>
        )}
      </div>
    </div>
  );
};

export default Schedule;
