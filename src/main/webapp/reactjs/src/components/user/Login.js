import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAppContext } from "../../context/AppContext"; // ✅ context 불러오기
import styles from "./css/Login.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function Login() {
  const [userId, setUserId] = useState("");
  const [userPasswd, setUserPasswd] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const { setUser } = useAppContext(); // ✅ context에 로그인 정보 저장할 함수

  const handleLogin = (e) => {
    e.preventDefault();

    axios
      .post(
        `${API_BASE_URL}/login`,
        {
          userId,
          userPasswd,
        },
        {
          withCredentials: true, // 세션 쿠키 저장
        }
      )
      .then((response) => {
        setUser(response.data); // ✅ context에도 저장!
        alert("로그인 성공!");
        navigate("/");
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          setError("아이디 또는 비밀번호가 틀렸습니다.");
        } else {
          setError("서버 오류가 발생했습니다.");
        }
      });
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>로그인</h2>
      <form className={styles.login} onSubmit={handleLogin}>
        <div>
          <label>아이디: </label>
          <input
            type="text"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </div>
        <div>
          <label>비밀번호: </label>
          <input
            type="password"
            value={userPasswd}
            onChange={(e) => setUserPasswd(e.target.value)}
            required
          />
        </div>
        <button type="submit">로그인</button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
