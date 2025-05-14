import React, { useState, useEffect } from 'react';
import { FaSearch } from "react-icons/fa";
import { useLocation } from 'react-router-dom';
import "./Board.css";
import dummyPosts from "../data/dummyPosts";
import freeBoardPosts from "../data/freeBoardPosts";
import blogPosts from "../data/blogPosts";
import qaPosts from "../data/qaPosts";
import { savePostToLocal } from '../utils/storage';

const Board = () => {
  const [activeTab, setActiveTab] = useState("main");
  const [selectedPost, setSelectedPost] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [showAll, setShowAll] = useState(false);

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const postId = params.get('postId');

  useEffect(() => {
  if (postId) {
    const found = dummyPosts.find(post => post.id.toString() === postId.toString());
    if (found) {
      setSelectedPost(found);
      setActiveTab('main'); // 필요 시 탭 고정
    }
  }
}, [postId]);

  const handleCategoryClick = (category) => {
    setSelectedCategory(category);
    setSelectedPost(null);
    setShowAll(false);
  };

  const handlePostClick = (post) => {
    setSelectedPost(post);
  };

  const handleBack = () => {
    setSelectedPost(null);
  };

  const filteredPosts = dummyPosts.filter(
    (post) => selectedCategory === "전체" || post.category === selectedCategory
  );

  const getRandomThreePosts = () => {
    const shuffled = [...filteredPosts].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, 3);
  };

  const handleShowAll = () => {
    setShowAll(true);
  };

  return (
    <div className="board-page">
      <div className="board-left-menu">
        <h1 className="menu-title">BOARD</h1>
        <nav className="menu-buttons">
          <button onClick={() => handleCategoryClick("맛집")}>맛집</button>
          <button onClick={() => handleCategoryClick("카페")}>카페</button>
          <button onClick={() => handleCategoryClick("놀거리")}>놀거리</button>
          <button onClick={() => handleCategoryClick("일정/코스")}>일정/코스</button>
        </nav>
        <div className="menu-footer">
          <p onClick={() => setActiveTab("free")}>자유게시판</p>
          <p onClick={() => setActiveTab("blog")}>블로그</p>
          <p onClick={() => setActiveTab("qa")}>Q&A</p>
        </div>
      </div>

      <div className="main-content">
        <div className="search-bar">
          <input type="text" placeholder="원하는 글을 찾아보세요" />
          <FaSearch className="search-icon" />
        </div>

        <div className="tabs">
          <button
            className={activeTab === "main" ? "active-tab" : ""}
            onClick={() => { setActiveTab("main"); setSelectedPost(null); setShowAll(false); }}
          >Main</button>
          <button
            className={activeTab === "free" ? "active-tab" : ""}
            onClick={() => { setActiveTab("free"); setSelectedPost(null); setShowAll(false); }}
          >자유게시판</button>
        </div>

        {selectedPost ? (
  <div className="post-detail">
    <button className="back-btn" onClick={handleBack}>← 뒤로가기</button>
    <h2>{selectedPost.title}</h2>
    <p className="description">{selectedPost.description}</p>
    <p className="timestamp">{selectedPost.writer} · {selectedPost.time}</p>
    <div className="content">{selectedPost.content}</div>

    {/* ✅ 좋아요/저장 버튼 추가 */}
    <div className="post-actions" style={{ marginTop: '1rem' }}>
      <button
        className="like-btn"
        onClick={() => savePostToLocal("like", selectedPost)}
      >
        ❤️ 좋아요
      </button>
      <button
        className="save-btn"
        onClick={() => savePostToLocal("save", selectedPost)}
      >
        📌 저장하기
      </button>
    </div>
  </div>
        ) : (
          activeTab === "main" ? (
            <>
              <div className="post-list">
                {(showAll ? filteredPosts : getRandomThreePosts()).map((post) => (
                  <div key={post.id} className="post-item" onClick={() => handlePostClick(post)}>
                    <h2>{post.title}</h2>
                    <p className="description">{post.description}</p>
                    <p className="timestamp">{post.writer} · {post.time}</p>
                  </div>
                ))}
              </div>

              {!showAll && (
                <div style={{ textAlign: "right", marginTop: "1rem" }}>
                  <button className="more-btn" onClick={handleShowAll}>더보기 &gt;</button>
                </div>
              )}

              <div className="popular-posts">
                <h3>🔥 인기글 <span className="time">오후 7시 기준</span></h3>
                <div className="popular-grid">
                  <div className="popular-thumbnail"></div>
                  <div className="popular-thumbnail"></div>
                  <div className="popular-thumbnail"></div>
                  <div className="popular-thumbnail"></div>
                  <div className="popular-thumbnail"></div>
                </div>
                <button className="more-btn">더보기 &gt;</button>
              </div>
            </>
          ) : activeTab === "free" ? (
    <div className="post-list">
      {freeBoardPosts.map((post) => (
        <div key={post.id} className="post-item" onClick={() => handlePostClick(post)}>
          <h2>{post.title}</h2>
          <p className="description">{post.description}</p>
          <p className="timestamp">{post.writer} · {post.time}</p>
        </div>
      ))}
    </div>
  ) : activeTab === "blog" ? (
  <div className="post-list">
    {blogPosts.map((post) => (
      <a
        key={post.id}
        href={post.url}
        target="_blank"
        rel="noopener noreferrer"
        className="post-item"
        style={{ textDecoration: "none", color: "inherit" }}
      >
        <h2>{post.title}</h2>
        <p className="description">{post.description}</p>
        <p className="timestamp">{post.writer} · {post.time}</p>
      </a>
    ))}
  </div>
) : activeTab === "qa" ? (
  <div className="post-list">
    {qaPosts.map((post) => (
      <div key={post.id} className="post-item" onClick={() => handlePostClick(post)}>
        <h2>{post.title}</h2>
        <p className="description">{post.description}</p>
        <p className="timestamp">{post.writer} · {post.time}</p>
      </div>
    ))}
  </div>
) : null
)}
</div>
</div> );};

export default Board;
