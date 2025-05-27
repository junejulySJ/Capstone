import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Header.css';

function Header({ toggleSidebar }) {
  const navigate = useNavigate();
  const location = useLocation();

  if (location.pathname === '/login' || location.pathname === '/register') return null;

  return (
    <div className="header-bar">
      <button className="hamburger" onClick={toggleSidebar}>☰</button>
      <div className="login-buttons">
        <button onClick={() => navigate('/login')}>로그인</button>
        <button onClick={() => navigate('/register')}>회원가입</button>
      </div>
    </div>
  );
}

export default Header;
