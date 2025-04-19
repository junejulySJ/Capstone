import React, { useEffect, useState } from "react";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styles from "./css/Register.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function Register() {
  const { user, setUser } = useAppContext();
  const [id, setId] = useState("");
  const [email, setEmail] = useState("");
  const [passwd, setPasswd] = useState("");
  const [confirmPasswd, setConfirmPasswd] = useState("");
  const [nick, setNick] = useState("");
  const [address, setAddress] = useState("");
  const [idCheckMessage, setIdCheckMessage] = useState("");
  const [isIdAvailable, setIsIdAvailable] = useState(true);
  const [isPasswdMatch, setIsPasswdMatch] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // user가 있으면 홈 페이지로 리다이렉트
  useEffect(() => {
    if (user) {
      navigate("/"); // 홈 페이지로 이동
    }
  }, [user, navigate]);

  const handleIdCheck = () => {
    axios
      .post(`${API_BASE_URL}/check-id`, { userId: id }) // 아이디 중복 검사 API
      .then((response) => {
        if (response.data.available) {
          setIdCheckMessage("사용 가능한 아이디입니다.");
          setIsIdAvailable(true);
        } else {
          setIdCheckMessage("이미 사용 중인 아이디입니다.");
          setIsIdAvailable(false);
        }
      })
      .catch((error) => {
        setIdCheckMessage("서버 오류가 발생했습니다.");
        setIsIdAvailable(false);
      });
  };

  // 비밀번호 확인 변경 시 실행되는 함수
  const handleConfirmPasswdChange = (e) => {
    const value = e.target.value;
    setConfirmPasswd(value);
    setIsPasswdMatch(value === passwd); // 비밀번호 확인과 원본 비밀번호 비교
  };

  const handleCreate = (e) => {
    e.preventDefault();

    if (!isIdAvailable) {
      setError("아이디가 중복되었습니다. 다른 아이디를 사용하세요.");
      return; // 중복 아이디일 경우 폼 제출을 막음
    }

    if (!isPasswdMatch) {
      setError("비밀번호가 일치하지 않습니다.");
      return; // 비밀번호가 일치하지 않으면 폼 제출을 막음
    }

    axios
      .post(`${API_BASE_URL}/register`, {
        userId: id,
        userEmail: email,
        userPasswd: passwd,
        userNick: nick,
        userAddress: address,
      })
      .then((response) => {
        alert("회원가입 성공!");
        navigate("/");
      })
      .catch((error) => {
        if (error.response) {
          setError("회원가입에 실패했습니다.");
        } else {
          setError("서버 오류가 발생했습니다.");
        }
      });
  };

  return (
    <div>
      <h1>회원가입</h1>
      <form onSubmit={handleCreate}>
        <table className={styles.register}>
          <tr>
            <td className={styles.id}>
              <label htmlFor="id">아이디</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                value={id}
                id="id"
                placeholder="아이디를 입력하세요"
                onChange={(e) => setId(e.target.value)}
              />
              <button type="button" onClick={handleIdCheck}>
                아이디 중복 확인
              </button>
            </td>
          </tr>
          {idCheckMessage && (
            <tr>
              <td>
                <p style={{ color: isIdAvailable ? "green" : "red" }}>
                  {idCheckMessage}
                </p>
              </td>
            </tr>
          )}
          <tr>
            <td className={styles.email}>
              <label htmlFor="email">이메일</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                value={email}
                id="email"
                placeholder="이메일을 입력하세요"
                onChange={(e) => setEmail(e.target.value)}
              />
            </td>
          </tr>
          <tr>
            <td className={styles.passwd}>
              <label htmlFor="password">비밀번호</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="password"
                value={passwd}
                id="password"
                placeholder="비밀번호를 입력하세요"
                onChange={(e) => setPasswd(e.target.value)}
              ></input>
            </td>
          </tr>
          <tr>
            <td className={styles.passwd}>
              <label htmlFor="confirmPassword">비밀번호 확인</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="password"
                value={confirmPasswd}
                id="confirmPassword"
                placeholder="비밀번호를 다시 입력하세요"
                onChange={handleConfirmPasswdChange} // 비밀번호 확인 변경 시 실행
              />
              {!isPasswdMatch && confirmPasswd && (
                <p style={{ color: "red" }}>비밀번호가 일치하지 않습니다.</p>
              )}
            </td>
          </tr>
          <tr>
            <td className={styles.passwd}>
              <label htmlFor="nickname">닉네임</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                value={nick}
                id="nickname"
                placeholder="닉네임을 입력하세요"
                onChange={(e) => setNick(e.target.value)}
              ></input>
            </td>
          </tr>
          <tr>
            <td className={styles.passwd}>
              <label htmlFor="address">주소</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                value={address}
                id="address"
                placeholder="주소를 입력하세요"
                onChange={(e) => setAddress(e.target.value)}
              ></input>
            </td>
          </tr>
          <tr>
            <td>
              <button type="submit">회원가입</button>
            </td>
          </tr>
        </table>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
