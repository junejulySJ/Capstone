import React, { useState } from 'react';
import './Board.css';
// 더미 초기 데이터
import initialPosts from '../data/initialPosts';

export default function Board() {
  const [mode, setMode] = useState('list');
  const [posts, setPosts] = useState(
    // 최근 날짜순으로 정렬
    [...initialPosts].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  );
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 10;

  const [selectedPost, setSelectedPost] = useState(null);

  // 새 글 폼 필드
  const [newAuthor, setNewAuthor] = useState('');
  const [newTitle, setNewTitle] = useState('');
  const [newContent, setNewContent] = useState('');
  const [newImages, setNewImages] = useState([]);

  // 댓글 폼
  const [commentInput, setCommentInput] = useState('');

  // 페이지 이동
  const totalPages = Math.ceil(posts.length / postsPerPage);
  const startIndex = (currentPage - 1) * postsPerPage;
  const currentPosts = posts.slice(startIndex, startIndex + postsPerPage);

  // 목록 클릭 시 상세 뷰
  const handleSelectPost = (post) => {
    setSelectedPost(post);
    setMode('detail');
  };

  // 새 글 등록
  const handleNewPostSubmit = (e) => {
    e.preventDefault();
    const nextId = posts.length ? Math.max(...posts.map(p => p.id)) + 1 : 1;
    const newPost = {
      id: nextId,
      author: newAuthor,
      title: newTitle,
      content: newContent,
      createdAt: new Date().toISOString(),
      images: newImages.map(file => URL.createObjectURL(file)),
      comments: []
    };
    const updated = [newPost, ...posts];
    setPosts(updated);
    setNewAuthor('');
    setNewTitle('');
    setNewContent('');
    setNewImages([]);
    setCurrentPage(1);
    setMode('list');
  };

  // 댓글 등록
  const handleCommentSubmit = (e) => {
    e.preventDefault();
    const newComment = {
      id: selectedPost.comments.length ? Math.max(...selectedPost.comments.map(c => c.id)) + 1 : 1,
      author: '익명',
      text: commentInput,
      createdAt: new Date().toISOString()
    };
    // 선택된 게시글에 댓글 추가
    const updatedPost = {
      ...selectedPost,
      comments: [...selectedPost.comments, newComment]
    };
    setPosts(posts.map(p => p.id === updatedPost.id ? updatedPost : p));
    setSelectedPost(updatedPost);
    setCommentInput('');
  };

  // 이미지 파일 선택
  const handleImageChange = (e) => {
    setNewImages(Array.from(e.target.files));
  };

  // 목록 화면
  if (mode === 'list') {
    return (
      <div className="board-page">
        <div className="board-header">
          <h2>Board</h2>
          <button className="new-post-btn" onClick={() => setMode('new')}>글 작성</button>
        </div>
        {posts.length ? (
          <>
            <table className="post-table">
              <thead>
                <tr>
                  <th>제목</th>
                  <th>작성자</th>
                  <th>등록일</th>
                </tr>
              </thead>
              <tbody>
                {currentPosts.map(post => (
                  <tr key={post.id} onClick={() => handleSelectPost(post)}>
                    <td>{post.title}</td>
                    <td>{post.author}</td>
                    <td>{new Date(post.createdAt).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="pagination">
              <button
                onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                disabled={currentPage === 1}
              >
                이전
              </button>
              <span>{currentPage} / {totalPages}</span>
              <button
                onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                disabled={currentPage === totalPages}
              >
                다음
              </button>
            </div>
          </>
        ) : (
          <p className="no-posts">등록된 게시글이 없습니다.</p>
        )}
      </div>
    );
  }


  // 상세 화면
  if (mode === 'detail' && selectedPost) {
    return (
      <div className="board-page">
        <button className="back-btn" onClick={() => setMode('list')}>← 목록으로</button>
        <h2>{selectedPost.title}</h2>
        <p className="post-meta">{selectedPost.author} · {new Date(selectedPost.createdAt).toLocaleString()}</p>
        <div className="post-content">{selectedPost.content}</div>
        {selectedPost.images.length > 0 && (
          <div className="post-images">
            {selectedPost.images.map((url, idx) => (
              <img key={idx} src={url} alt={`img-${idx}`} className="post-image" />
            ))}
          </div>
        )}
        <hr />
        <div className="comments-section">
          <h3>댓글</h3>
          {selectedPost.comments.map(c => (
            <div key={c.id} className="comment-item">
              <p><strong>{c.author}</strong> · {new Date(c.createdAt).toLocaleTimeString()}</p>
              <p>{c.text}</p>
            </div>
          ))}
          <form className="comment-form" onSubmit={handleCommentSubmit}>
            <input
              type="text"
              value={commentInput}
              onChange={e => setCommentInput(e.target.value)}
              placeholder="댓글을 입력하세요"
              required
            />
            <button type="submit">등록</button>
          </form>
        </div>
      </div>
    );
  }

  // 새 게시글 작성 화면
  if (mode === 'new') {
    return (
      <div className="board-page">
        <button className="back-btn" onClick={() => setMode('list')}>← 목록으로</button>
        <h2>게시글 작성</h2>
        <form className="new-post-form" onSubmit={handleNewPostSubmit}>
          <input
            type="text"
            value={newAuthor}
            onChange={e => setNewAuthor(e.target.value)}
            placeholder="작성자"
            required
          />
          <input
            type="text"
            value={newTitle}
            onChange={e => setNewTitle(e.target.value)}
            placeholder="제목"
            required
          />
          <textarea
            value={newContent}
            onChange={e => setNewContent(e.target.value)}
            placeholder="내용"
            required
          />
          <input type="file" multiple onChange={handleImageChange} />
          <button type="submit">등록</button>
        </form>
      </div>
    );
  }

  return null;
}
