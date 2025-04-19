import React, { useEffect, useState } from "react";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styles from "./css/BoardCreate.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function BoardCreate() {
  const { user, setUser } = useAppContext();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [category, setCategory] = useState(2);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // user가 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
    }
  }, [user, navigate]);

  const handleCreate = (e) => {
    e.preventDefault();

    axios
      .post(
        `${API_BASE_URL}/boards/create`,
        {
          boardTitle: title,
          boardContent: content,
          userId: user.userId,
          categoryNo: category,
        },
        {
          withCredentials: true, // 세션 쿠키를 포함하여 요청 보냄
        }
      )
      .then((response) => {
        alert("글쓰기 성공!");
        navigate("/board");
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
      <form onSubmit={handleCreate}>
        <table className={styles.create}>
          <tr>
            <td>
              <label htmlFor="category">카테고리</label>
            </td>
          </tr>
          <tr>
            <td>
              <select
                id="category"
                value={category}
                onChange={(e) => setCategory(Number(e.target.value))}
              >
                <option value="2">자유게시판</option>
                <option value="1">Q&A</option>
              </select>
            </td>
          </tr>
          <tr>
            <td className={styles.header}>제목</td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                placeholder=" 제목을 입력하세요"
                onChange={(e) => setTitle(e.target.value)}
              />
            </td>
          </tr>
          <tr>
            <td>
              <label htmlFor="contents">내용</label>
            </td>
          </tr>
          <tr>
            <td>
              <textarea
                id="contents"
                onChange={(e) => setContent(e.target.value)}
              ></textarea>
            </td>
          </tr>
          <tr>
            <td>
              <label htmlFor="author">작성자</label>
            </td>
          </tr>
          <tr>
            <td>
              <input
                type="text"
                id="author"
                value={user ? user.userNick : ""}
                disabled
              />
            </td>
          </tr>
          <tr>
            <td>
              <button type="submit">등록</button>
            </td>
          </tr>
        </table>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
