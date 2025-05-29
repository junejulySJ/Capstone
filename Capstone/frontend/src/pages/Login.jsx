import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import { useAppContext } from "../AppContext"; // ✅ context 불러오기
import { API_BASE_URL } from '../constants';

const predefinedUsers = ['user1','user2','user3','user4','user5','user6','user7','user8','user9','user10','admin1','admin2','admin3'];

function Login() {
  const [userId, setUserId] = useState('');
  const [userPasswd, setUserPasswd] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const { setUser } = useAppContext(); // ✅ context에 로그인 정보 저장할 함수

  const handleLogin = async () => {
    if (!userId || !userPasswd) {
      setErrorMessage("아이디와 비밀번호를 모두 입력해주세요.");
      return;
    }

    const finalPassword = predefinedUsers.includes(userId) ? '123456' : userPasswd;

    try {
      const response = await axios.post(
        `${API_BASE_URL}/auth/login`,
        { userId, userPasswd: finalPassword },
        {
          withCredentials: true,
          headers: { "Content-Type": "application/json" }
        }
      );
      setUser(response.data); // ✅ context에도 저장!
      alert("로그인 성공");
      navigate("/"); // 메인페이지 이동
    } catch (error) {
      setErrorMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h2>로그인</h2>

        <label>아이디:</label>
        <input
          type="text"
          placeholder="아이디"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        />

        <label>비밀번호:</label>
        <div className="input-with-toggle">
          <input
            type={showPassword ? "text" : "password"}
            placeholder="비밀번호"
            value={userPasswd}
            onChange={(e) => setUserPasswd(e.target.value)}
          />
          <span className="eye-icon" onClick={() => setShowPassword(!showPassword)}>
            {showPassword ? '👁‍🗨' : '👁'}
          </span>
        </div>

        {errorMessage && <div className="error">{errorMessage}</div>}

        <button onClick={handleLogin}>로그인</button>

        <div style={{ width: '100%', marginBottom: '12px' }}>
  <a
    href="https://kauth.kakao.com/oauth/authorize?client_id=d88db5d8494588ec7e3f5e9aa95b78d8&redirect_uri=http://localhost:3000/auth/kakao/callback&response_type=code"
    style={{
      display: 'block',
      width: '100%',
      height: '44px',
      backgroundColor: '#FEE500',
      borderRadius: '6px',
      overflow: 'hidden'
    }}
  >
    <img
      src="/images/kakao_login_medium_narrow.png"
      alt="카카오 로그인"
      style={{
        width: '100%',
        height: '100%',
        objectFit: 'contain',
        display: 'block'
      }}
    />
  </a>
</div>

  {/* ⬇ 회원가입은 그 아래로 이동 */}
  <div className="register-link" onClick={() => navigate("/register")}>
    회원가입
  </div>
</div>
</div>
  );
}

export default Login;
