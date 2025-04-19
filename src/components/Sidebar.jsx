// 📁 src/components/Sidebar.jsx
import React from 'react';
import './Sidebar.css';

export default function Sidebar({ isOpen }) {
  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="sidebar-links">
        <a href="#login" className="auth-link">Login/Sign up</a>
        <a href="/">HOME</a>
        <a href="/group">GROUP</a>
        <a href="/board">BOARD</a>
        <a href="/schedule">SCHEDULE</a>
        <a href="/mypage">MYPAGE</a>
      </div>
    </div>
  );
}
