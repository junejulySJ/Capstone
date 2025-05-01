// src/pages/Mypage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Mypage.css';

const mockUser = {
  id: 'User1',
  avatarUrl: '/images/default-avatar.png',
  posts: [
    { id: 1, title: '강남 맛집 후기', preview: '이 집 김치찌개가 참 맛있었어요...' },
    { id: 2, title: '홍대 카페 후기', preview: '분위기 좋고, 디저트 추천합니다.' },
  ],
  liked: [
    { id: 3, title: '이태원 바 후기', preview: '칵테일이 독특했어요.' },
  ],
  friends: [
    { id: 1, name: '홍길동' },
    { id: 2, name: '김철수' },
  ],
  schedules: [
    { id: 1, date: '2025-05-03', title: '헬스장 가기' },
    { id: 2, date: '2025-05-05', title: '팀 미팅' },
  ],
};

const Mypage = () => {
  const [view, setView] = useState('home');
  const [avatar, setAvatar] = useState(mockUser.avatarUrl);
  const [selectedPost, setSelectedPost] = useState(null);
  const [editingPost, setEditingPost] = useState(null);
const [posts, setPosts] = useState(mockUser.posts);
const navigate = useNavigate();
const [isLoggedIn, setIsLoggedIn] = useState(true);

  const user = mockUser;

  const handleAvatarChange = e => {
    const file = e.target.files[0];
    if (file) setAvatar(URL.createObjectURL(file));
  };

  const handleEditPost = (post) => {
    setEditingPost(post);
  };
  
  const handleDeletePost = (postId) => {
    if (window.confirm('정말 이 게시글을 삭제하시겠습니까?')) {
      const updatedPosts = posts.filter(p => p.id !== postId);
      setPosts(updatedPosts);
      mockUser.posts = updatedPosts; // mockUser도 같이 업데이트해줌
    }
  };
  
  const handleLogout = () => {
    if (window.confirm('정말 로그아웃 하시겠습니까?')) {
      setIsLoggedIn(false);
      navigate('/'); // 홈 화면으로 이동
    }
  };
  

  return (
    <div className="mypage-container">
      <aside className="sidebar">
        <ul className="sidebar-menu">
        <li className="menu-item"><button onClick={() => { setView('home'); setSelectedPost(null); }}>내 정보</button></li>
        <li className="menu-item"><button onClick={() => { setView('posts'); setSelectedPost(null); }}>내 게시글</button></li>
          <li className="menu-item"><button onClick={() => setView('liked')}>좋아요/저장 글</button></li>
          <li className="menu-item"><button onClick={() => setView('friends')}>내 친구</button></li>
          <li className="menu-item"><button onClick={() => setView('schedules')}>내 일정</button></li>
          <li className="menu-item"><button onClick={() => setView('settings')}>설정</button></li>
          <li className="menu-item"><button onClick={handleLogout}>로그아웃</button>
</li>

        </ul>
      </aside>

      <main className="content">
        {view === 'home' && (
          <>
          <section className="content-section">
            <h2>내 정보</h2>
            <div className="profile-card">
              <div className="avatar-wrapper">
                <img src={avatar} alt="사진" className="avatar" />
                <label htmlFor="avatar-upload" className="avatar-edit">📷
                  <input type="file" id="avatar-upload" accept="image/*" onChange={handleAvatarChange} />
                </label>
              </div>
              <div className="data-list">
                <p><strong>아이디:</strong> {user.id}</p>
              </div>
            </div>
          </section>


          <section className="content-section">
            <div className="quick-links">
              <button onClick={() => setView('posts')} className="quick-btn">내 게시글</button>
              <button onClick={() => setView('schedules')} className="quick-btn">내 일정</button>
              <button onClick={() => setView('friends')} className="quick-btn">내 친구</button>
              <button onClick={() => setView('liked')} className="quick-btn">좋아요/저장한 글</button>
              <button onClick={() => setView('settings')} className="quick-btn">설정</button>
              <button onClick={() => setView('logout')} className="quick-btn">로그아웃</button>
            </div>
          </section>
          </>
        )}

{view === 'posts' && !selectedPost && (
  <section className="content-section">
    <button className="back-btn" onClick={() => setView('home')}>← 뒤로가기</button>
    <h2>내 게시글</h2>
    {user.posts.map(p => (
      <article key={p.id} className="post-item" style={{ position: 'relative' }}>
        <h4 onClick={() => setSelectedPost(p)} style={{ cursor: 'pointer' }}>{p.title}</h4>
        <p onClick={() => setSelectedPost(p)} style={{ cursor: 'pointer' }}>{p.preview}</p>

        {/* 수정 및 삭제 버튼 */}
        <div style={{ position: 'absolute', top: 10, right: 10 }}>
          <button
            className="edit-btn"
            onClick={() => handleEditPost(p)}
            style={{ marginRight: '8px' }}
          >
            ✏️ 수정
          </button>
          <button
            className="delete-btn"
            onClick={() => handleDeletePost(p.id)}
          >
            🗑 삭제
          </button>
        </div>
      </article>
    ))}
  </section>
)}

{view === 'posts' && editingPost && (
  <section className="content-section">
    <button className="back-btn" onClick={() => setEditingPost(null)}>← 목록으로</button>
    <h2>게시글 수정</h2>
    <input
      type="text"
      value={editingPost.title}
      onChange={(e) => setEditingPost({ ...editingPost, title: e.target.value })}
      className="edit-input"
      placeholder="제목 수정"
    />
    <textarea
      value={editingPost.preview}
      onChange={(e) => setEditingPost({ ...editingPost, preview: e.target.value })}
      className="edit-textarea"
      placeholder="내용 수정"
    />
    <button
      className="save-btn"
      onClick={() => {
        const updatedPosts = posts.map(p => (p.id === editingPost.id ? editingPost : p));
        setPosts(updatedPosts);
        mockUser.posts = updatedPosts;
        setEditingPost(null);
      }}
    >
      저장
    </button>
  </section>
)}



        {view === 'liked' && (
          <section className="content-section">
            <button className="back-btn" onClick={() => setView('home')}>← 뒤로가기</button>
            <h2>좋아요/저장한 글</h2>
            {user.liked.map(p => (
              <article key={p.id} className="post-item">
                <h4>{p.title}</h4>
                <p>{p.preview}</p>
              </article>
            ))}
          </section>
        )}

        {view === 'friends' && (
          <section className="content-section">
            <button className="back-btn" onClick={() => setView('home')}>← 뒤로가기</button>
            <h2>내 친구</h2>
            <ul className="friend-list">
              {user.friends.map(f => (<li key={f.id}>{f.name}</li>))}
            </ul>
          </section>
        )}

        {view === 'schedules' && (
          <section className="content-section">
            <button className="back-btn" onClick={() => setView('home')}>← 뒤로가기</button>
            <h2>내 일정</h2>
            {user.schedules.map(s => (
              <div key={s.id} className="schedule-item">
                <span className="date">{s.date}</span>
                <span className="title">{s.title}</span>
              </div>
            ))}
          </section>
        )}

{view === 'settings' && (
  <section className="content-section">
    <button className="back-btn" onClick={() => setView('home')}>← 뒤로가기</button>
    <h2>설정</h2>

    {/* 닉네임 변경 */}
    <div className="setting-item">
      <label htmlFor="nickname">닉네임 변경:</label>
      <input
        id="nickname"
        type="text"
        placeholder="새 닉네임 입력"
        value={user.id} // mockUser 수정 없이 보여주기만 할거야
        readOnly
        style={{ marginBottom: '10px' }}
      />
      <p style={{ fontSize: '0.9em', color: '#888' }}>※ 현재는 수정 기능만 준비중입니다.</p>
    </div>

    {/* 알림 설정 */}
    <div className="setting-item">
      <label>알림 설정:</label>
      <button
        className="toggle-btn"
        onClick={() => alert('알림 설정 기능은 준비중입니다')}
      >
        켜기/끄기
      </button>
    </div>

    {/* 테마 설정 */}
    <div className="setting-item">
      <label>테마 설정:</label>
      <button
        className="toggle-btn"
        onClick={() => {
          document.body.classList.toggle('dark-mode');
          alert('테마가 변경되었습니다.');
        }}
      >
        다크모드 전환
      </button>
    </div>

    {/* 계정 삭제 */}
    <div className="setting-item">
      <label>계정 삭제:</label>
      <button
        className="danger-btn"
        onClick={() => {
          if (window.confirm('정말 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
            alert('계정 삭제 처리되었습니다. (실제 삭제 아님)');
          }
        }}
      >
        계정 삭제
      </button>
    </div>
  </section>
)}

      </main>
    </div>
  );
};

export default Mypage;
