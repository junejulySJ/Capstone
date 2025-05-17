import React from 'react';
import './RouteSummary.css';

const RouteSummary = ({ routes = [], transportMode, selectedIdx, onSelect }) => {
  return (
    <div className="route-list">
      {routes.map((routeGroup, groupIdx) => (
        <div key={groupIdx} className="route-group">
          <h4>{routeGroup.from}에서 출발:</h4>
          {routeGroup.routes.map((route, idx) => {
            if (transportMode === 'transit') {
              if (route.message === null) {
                return (
                route.plan?.map((p, subIdx) => (
                  <button
                    key={`${groupIdx}-${idx}-${subIdx}`}
                    className={`route-button ${transportMode} ${selectedIdx === `${groupIdx}-${idx}-${subIdx}` ? 'selected' : ''}`}
                    onClick={() => onSelect(groupIdx, idx, subIdx)}
                  >
                    🚇 {
                      p?.totalTime >= 60
                        ? `${Math.floor(p.totalTime / 60) > 0 ? `${Math.floor(p.totalTime / 60)}시간 ` : ''}${p.totalTime % 60 > 0 ? `${p.totalTime % 60}분` : ''}`
                        : `${p?.totalTime}분`
                    } / 도보 {
                      p?.totalWalkTime >= 60
                        ? `${Math.floor(p.totalWalkTime / 60) > 0 ? `${Math.floor(p.totalWalkTime / 60)}시간 ` : ''}${p.totalWalkTime % 60 > 0 ? `${p.totalWalkTime % 60}분` : ''}`
                        : `${p?.totalWalkTime}분`
                    } / 환승 {p?.transferCount}회 / 요금 {p?.fare.toLocaleString()}원
                  </button>
                ))
              )
              } else {
                return <h4>{route.message}</h4>
              }
            } else {
              return (
                <button
                  key={`${groupIdx}-${idx}`}
                  className={`route-button ${transportMode} ${selectedIdx === `${groupIdx}-${idx}` ? 'selected' : ''}`}
                  onClick={() => onSelect(groupIdx, idx)}
                >
                  ⏱ {
                  route.time >= 60
                    ? `${Math.floor(route.time / 60) > 0 ? `${Math.floor(route.time / 60)}시간 ` : ''}${route.time % 60 > 0 ? `${route.time % 60}분` : ''}`
                    : `${route.time}분`}
                    {route.fare ? ` / 요금 ${route.fare.toLocaleString()}원` : ``} / 거리 {(route.distance / 1000).toFixed(1)}km
                </button>
              )
            }
          })}
          </div>
          ))}
    </div>
  );
};

export default RouteSummary;
