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
  const [scheduleItems, setScheduleItems] = useState();

  const departures = location.state?.departures || [];
  const destinationCoord = location.state?.destinationCoord || null;

  const { addedList = [] } = location.state || {};
  const { end = [] } = location.state || {};
  const [addedListStayMinutes, setAddedListStayMinutes] = useState(["", "", "", "", "", "", "", ""]);
  const [scheduleName, setScheduleName] = useState();
  const [scheduleAbout, setScheduleAbout] = useState();
  const [scheduleStartTime, setScheduleStartTime] = useState();
  const [scheduleEndTime, setScheduleEndTime] = useState();
  const [startContentId, setStartContentId] = useState();
  const [transport, setTransport] = useState();
  const [additionalRecommendation, setAdditionalRecommendation] = useState(false);
  const [totalPlaceCount, setTotalPlaceCount] = useState();
  const [stayMinutesMean, setStayMinutesMean] = useState();

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

  useEffect(() => {
    setScheduleItems(addedList);
  }, [addedList])

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
            {
              <ul>
                <li>스케줄 이름: 
                  <input type="text" name="scheduleName" placeholder="이름" value={scheduleName} onChange={(e) => setScheduleName(e.target.value)} required />
                  </li>
                <li>스케줄 설명: 
                  <input type="text" name="scheduleAbout" placeholder="설명" value={scheduleAbout} onChange={(e) => setScheduleAbout(e.target.value)} required />
                </li>
                <li>스케줄 시작 시간: 
                  <input type="datetime-local" name="scheduleStartTime" value={scheduleStartTime} onChange={(e) => setScheduleStartTime(e.target.value)} required/>
                </li>
                <li>스케줄 종료 시간: 
                  <input type="datetime-local" name="scheduleEndTime" value={scheduleEndTime} onChange={(e) => setScheduleEndTime(e.target.value)} required/>
                </li>
                <li>이동수단: <select name="transport" value={transport} onChange={(e) => setTransport(e.target.value)}>
                  <option value="도보">도보</option>
                  <option value="자동차">자동차</option>
                  <option value="대중교통">대중교통</option>
                  </select></li>
                <li>추가 추천 여부: <input type="checkbox" name="additionalRecommendation" checked={additionalRecommendation} onChange={(e) => setAdditionalRecommendation(e.target.checked)} /></li>
                {additionalRecommendation && (
                  <>
                    <li>총 장소 수: <input type="number" name="totalPlaceCount" value={totalPlaceCount} onChange={(e) => setTotalPlaceCount(e.target.value)} /></li>
                    <li>테마: <select name="theme">
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
                <li>---</li>
                <li>장소 목록</li>
                {scheduleItems.length !== 0 ? (scheduleItems.map((item, index) => (
                  <li key={index} className="schedule-item">
                    {item.name} ({item.category})
                    <input type="number" name="stayMinutes" placeholder="머무는 시간" value={addedListStayMinutes[index]} onChange={(e) => {
                      const newArray = [...addedListStayMinutes];
                      newArray[index] = e.target.value;
                      setAddedListStayMinutes(newArray);
                    }} />
                    <input type="radio" name="startContentId" value={item.contentId} checked={startContentId === item.contentId}  onChange={(e) => setStartContentId(e.target.value)} />첫 방문 장소
                    <button className="delete-btn" onClick={() => removeFromSchedule(item.name)}>삭제</button>
                  </li>
                ))) : <></>}
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
      </div>
    </div>
  );
};

export default Schedule;
