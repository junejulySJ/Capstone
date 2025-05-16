// ✅ 최종 MainSection.jsx: 카카오 API 자동완성 → 백엔드 자동완성으로 전환
// ✅ 드롭다운은 input 아래에 정확히 위치
// ✅ Tmap 연동을 위해 placeName만 사용하여 저장 (주소/좌표는 백엔드 응답 활용)

import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './MainSection.css';

const API_BASE_URL = 'http://localhost:8080';

const images = [
  '/images/bg1.jpg',
  '/images/bg2.jpg',
  '/images/bg3.jpg',
  '/images/bg4.jpg'
];

const samplePosts = [
  { id: 1, title: '서울 핫플 탐방기', views: 150, likes: 30 },
  { id: 2, title: '부산 해운대 맛집 추천', views: 120, likes: 40 },
  { id: 3, title: '제주도 힐링 여행 후기', views: 300, likes: 80 },
  { id: 4, title: '대구 시내 야경 명소', views: 90, likes: 20 },
  { id: 5, title: '강릉 카페 투어', views: 200, likes: 50 },
];

const randomPlaces = [
  '남산타워', '경복궁', '홍대 걷고 싶은 거리', '롯데월드타워', '서울숲',
  '청계천', '이태원 거리', '코엑스 몰', '광장시장', '올림픽공원'
];

const placeBackgrounds = {
  '남산타워': '/images/placeBG/1.jpg',
  '경복궁': '/images/placeBG/2.jpg',
  '홍대 걷고 싶은 거리': '/images/placeBG/3.jpg',
  '롯데월드타워': '/images/placeBG/4.jpg',
  '서울숲': '/images/placeBG/5.jpg',
  '청계천': '/images/placeBG/6.jpg',
  '이태원 거리': '/images/placeBG/7.jpg',
  '코엑스 몰': '/images/placeBG/8.jpg',
  '광장시장': '/images/placeBG/9.jpg',
  '올림픽공원': '/images/placeBG/10.jpg'
};

