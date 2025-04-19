import React, { useEffect, useState } from "react";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import { Link, useLocation, useNavigate } from "react-router-dom";
import styles from "./css/Friend.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function ReceivedFriend() {
  const { user } = useAppContext();
  const location = useLocation();
  const [friends, setFriends] = useState([]);
  const navigate = useNavigate();
  const [error, setError] = useState("");

  useEffect(() => {
    // user가 없으면 로그인 페이지로 리다이렉트
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
      return;
    }

    const url = `${API_BASE_URL}/users/${user.userId}/friends/received`;

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
  }, [location.search]);

  const handleApprove = (friendshipNo) => {
    axios
      .post(
        `${API_BASE_URL}/users/${user.userId}/friends/approve/${friendshipNo}`,
        {},
        {
          withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
        }
      )
      .then((response) => {
        alert("요청 수락!");
        setFriends((prevFriends) =>
          prevFriends.filter((friend) => friend.friendshipNo !== friendshipNo)
        );
      })
      .catch((error) => {
        if (error.response) {
          setError("수락에 실패했습니다.");
        } else {
          setError("서버 오류가 발생했습니다.");
        }
      });
  };

  return (
    <div>
      <h1>보낸 요청</h1>
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
            <th>상태</th>
            <th>수락</th>
          </tr>
        </thead>
        <tbody>
          {friends.length > 0 ? (
            friends.map((friend) => (
              <tr key={friend.friendshipNo}>
                <td>{friend.opponentId}</td>
                <td>{friend.opponentNick}</td>
                <td>{friend.status}</td>
                <td>
                  <button onClick={() => handleApprove(friend.friendshipNo)}>
                    수락
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={4}>받은 친구 요청이 없습니다 :&#40;</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
