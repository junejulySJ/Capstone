import React, { useState } from 'react';
import './Board.css';
import { FaComment, FaThumbsUp, FaEye } from 'react-icons/fa';
import initialPosts from '../data/initialPosts';

const CATEGORIES = [
  { key: '맛집 추천', title: '맛집' },
  { key: '카페 추천', title: '카페' },
  { key: '문화·여가 추천', title: '놀거리' },
  { key: '일정 경험 추천', title: '일정·코스 추천' }
];

export default function Board() {
  const [posts, setPosts] = useState([...initialPosts]);
  const [sortMode, setSortMode] = useState({
    '맛집 추천': '최신순',
    '카페 추천': '최신순',
    '문화·여가 추천': '최신순',
    '일정 경험 추천': '최신순'
  });

  // 정렬 함수
  const sortPosts = (posts, mode) => {
    if (mode === '인기순') {
      return [...posts].sort((a, b) => (b.likes || 0) - (a.likes || 0));
    } else { // 최신순
      return [...posts].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    }
  };

  return (
    <div className="board-container">
      {/* 왼쪽 사이드바 */}
      <aside className="sidebar">
        <div className="menu-icon">☰</div>
        <div className="logo">MeetingMap</div>
      </aside>

      {/* 오른쪽 메인 콘텐츠 */}
      <main className="main-content">
        <h1 className="board-title">Board</h1>
        <div className="grid-2x2">
          {CATEGORIES.map(cat => {
            const filtered = posts.filter(p => p.category === cat.key);
            const sorted = sortPosts(filtered, sortMode[cat.key]);

            return (
              <div key={cat.key} className="category-block">
                <div className="category-header">
                  <h2>#{cat.title}</h2>
                  <div className="sort-buttons">
                    <button
                      className={sortMode[cat.key] === '최신순' ? 'active' : ''}
                      onClick={() => setSortMode(prev => ({ ...prev, [cat.key]: '최신순' }))}
                    >
                      최신순
                    </button>
                    <button
                      className={sortMode[cat.key] === '인기순' ? 'active' : ''}
                      onClick={() => setSortMode(prev => ({ ...prev, [cat.key]: '인기순' }))}
                    >
                      인기순
                    </button>
                  </div>
                </div>

                <div className="post-grid">
                  {sorted.slice(0, 6).map(post => (
                    <div key={post.id} className="post-card">
                      {post.images[0] && (
                        <div className="card-thumb">
                          <img src={post.images[0]} alt={post.title} />
                        </div>
                      )}
                      <div className="card-body">
                        <h3 className="card-title">{post.title}</h3>
                        <p className="card-meta">
                          {post.author} · {new Date(post.createdAt).toLocaleDateString()}
                        </p>
                        <div className="card-stats">
                          <span><FaComment /> {post.comments.length}</span>
                          <span><FaThumbsUp /> {post.likes || 0}</span>
                          <span><FaEye /> {post.views || 0}</span>
                        </div>
                      </div>
                    </div>
                  ))}
                  {sorted.length === 0 && <p className="no-posts">게시물이 없습니다.</p>}
                </div>
              </div>
            );
          })}
        </div>
      </main>
    </div>
  );
}
