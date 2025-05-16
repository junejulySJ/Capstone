import React from 'react';
import './RouteSummary.css';

const RouteSummary = ({ routes = [], transportMode, selectedIdx, onSelect }) => {
  return (
    <div className="route-list">
      {routes.map((route, idx) => (
        transportMode === 'transit' ? (
          route.plan?.map((p, subIdx) => (
            <button
              key={`${idx}-${subIdx}`}
              className={`route-button ${transportMode} ${selectedIdx === `${idx}-${subIdx}` ? 'selected' : ''}`}
              onClick={() => onSelect(idx, subIdx)}
            >
              🚇 {p?.totalTime}분 / 도보 {p?.totalWalkTime}분 / 환승 {p?.transferCount}회 / 요금 {p?.fare}원
            </button>
          ))
        ) : (
          <button
            key={idx}
            className={`route-button ${transportMode} ${selectedIdx === idx ? 'selected' : ''}`}
            onClick={() => onSelect(idx)}
          >
            ⏱ {route.time}분 / 요금 {route.fare}원 / 거리 {route.distance}km
          </button>
        )
      ))}
    </div>
  );
};

export default RouteSummary;