export default function MainSection() {
  const [currentImage, setCurrentImage] = useState(0);
  const [mode, setMode] = useState('route');
  const [departures, setDepartures] = useState(['', '']);
  const [departure, setDeparture] = useState('');
  const [destination, setDestination] = useState('');
  const [randomPlace, setRandomPlace] = useState('');
  const [suggestions, setSuggestions] = useState({});
  const [focusedInput, setFocusedInput] = useState(null);
  const navigate = useNavigate();
  const timeoutRef = useRef(null);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImage((prev) => (prev + 1) % images.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    const random = randomPlaces[Math.floor(Math.random() * randomPlaces.length)];
    setRandomPlace(random);
  }, []);

  const fetchSuggestions = async (query, index = null) => {
    if (!query.trim()) return;
    try {
      const res = await axios.get(`${API_BASE_URL}/api/map/autocomplete?name=${query}`);
      const key = index !== null ? index : 'single';
      const data = res.data.slice(0, 10); // 최대 10개 제한
      setSuggestions((prev) => ({ ...prev, [key]: data }));
    } catch (err) {
      const key = index !== null ? index : 'single';
      setSuggestions((prev) => ({ ...prev, [key]: [] }));
    }
  };

  const handleDepartureChange = (index, value) => {
    const updated = [...departures];
    updated[index] = value;
    setDepartures(updated);
    setFocusedInput(index);
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    timeoutRef.current = setTimeout(() => fetchSuggestions(value, index), 300);
  };

  const handleSingleDepartureChange = (value) => {
    setDeparture(value);
    setFocusedInput('single');
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    timeoutRef.current = setTimeout(() => fetchSuggestions(value), 300);
  };

  const handleSelectSuggestion = (text, index = null) => {
    if (index !== null) {
      const updated = [...departures];
      updated[index] = text;
      setDepartures(updated);
      setSuggestions((prev) => ({ ...prev, [index]: [] }));
    } else {
      setDeparture(text);
      setSuggestions((prev) => ({ ...prev, single: [] }));
    }
  };

  const handleGetCurrentLocation = (index = null, isDestination = false) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        const { latitude, longitude } = position.coords;
        const geocoder = new window.kakao.maps.services.Geocoder();
        geocoder.coord2Address(longitude, latitude, (result, status) => {
          if (status === window.kakao.maps.services.Status.OK) {
            const address = result[0].road_address?.address_name || result[0].address.address_name;
            if (isDestination) setDestination(address);
            else if (index !== null) {
              const updated = [...departures];
              updated[index] = address;
              setDepartures(updated);
            } else setDeparture(address);
          }
        });
      });
    } else {
      alert('위치 정보를 가져올 수 없습니다.');
    }
  };

  const handleStart = () => {
    if (mode === 'midpoint') {
      const filtered = departures.filter((d) => d.trim() !== '');
      if (filtered.length < 2) return alert('출발지를 2개 이상 입력해야 합니다.');
      navigate(`/map?search=middle-point&sort=rating_dsc&${filtered.map((f) => (`name=${f}`)).join('&')}`);
    } else {
      if (!departure.trim() || !destination.trim()) return alert('출발지와 도착지를 모두 입력해주세요.');
      navigate(`/map?search=destination&sort=rating_dsc&start=${departure}&end=${destination}`);
    }
  };

  const addDepartureInput = () => {
    if (departures.length < 4) setDepartures([...departures, '']);
  };

  const removeDepartureInput = (index) => {
    if (index >= 2) {
      const updated = departures.filter((_, i) => i !== index);
      setDepartures(updated);
    }
  };

  const topPosts = [...samplePosts].sort((a, b) => (b.views + b.likes * 2) - (a.views + a.likes * 2)).slice(0, 3);

  return (
    <div className="page-container">
      <header className="header">
        <h1 className="logo-text">MeetingMap</h1>
      </header>

      <section className="image-slider" style={{ backgroundImage: `url(${images[currentImage]})` }}>
        <div className="main-box">
          <p className="subtitle">Enjoy your journey!</p>

          {mode === 'midpoint' ? (
            <div className="input-vertical">
              {departures.map((value, index) => (
                <div className="input-wrapper" key={index}>
                  <input
                    type="text"
                    className="input-box with-button"
                    placeholder="출발지"
                    value={value}
                    onChange={(e) => handleDepartureChange(index, e.target.value)}
                  />
                  <button className="compass-btn" onClick={() => handleGetCurrentLocation(index)}>🧭</button>
                  {index >= 2 && <button className="inline-remove" onClick={() => removeDepartureInput(index)}>×</button>}
                  {suggestions[index] && suggestions[index].length > 0 && (
                    <ul className="dropdown">
                      {suggestions[index].map((s, i) => (
                        <li key={i} onClick={() => handleSelectSuggestion(s.placeName, index)}>{s.placeName}</li>
                      ))}
                    </ul>
                  )}
                </div>
              ))}
              <div className="button-row spaced">
                <button className="action-btn" onClick={handleStart}>시작</button>
                {departures.length < 4 && <button className="action-btn" onClick={addDepartureInput}>출발지 추가</button>}
                <button className="action-btn" onClick={() => setMode('route')}>되돌리기</button>
              </div>
            </div>
          ) : (
            <div className="input-vertical">
              <div className="input-wrapper">
                <input
                  type="text"
                  className="input-box with-button"
                  placeholder="출발지 입력"
                  value={departure}
                  onChange={(e) => handleSingleDepartureChange(e.target.value)}
                />
                <button className="compass-btn" onClick={() => handleGetCurrentLocation()}>🧭</button>
                {suggestions.single && suggestions.single.length > 0 && (
                  <ul className="dropdown">
                    {suggestions.single.map((s, i) => (
                      <li key={i} onClick={() => handleSelectSuggestion(s.placeName)}>{s.placeName}</li>
                    ))}
                  </ul>
                )}
              </div>
              <div className="input-wrapper">
                <input
                  type="text"
                  className="input-box with-button"
                  placeholder="도착지 입력"
                  value={destination}
                  onChange={(e) => {
                    setDestination(e.target.value);
                    setFocusedInput('destination');
                    if (timeoutRef.current) clearTimeout(timeoutRef.current);
                    timeoutRef.current = setTimeout(() => fetchSuggestions(e.target.value, 'destination'), 300);
                  }}
                />
                <button className="compass-btn" onClick={() => handleGetCurrentLocation(null, true)}>🧭</button>
                {suggestions.destination && suggestions.destination.length > 0 && (
                  <ul className="dropdown">
                    {suggestions.destination.map((s, i) => (
                      <li key={i} onClick={() => {
                        setDestination(s.placeName);
                        setSuggestions((prev) => ({ ...prev, destination: [] }));
                      }}>{s.placeName}</li>
                    ))}
                  </ul>
                )}
              </div>
              <div className="button-row spaced">
                <button className="action-btn" onClick={handleStart}>시작</button>
                <button className="action-btn" onClick={() => setMode('midpoint')}>중간지점 찾기</button>
              </div>
            </div>
          )}
        </div>

        <div className="dots">
          {images.map((_, idx) => (
            <span
              key={idx}
              className={`dot ${idx === currentImage ? 'active' : ''}`}
              onClick={() => setCurrentImage(idx)}
            />
          ))}
        </div>
      </section>

      <section className="recommend-section">
        <div className="recommend-left">
          <h2>#오늘의 추천</h2>
          <div className="card-row">
            {topPosts.map((post) => (
              <div key={post.id} className="recommend-card">
                <img src={`/images/sample${post.id}.jpg`} alt={post.title} className="card-image" />
                <div className="card-content">
                  <h3>{post.title}</h3>
                  <p>조회수 {post.views} | 좋아요 {post.likes}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="recommend-right">
          <h2>#랜덤 장소 추천</h2>
          <div className="card-grid">
            <div
              className="recommend-card big-card"
              style={{
                backgroundImage: `url(${placeBackgrounds[randomPlace] || ''})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                position: 'relative',
                overflow: 'hidden'
              }}
            >
              <div className="overlay"></div>
              <div className="card-content">
                <h3>{randomPlace}</h3>
                <button className="action-btn" onClick={() => setRandomPlace(randomPlaces[Math.floor(Math.random() * randomPlaces.length)])}>다시 추천받기</button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}