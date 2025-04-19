import React, { useEffect, useState } from "react";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styles from "./css/AddFriend.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function AddFriend() {
  const { user } = useAppContext();
  const [opponentId, setOpponentId] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // user가 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
    }
  }, [user, navigate]);

  const handleAdd = (e) => {
    e.preventDefault();

    axios
      .post(
        `${API_BASE_URL}/users/${user.userId}/friends/add`,
        {
          opponentId: opponentId,
        },
        {
          withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
        }
      )
      .then((response) => {
        alert("친구 추가 요청 성공!");
        navigate(`/friend`);
      })
      .catch((error) => {
        if (error.response) {
          setError("글 작성에 실패했습니다.");
        } else {
          setError("서버 오류가 발생했습니다.");
        }
      });
  };

  return (
    <div>
      <h1>친구 추가</h1>
      <form onSubmit={handleAdd}>
        <table className={styles.add}>
          <tbody>
            <tr>
              <td className={styles.header}>친구 아이디</td>
            </tr>
            <tr>
              <td>
                <input
                  type="text"
                  value={opponentId}
                  placeholder="친구 아이디를 입력하세요"
                  onChange={(e) => setOpponentId(e.target.value)}
                />
              </td>
            </tr>
            <tr>
              <td>
                <button type="submit">친구 추가</button>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
