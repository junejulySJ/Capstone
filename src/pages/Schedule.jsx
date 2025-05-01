import React, { useState } from 'react';
import './Schedule.css';
import CategorySidebar from '../components/CategorySidebar';

const categories = ['Restaurant', 'Cafe', 'Shopping', 'Culture', 'Outdoors'];

const Schedule = () => {
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [departure, setDeparture] = useState('');
  const [destination, setDestination] = useState('');
  const [estimatedTime, setEstimatedTime] = useState('');
  const [showCreateSection, setShowCreateSection] = useState(false);
  const [showRecommendSection, setShowRecommendSection] = useState(false);
  const [sidebarVisible, setSidebarVisible] = useState(false);
  const [scheduleItems, setScheduleItems] = useState([
    { name: '서울숲', category: 'Outdoors' },
    { name: '카페 라떼아트', category: 'Cafe' }
  ]);

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

        <div className="map-placeholder">
          <img src="https://t1.daumcdn.net/mapjsapi/images/2x/base_map.png" alt="map" className="map-image" />
        </div>

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
