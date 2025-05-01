import React, { useState } from 'react';
import './CategorySidebar.css';

const dummyPlaces = {
  Restaurant: [
    { name: '한식당 미소', image: 'https://via.placeholder.com/100', description: '정갈한 한식 코스요리 제공' },
    { name: '이탈리안 라운지', image: 'https://via.placeholder.com/100', description: '분위기 좋은 파스타 전문점' }
  ],
  Cafe: [
    { name: '카페 라떼아트', image: 'https://via.placeholder.com/100', description: '라떼아트가 유명한 카페' },
    { name: '빈티지 커피', image: 'https://via.placeholder.com/100', description: '엔틱한 분위기의 카페' }
  ],
  Shopping: [
    { name: '트렌드 쇼핑몰', image: 'https://via.placeholder.com/100', description: '최신 트렌드 상품 다수' },
    { name: '디자이너 마켓', image: 'https://via.placeholder.com/100', description: '국내 디자이너 브랜드 판매' }
  ],
  Culture: [
    { name: '현대미술관', image: 'https://via.placeholder.com/100', description: '현대 미술 작품 전시' },
    { name: '역사 박물관', image: 'https://via.placeholder.com/100', description: '한국사 중심 전시관' }
  ],
  Outdoors: [
    { name: '서울숲', image: 'https://via.placeholder.com/100', description: '자연과 산책이 함께하는 공원' },
    { name: '한강 공원', image: 'https://via.placeholder.com/100', description: '강변 산책 및 운동 공간' }
  ]
};

const CategorySidebar = ({ category, onClose, onAdd }) => {
  const [expandedItem, setExpandedItem] = useState(null);
  const places = dummyPlaces[category] || [];

  return (
    <div className="sidebar-container">
      <div className="sidebar-header">
        <h3>{category}</h3>
        <button onClick={onClose} className="close-btn">×</button>
      </div>
      <div className="sidebar-content">
        {places.map((place, index) => (
          <div key={index} className="sidebar-item">
            <img src={place.image} alt={place.name} />
            <div className="item-info">
              <span>{place.name}</span>
              <div className="item-buttons">
                <button onClick={() => setExpandedItem(expandedItem === index ? null : index)}>상세보기</button>
                <button onClick={() => onAdd({ name: place.name, category })}>추가</button>
              </div>
              {expandedItem === index && (
                <p className="item-description">{place.description}</p>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CategorySidebar;