import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { API_BASE_URL } from "../../constants.js";

export default function BoardDetail() {
  const { bno } = useParams();
  const [board, setBoard] = useState(null);

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/boards/${bno}`)
      .then((response) => setBoard(response.data))
      .catch((error) => console.error("게시글 상세 조회 실패:", error));
  }, [bno]);

  if (!board) return <p>로딩 중...</p>;

  return (
    <div>
      <h2>{board.boardTitle}</h2>
      <p>{board.boardContent}</p>
      <p>작성자: {board.userNick}</p>
      <p>조회수: {board.boardViewCount}</p>
    </div>
  );
}
