// ✅ src/components/Sidebar.jsx
import React from 'react';
import { Link } from 'react-router-dom'; // 반드시 추가
import './Sidebar.css';

export default function Sidebar({ isOpen, toggleSidebar }) {
  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="sidebar-links">
        {/* ✅ 로그인/회원가입 메뉴 */}
        <Link to="/login" onClick={toggleSidebar} className="auth-link">Login/Sign up</Link>

        {/* ✅ 메뉴 순서대로 배치 */}
        <a href="/" onClick={toggleSidebar}>HOME</a>
        <a href="/group" onClick={toggleSidebar}>GROUP</a>
        <a href="/board" onClick={toggleSidebar}>BOARD</a>
        <a href="/map" onClick={toggleSidebar}>MAP</a>        
        <a href="/schedule" onClick={toggleSidebar}>SCHEDULE</a>
        <a href="/mypage" onClick={toggleSidebar}>MYPAGE</a>
      </div>
    </div>
  );
}
