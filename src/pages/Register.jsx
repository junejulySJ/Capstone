// src/pages/Register.jsx
import React from 'react';
import './Register.css';

export default function Register() {
  return (
    <div className="register-page">
      <h2>회원가입</h2>
      <input type="text" placeholder="닉네임" />
      <input type="email" placeholder="이메일" />
      <input type="password" placeholder="비밀번호" />
      <button>가입하기</button>
    </div>
  );
}
