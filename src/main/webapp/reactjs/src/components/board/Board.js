import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useLocation } from "react-router-dom";
import { format, isToday } from "date-fns";
import styles from "./css/Board.module.css";
import { API_BASE_URL } from "../../constants.js";

export default function Board() {
  const location = useLocation();
  const [boards, setBoards] = useState([]);

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const category = queryParams.get("category"); // 'category' 파라미터 가져오기
    const url = category
      ? `${API_BASE_URL}/boards?category=${category}`
      : `${API_BASE_URL}/boards`;

    axios
      .get(url)
      // 오류방지를 위해 데이터가 없을 경우 빈 배열
      .then((response) => {
        setBoards(response.data || []);
        // 데이터 확인용
        console.log(response);
      })
      .catch((error) => {
        console.error("게시판 데이터를 가져오는 중 오류 발생:", error);
      });
  }, [location.search]);

  return (
    <div>
      <h1>게시판</h1>
      <div>
        <Link to={`/board`}>전체</Link>&nbsp;
        <Link to={`/board/?category=2`}>자유게시판</Link>&nbsp;
        <Link to={`/board/?category=1`}>Q&A</Link>
      </div>
      <table className={styles.board}>
        <thead>
          <tr>
            <th>번호</th>
            <th>카테고리</th>
            <th>제목</th>
            <th>글쓴이</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody>
          {boards.length > 0 ? (
            boards.map((board) => (
              <tr key={board.boardNo}>
                <td>{board.boardNo}</td>
                <td>{board.categoryName}</td>
                <td>
                  <Link to={`/board/${board.boardNo}`}>{board.boardTitle}</Link>
                </td>
                <td>{board.userNick}</td>
                <td>
                  {isToday(new Date(board.boardWriteDate))
                    ? format(new Date(board.boardWriteDate), "HH:mm")
                    : format(new Date(board.boardWriteDate), "yyyy.MM.dd")}
                </td>
                <td>{board.boardViewCount}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td>게시글이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>
      <div>
        <Link to={`/board/create`}>
          <button>글쓰기</button>
        </Link>
      </div>
    </div>
  );
}
