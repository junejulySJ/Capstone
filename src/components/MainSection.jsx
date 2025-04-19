import React, { useState, useEffect } from 'react';
import './MainSection.css';

const images = [
  '/images/bg1.jpg',
  '/images/bg2.jpg',
  '/images/bg3.jpg',
  '/images/bg4.jpg'
];

export default function MainSection() {
  const [currentImage, setCurrentImage] = useState(0);
  const [departures, setDepartures] = useState(['', '']);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImage((prev) => (prev + 1) % images.length);
    }, 5000);
    return () => clearInterval(interval);
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

  return (
    <div className="page-container">
      <header className="header">
        <h1 className="logo-text">MeetingMap</h1>
      </header>

      <section className="image-slider" style={{ backgroundImage: `url(${images[currentImage]})` }}>
        <div className="main-box">
          <p className="subtitle">Enjoy your journey!</p>
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
              <button className="action-btn">시작</button>
              {departures.length < 4 && (
                <button className="action-btn" onClick={addDepartureInput}>출발지 추가</button>
              )}
            </div>
          </div>
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

      <section className="extra-space">
        {/* 여기에 향후 컴포넌트 추가 가능 */}
      </section>
    </div>
  );
}