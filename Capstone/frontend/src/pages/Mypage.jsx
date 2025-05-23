import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Mypage.css';
import { myPosts } from "../data/myPosts";
import { getPostsFromLocal, removePostFromLocal } from '../utils/storage';
import { members } from '../data/members';

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
  const [searchTerm, setSearchTerm] = useState('');
  const [friends, setFriends] = useState(members);
  const [newSchedule, setNewSchedule] = useState({ title: '', date: '' });
  const [schedules, setSchedules] = useState([]);
  const [settingsTab, setSettingsTab] = useState('profile');

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

const handleRemoveFriend = (id) => {
  setFriends(friends.filter(friend => friend.id !== id));
};

const handleAddSchedule = (e) => {
  e.preventDefault();
  setSchedules([...schedules, newSchedule]);
  setNewSchedule({ title: '', date: '' });
};

const handleDeleteSchedule = (index) => {
  const updated = [...schedules];
  updated.splice(index, 1);
  setSchedules(updated);
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


       {view === 'friends' && (
  <div className="p-4">
    <h2 className="text-xl font-semibold mb-4">친구 목록</h2>

    {/* 검색창 */}
    <input
      type="text"
      placeholder="친구 이름 검색"
      className="friend-search-input"
      onChange={(e) => setSearchTerm(e.target.value)}
    />

    {/* 친구 목록 카드 */}
    <div className="friend-list">
      {friends
        .filter(friend => friend.name.includes(searchTerm))
        .map(friend => (
          <div key={friend.id} className="friend-card">
  <div className="friend-info">
    <img src={friend.avatar} alt="프로필" className="friend-avatar" />
    <div className="friend-details">
      <p className="friend-name">{friend.name}</p>
      <p className="friend-status">{friend.statusMessage}</p>
    </div>
  </div>
  <button className="friend-delete-btn" onClick={() => handleRemoveFriend(friend.id)}>
    삭제
  </button>
</div>

        ))}
    </div>
  </div>
)}

        {view === 'schedules' && (
  <div className="p-4">
    <h2 className="text-xl font-semibold mb-4">일정 관리</h2>

    {/* 일정 추가 폼 */}
    <form onSubmit={handleAddSchedule} className="schedule-form">
      <input
        type="text"
        placeholder="일정 제목"
        value={newSchedule.title}
        onChange={(e) => setNewSchedule({ ...newSchedule, title: e.target.value })}
        required
      />
      <input
        type="date"
        value={newSchedule.date}
        onChange={(e) => setNewSchedule({ ...newSchedule, date: e.target.value })}
        required
      />
      <button type="submit">추가</button>
    </form>

    {/* 일정 목록 */}
    <ul className="schedule-list">
      {schedules.map((item, index) => (
        <li key={index} className="schedule-item">
          <div>
            <strong>{item.date}</strong> - {item.title}
          </div>
          <button onClick={() => handleDeleteSchedule(index)}>삭제</button>
        </li>
      ))}
    </ul>
  </div>
)}

                {view === 'settings' && (
          <div className="settings-page">
            <div className="settings-menu">
              <ul>
                {['profile', 'account', 'privacy', 'notifications', 'withdraw'].map(tab => (
                  <li
                    key={tab}
                    className={settingsTab === tab ? 'active' : ''}
                    onClick={() => setSettingsTab(tab)}
                  >
                    {tab === 'profile' && '프로필 수정'}
                    {tab === 'account' && '계정 정보'}
                    {tab === 'privacy' && '개인정보 설정'}
                    {tab === 'notifications' && '알림 설정'}
                    {tab === 'withdraw' && '회원 탈퇴'}
                  </li>
                ))}
              </ul>
            </div>
            <div className="settings-content">
              {settingsTab === 'profile' && (
                <div>
                  <h3>프로필 수정</h3>
                  <p>프로필 사진 및 닉네임을 변경할 수 있습니다.</p>
                  <input type="text" placeholder="새 닉네임 입력" />
                  <button>저장</button>
                </div>
              )}
              {settingsTab === 'account' && (
                <div>
                  <h3>계정 정보</h3>
                  <p>이메일: user@example.com</p>
                  <input type="password" placeholder="새 비밀번호 입력" />
                  <button>변경</button>
                </div>
              )}
              {settingsTab === 'privacy' && (
                <div>
                  <h3>개인정보 설정</h3>
                  <label>
                    <input type="checkbox" /> 내 활동을 친구에게만 공개
                  </label>
                </div>
              )}
              {settingsTab === 'notifications' && (
                <div>
                  <h3>알림 설정</h3>
                  <label>
                    <input type="checkbox" /> 이메일 알림 수신 동의
                  </label>
                  <label>
                    <input type="checkbox" /> 앱 푸시 알림 허용
                  </label>
                </div>
              )}
              {settingsTab === 'withdraw' && (
                <div>
                  <h3>회원 탈퇴</h3>
                  <p>계정을 삭제하려면 비밀번호를 입력해주세요.</p>
                  <input type="password" placeholder="비밀번호" />
                  <button className="danger">탈퇴하기</button>
                </div>
              )}
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default Mypage;
