import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Mypage.css';
import { myPosts } from "../data/myPosts";
import { getPostsFromLocal, removePostFromLocal } from '../utils/storage';

const mockUser = {
  id: '홍길동',
  avatarUrl: '/images/defaultPro.png',
};

const Mypage = () => {
  const [view, setView] = useState('home');
  const [avatar, setAvatar] = useState(mockUser.avatarUrl); // 상태로 관리
  const navigate = useNavigate();
  const [likedPosts, setLikedPosts] = useState(getPostsFromLocal("like"));
  const [savedPosts, setSavedPosts] = useState(getPostsFromLocal("save"));

  const handleAvatarChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const newAvatarUrl = URL.createObjectURL(file);
      setAvatar(newAvatarUrl);
    }
  };

  const handleLogout = () => {
    if (window.confirm('정말 로그아웃 하시겠습니까?')) {
      navigate('/');
    }
  };

const handleDeleteLiked = (postId) => {
  removePostFromLocal("like", postId);
  setLikedPosts(getPostsFromLocal("like"));
};

const handleDeleteSaved = (postId) => {
  removePostFromLocal("save", postId);
  setSavedPosts(getPostsFromLocal("save"));
};

  return (
    <div className="mypage-container">
      <aside className="mypage-sidebar">
        <div className="profile-section">
          <div className="avatar-wrapper">
          <img src={avatar} alt="프로필 사진" className="profile-avatar" />
            <label htmlFor="avatar-upload" className="avatar-upload-btn">📷
              <input type="file" id="avatar-upload" accept="image/*" onChange={handleAvatarChange} />
            </label>
          </div>
          <div className="user-name">{mockUser.id}</div>
        </div>
        <nav className="menu-section">
          <ul className="menu-list">
            <li onClick={() => setView('posts')}>내 게시물</li>
            <li onClick={() => setView('liked')}>좋아요/저장한 글</li>
            <li onClick={() => setView('friends')}>친구목록</li>
            <li onClick={() => setView('schedules')}>일정</li>
            <li onClick={() => setView('settings')}>설정</li>
          </ul>
        </nav>
      </aside>

      <main className="mypage-content">
        {view === 'home' && (
          <div className="welcome-text">
            {mockUser.id}님 반갑습니다!
          </div>
        )}
        {view === 'posts' && (
          <div>
            <h2>내 게시물</h2>
              <div className="post-list">
                {myPosts.map((post) => (
                  <div key={post.id} className="post-card" onClick={() => navigate(`/board?postId=${post.id}`)}>
                    <img src={post.image} alt={post.title} className="post-thumb" />
                      <div className="post-info">
                        <h3>{post.title}</h3>
                        <p>{post.category} · {post.time}</p>
                        <p>조회수 {post.views} · 좋아요 {post.likes}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          {view === 'liked' && (
  <div className="liked-saved-wrapper">
    <h2>📌 좋아요/저장한 글</h2>

    {/* 좋아요 섹션 */}
    <section className="liked-saved-section">
      <h3 className="section-subtitle">📌 좋아요한 글</h3>
      {likedPosts.length === 0 ? (
        <p className="empty-text">좋아요한 글이 없습니다.</p>
      ) : (
        <ul className="liked-posts-list">
          {likedPosts.map((post) => (
            <li key={post.id} className="liked-post-item">
              <img src={post.image} alt={post.title} />
              <div>
                <h3>{post.title}</h3>
                <p>{post.category} · {post.time}</p>
                <button onClick={() => handleDeleteLiked(post.id)}>삭제</button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </section>

    {/* 저장한 글 섹션 */}
    <section className="liked-saved-section">
      <h3 className="section-subtitle">📌 저장한 글</h3>
      {savedPosts.length === 0 ? (
        <p className="empty-text">저장한 글이 없습니다.</p>
      ) : (
        <ul className="liked-posts-list">
          {savedPosts.map((post) => (
            <li key={post.id} className="liked-post-item">
              <img src={post.image} alt={post.title} />
              <div>
                <h3>{post.title}</h3>
                <p>{post.category} · {post.time}</p>
                <button onClick={() => handleDeleteSaved(post.id)}>삭제</button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </section>
  </div>
)}


        {view === 'friends' && <div>친구목록 화면 구성</div>}
        {view === 'schedules' && <div>일정 화면 구성</div>}
        {view === 'settings' && <div>설정 화면 구성</div>}
      </main>
    </div>
  );
};

export default Mypage;
