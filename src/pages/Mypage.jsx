import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Mypage.css';

const mockUser = {
  id: '홍길동',
  avatarUrl: '/images/defaultPro.png',
};

const Mypage = () => {
  const [view, setView] = useState('home');
  const [avatar, setAvatar] = useState(mockUser.avatarUrl); // 상태로 관리
  const navigate = useNavigate();

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
        {view === 'posts' && <div>내 게시물 화면 구성</div>}
        {view === 'liked' && <div>좋아요/저장한 글 화면 구성</div>}
        {view === 'friends' && <div>친구목록 화면 구성</div>}
        {view === 'schedules' && <div>일정 화면 구성</div>}
        {view === 'settings' && <div>설정 화면 구성</div>}
      </main>
    </div>
  );
};

export default Mypage;
