import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import React, { useEffect, useState } from "react";
import styles from "./css/Friend.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function Friend() {
  const { user } = useAppContext();
  const location = useLocation();
  const navigate = useNavigate();
  const [friends, setFriends] = useState([]);

  useEffect(() => {
    // user가 없으면 로그인 페이지로 리다이렉트
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
      return;
    }

    const url = `${API_BASE_URL}/users/${user.userId}/friends`;

    axios
      .get(url, {
        withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
      })
      // 오류방지를 위해 데이터가 없을 경우 빈 배열
      .then((response) => {
        setFriends(response.data || []);
        // 데이터 확인용
        console.log(response);
      })
      .catch((error) => {
        console.error("친구 데이터를 가져오는 중 오류 발생:", error);
      });
  }, [location.search, user, navigate]);

  return (
    <div>
      <h1>친구</h1>
      <div>
        <Link to={`/friend`}>친구 목록</Link>&nbsp;
        <Link to={`/friend/sent`}>보낸 요청</Link>&nbsp;
        <Link to={`/friend/received`}>받은 요청</Link>
      </div>
      <table className={styles.friend}>
        <thead>
          <tr>
            <th>아이디</th>
            <th>닉네임</th>
          </tr>
        </thead>
        <tbody>
          {friends.length > 0 ? (
            friends.map((friend) => (
              <tr key={friend.friendshipNo}>
                <td>{friend.opponentId}</td>
                <td>{friend.opponentNick}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3}>친구가 없습니다 :&#40;</td>
            </tr>
          )}
        </tbody>
      </table>
      <div>
        <Link to={`/friend/add`}>
          <button>친구 추가</button>
        </Link>
      </div>
    </div>
  );
}
