import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { dummyUsers } from "../data/dummyUsers";
import "./Login.css";

export default function Login() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const handleLogin = (e) => {
    e.preventDefault();

    const user = dummyUsers.find(
      (u) => u.userId === userId && u.password === password
    );

    if (user) {
      localStorage.setItem("userId", userId);
      navigate("/");
    } else {
      setError("아이디 또는 비밀번호가 틀렸습니다.");
    }
  };

  return (
    <div className="login-container">
      <h2>로그인</h2>
      <form onSubmit={handleLogin} className="login-form">
        <label>아이디:</label>
        <input
          type="text"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          required
        />

        <label>비밀번호:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        {error && <p className="error">{error}</p>}

        <button type="submit">로그인</button>
      </form>
      <p onClick={() => navigate("/register")} className="register-link">회원가입</p>
    </div>
  );
}