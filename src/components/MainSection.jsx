import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './MainSection.css';
import dummyPosts from '../data/dummyPosts';

const images = [
  '/images/bg1.jpg',
  '/images/bg2.jpg',
  '/images/bg3.jpg',
  '/images/bg4.jpg'
];

const randomPlaces = [
  '남산타워',
  '경복궁',
  '홍대 걷고 싶은 거리',
  '롯데월드타워',
  '서울숲',
  '청계천',
  '이태원 거리',
  '코엑스 몰',
  '광장시장',
  '올림픽공원'
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

function seededShuffle(array, seed) {
  const result = [...array];
  let currentIndex = result.length, temporaryValue, randomIndex;

  const random = () => {
    const x = Math.sin(seed++) * 10000;
    return x - Math.floor(x);
  };

  while (0 !== currentIndex) {
    randomIndex = Math.floor(random() * currentIndex);
    currentIndex -= 1;
    temporaryValue = result[currentIndex];
    result[currentIndex] = result[randomIndex];
    result[randomIndex] = temporaryValue;
  }

  return result;
}

export default function MainSection() {
  const [currentImage, setCurrentImage] = useState(0);
  const [mode, setMode] = useState('midpoint');
  const [departures, setDepartures] = useState(['', '']);
  const [departure, setDeparture] = useState('');
  const [destination, setDestination] = useState('');
  const [randomPlace, setRandomPlace] = useState('');
  const [dailyPosts, setDailyPosts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImage((prev) => (prev + 1) % images.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    pickRandomPlace();
    const placeInterval = setInterval(() => {
      pickRandomPlace();
    }, 5000);
    return () => clearInterval(placeInterval);
  }, []);

  useEffect(() => {
    const today = new Date().toDateString();
    const seed = today.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    const shuffled = seededShuffle(dummyPosts, seed);
    setDailyPosts(shuffled.slice(0, 3));
  }, []);

  const goToImage = (index) => setCurrentImage(index);

  const handleDepartureChange = (index, value) => {
    const updated = [...departures];
    updated[index] = value;
    setDepartures(updated);
  };

  const addDepartureInput = () => {
    if (departures.length < 4) {
      setDepartures([...departures, '']);
    }
  };

  const removeDepartureInput = (index) => {
    if (index >= 2) {
      const updated = departures.filter((_, i) => i !== index);
      setDepartures(updated);
    }
  };

  const pickRandomPlace = () => {
    const random = randomPlaces[Math.floor(Math.random() * randomPlaces.length)];
    setRandomPlace(random);
  };

  const handlePostClick = (postId) => {
    navigate(`/board?postId=${postId}`);
  };

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
                    placeholder="출발지"
                    className="input-box with-button"
                    value={value}
                    onChange={(e) => handleDepartureChange(index, e.target.value)}
                  />
                  {index >= 2 && (
                    <button className="inline-remove" onClick={() => removeDepartureInput(index)}>×</button>
                  )}
                </div>
              ))}
              <div className="button-row spaced">
                <button className="action-btn" onClick={() => alert('중간 지점을 찾습니다!')}>시작</button>
                {departures.length < 4 && (
                  <button className="action-btn" onClick={addDepartureInput}>출발지 추가</button>
                )}
                <button className="action-btn" onClick={() => setMode('route')}>변환</button>
              </div>
            </div>
          ) : (
            <div className="input-vertical">
              <input
                type="text"
                placeholder="출발지 입력"
                className="input-box"
                value={departure}
                onChange={(e) => setDeparture(e.target.value)}
              />
              <input
                type="text"
                placeholder="도착지 입력"
                className="input-box"
                value={destination}
                onChange={(e) => setDestination(e.target.value)}
              />
              <div className="button-row spaced">
                <button className="action-btn" onClick={() => alert('경로를 표시합니다!')}>시작</button>
                <button className="action-btn" onClick={() => setMode('midpoint')}>되돌리기</button>
              </div>
            </div>
          )}
        </div>

        <div className="dots">
          {images.map((_, idx) => (
            <span
              key={idx}
              className={`dot ${idx === currentImage ? 'active' : ''}`}
              onClick={() => goToImage(idx)}
            />
          ))}
        </div>
      </section>

      <section className="recommend-section">
        <div className="recommend-left">
          <h2>#오늘의 추천</h2>
          <div className="card-row">
            {dailyPosts.map((post, index) => (
              <div key={index} className="recommend-card" onClick={() => handlePostClick(post.id)}>
                <img src={`/images/${post.image}`} alt={post.title} className="card-image" />


                <div className="card-content">
                  <h3>{post.title}</h3>
                  <p>조회수 {post.views || 0} | 좋아요 {post.likes || 0}</p>
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
                <button className="action-btn" onClick={pickRandomPlace}>다시 추천받기</button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
