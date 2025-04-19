// src/pages/Login.jsx
import React from 'react';
import './Login.css';

export default function Login() {
  return (
    <div className="login-page">
      <h2>로그인</h2>
      <input type="email" placeholder="이메일" />
      <input type="password" placeholder="비밀번호" />
      <button>로그인</button>
    </div>
  );
}
