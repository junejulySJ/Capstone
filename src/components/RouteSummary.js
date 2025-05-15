import React from 'react';
import './RouteSummary.css';

const RouteSummary = ({ routes = [], transportMode, selectedIdx, onSelect }) => {
  return (
    <div className="route-list">
      {routes.map((route, idx) => (
        <button
          key={idx}
          className={`route-button ${transportMode} ${selectedIdx === idx ? 'selected' : ''}`}
          onClick={() => onSelect(idx)}
        >
          {transportMode === 'transit' ? (
            <>
              🚇 {route.plan?.[0]?.totalTime}분 / 도보 {route.plan?.[0]?.totalWalkTime}분 / 환승 {route.plan?.[0]?.transferCount}회 / 요금 {route.plan?.[0]?.fare}원
            </>
          ) : (
            <>
              ⏱ {route.time}분 / 요금 {route.fare}원 / 거리 {route.distance}km
            </>
          )}
        </button>
      ))}
    </div>
  );
};

export default RouteSummary;
