import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './Schedule.css';
import CategorySidebar from '../components/CategorySidebar';
import { themeSchedules } from '../data/scheduleDummy';

const categories = ['Restaurant', 'Cafe', 'Shopping', 'Culture', 'Outdoors'];

const Schedule = () => {
  const location = useLocation();
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [estimatedTime, setEstimatedTime] = useState('');
  const [showCreateSection, setShowCreateSection] = useState(false);
  const [showRecommendSection, setShowRecommendSection] = useState(false);
  const [sidebarVisible, setSidebarVisible] = useState(false);
  const [selectedTheme, setSelectedTheme] = useState('date');
  const [scheduleItems, setScheduleItems] = useState([
    { name: '서울숲', category: 'Outdoors' },
    { name: '카페 라떼아트', category: 'Cafe' }
  ]);

  const departures = location.state?.departures || [];
  const destinationCoord = location.state?.destinationCoord || null;

  useEffect(() => {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=0bc6b6da5af871403e05922921487a1c&libraries=services&autoload=false`;
    script.onload = () => {
      window.kakao.maps.load(() => {
        const container = document.getElementById('map');
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780),
          level: 6
        };
        const map = new window.kakao.maps.Map(container, options);

        const geocoder = new window.kakao.maps.services.Geocoder();

        // 출발지 마커 출력
        departures.forEach((addr) => {
          geocoder.addressSearch(addr, (result, status) => {
            if (status === window.kakao.maps.services.Status.OK) {
              const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
              new window.kakao.maps.Marker({ map, position: coords });
              map.setCenter(coords);
            }
          });
        });

        // 도착지 좌표 마커 출력
        if (destinationCoord) {
          const destCoords = new window.kakao.maps.LatLng(destinationCoord.lat, destinationCoord.lng);
          new window.kakao.maps.Marker({
            map,
            position: destCoords,
            title: '도착지'
          });
          map.setCenter(destCoords);
        }
      });
    };
    document.head.appendChild(script);
  }, [departures, destinationCoord]);

  const handleEstimate = () => {
    setEstimatedTime('차량: 35분 | 대중교통: 45분');
  };

  const toggleSidebar = (cat) => {
    if (selectedCategory === cat && sidebarVisible) {
      setSidebarVisible(false);
    } else {
      setSelectedCategory(cat);
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

        <div id="map" className="map-placeholder"></div>

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
          <label>출력 장소</label>
          <button onClick={handleEstimate}>예상 시간 계산</button>
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
      </div>
    </div>
  );
};

export default Schedule;
