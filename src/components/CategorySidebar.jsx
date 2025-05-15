import React from 'react';
import './CategorySidebar.css';

const CategorySidebar = ({ places = [], onClose, onAddPlace, addedList = [] }) => {
  return (
    <div className="category-sidebar">
      <div className="header">
        <h3>카테고리</h3>
        <button className="close-button" onClick={onClose}>닫기</button>
      </div>
      <div className="place-list">
        {places.length === 0 ? (
          <p>장소 정보를 불러오는 중입니다...</p>
        ) : (
          places.map((place, index) => (
            <div key={index} className="place-item">
              <img
                src={place.thumbnail || '/images/no_image.png'}
                alt={place.name}
                className="place-thumbnail"
              />
              <div className="place-content">
                <h4 className="place-title">{place.name}</h4>
                <p className="place-desc">{place.address || '상세 주소 없음'}</p>
                <button
                  disabled={addedList.some(p => p.name === place.name) || addedList.length >= 8}
                  onClick={() => onAddPlace(place)}
                >
                  추가
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default CategorySidebar;
