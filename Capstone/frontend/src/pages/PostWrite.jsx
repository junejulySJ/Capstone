import React, { useState } from 'react';
import axios from 'axios';
import { useAppContext } from '../AppContext';
import { useNavigate } from 'react-router-dom';
import './PostWrite.css';

const API_BASE = 'http://localhost:8080/api';

const PostWrite = () => {
  const { user } = useAppContext();
  const navigate = useNavigate();

  const [boardTitle, setBoardTitle] = useState('');
  const [boardDescription, setBoardDescription] = useState('');
  const [boardContent, setBoardContent] = useState('');
  const [categoryNo, setCategoryNo] = useState(0);  // 카테고리 기본값을 설정
  const [file, setFile] = useState(null);  // 파일 상태 추가

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!user) {
      alert('로그인 후 게시글을 작성할 수 있습니다.');
      return;
    }

    // FormData 객체 생성
    const formData = new FormData();

    // 게시글 데이터를 FormData에 추가
    const postData = {
      boardTitle,
      boardDescription,
      boardContent,
      categoryNo,
    };

    

    // JSON 형식으로 postData를 stringify 해서 추가
    formData.append("json", JSON.stringify(postData));

    // 파일이 있다면 파일도 추가
    if (file) {
      formData.append("files", file);
    }

    // 폼 데이터를 서버로 보내기
    try {
      const res = await axios.post(`${API_BASE}/boards`, formData, {
        headers: {
          "Content-Type": "multipart/form-data", // 반드시 multipart/form-data로 설정
        },
        withCredentials: true, // 쿠키 전달
      });

      alert('게시글 작성이 완료되었습니다.');
      navigate('/board'); // 게시글 목록 페이지로 이동
    } catch (err) {
      console.error('게시글 작성 실패:', err);
      alert('게시글 작성에 실패했습니다.');
    }
  };





    return (
        <div className="post-write-page">
            <button onClick={() => navigate(-1)} className="back-button">← 뒤로가기</button>
            <h2>게시글 작성</h2>
            <form onSubmit={handleSubmit} className="post-write-form">
                <div className="form-group">
                    <label>제목</label>
                    <input
                        type="text"
                        value={boardTitle}
                        onChange={(e) => setBoardTitle(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>설명</label>
                    <input
                        type="text"
                        value={boardDescription}
                        onChange={(e) => setBoardDescription(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>내용</label>
                    <textarea
                        value={boardContent}
                        onChange={(e) => setBoardContent(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>카테고리</label>
                    <select value={categoryNo} onChange={(e) => setCategoryNo(Number(e.target.value))}>
                        {user?.userType === 0 ? (
                            // 어드민일 경우 공지사항, Q&A, 자유게시판 모두 보이기
                            <>
                                <option value={0}>공지사항</option>
                                <option value={1}>Q&A</option>
                                <option value={2}>자유게시판</option>
                            </>
                        ) : (
                            // 유저일 경우 자유게시판, Q&A만 보이기
                            <>
                                <option value={1}>Q&A</option>
                                <option value={2}>자유게시판</option>
                            </>
                        )}
                    </select>
                </div>

                <div className="form-group">
                    <button type="submit">게시글 작성</button>
                </div>
            </form>
        </div>
    );
};

export default PostWrite;
