import React, { useState, useEffect } from 'react';
import { FaSearch } from "react-icons/fa";
import { useLocation } from 'react-router-dom';
import "./Board.css";
import dummyPosts from "../data/dummyPosts";
import freeBoardPosts from "../data/freeBoardPosts";
import blogPosts from "../data/blogPosts";
import qaPosts from "../data/qaPosts";
import { savePostToLocal } from '../utils/storage';

// 🔥 인기글 랜덤 추출 로직 추가
const getPopularPosts = () => {
  const shuffled = [...dummyPosts].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, 5);
};

const Board = () => {
  const [activeTab, setActiveTab] = useState("main");
  const [selectedPost, setSelectedPost] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [showAll, setShowAll] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [activeTabBeforeSearch, setActiveTabBeforeSearch] = useState("main");
  const [popularPosts, setPopularPosts] = useState([]);
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const postId = params.get('postId');

  const [blogPage, setBlogPage] = useState(1);

  const sortedBlogPosts = [...blogPosts].sort(
    (a, b) => new Date(b.time) - new Date(a.time)
  );
  const postsPerPage = 5;
  const paginatedBlogPosts = sortedBlogPosts.slice(
    (blogPage - 1) * postsPerPage,
    blogPage * postsPerPage
  );

  useEffect(() => {
  if (postId) {
    const found = dummyPosts.find(post => post.id.toString() === postId.toString());
    if (found) {
      setSelectedPost(found);
      setActiveTab('main'); // 필요 시 탭 고정
    }
  }
}, [postId]);

useEffect(() => {
    setPopularPosts(getPopularPosts());
  }, []);

  useEffect(() => {
  if (activeTab === "blog") {
    setBlogPage(1);
  }
}, [activeTab]);
  
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

  const handleSearch = () => {
    setActiveTabBeforeSearch(activeTab);
  const keyword = searchQuery.toLowerCase();

  let sourceData = [];
  if (activeTab === "main") sourceData = dummyPosts;
  else if (activeTab === "free") sourceData = freeBoardPosts;
  else if (activeTab === "qa") sourceData = qaPosts;
  else if (activeTab === "blog") sourceData = blogPosts;

  const results = sourceData.filter(post =>
    (post.title && post.title.toLowerCase().includes(keyword)) ||
    (post.description && post.description.toLowerCase().includes(keyword)) ||
    (post.content && post.content.toLowerCase().includes(keyword)) // blogPosts엔 content 없을 수도 있음
  );

  setSearchResults(results);
  setSelectedPost(null);
  setShowAll(false);
  setActiveTab("search");
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
  <input
  type="text"
  placeholder="원하는 글을 찾아보세요"
  value={searchQuery}
  onChange={(e) => setSearchQuery(e.target.value)}
  onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
/>
<FaSearch className="search-icon" onClick={handleSearch} />

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
    {popularPosts.map((post) => (
      <div
  key={post.id}
  className="popular-thumbnail"
  onClick={() => handlePostClick(post)}
  style={{
    backgroundImage: `url(/images/${post.image})`,
    backgroundSize: "cover",
    backgroundPosition: "center",
    height: "120px",
    borderRadius: "12px",
    cursor: "pointer",
    position: "relative",
    overflow: "hidden" // ✅ 핵심: 자식 요소가 튀어나오지 않게!
  }}
>
  <div
    style={{
      position: "absolute",
      bottom: 0,
      width: "100%",
      padding: "6px 8px",
      background: "rgba(0, 0, 0, 0.5)",
      color: "#fff",
      fontSize: "0.85rem",
      textAlign: "center",
      boxSizing: "border-box",  // ✅ 패딩 계산에 포함되도록
      borderBottomLeftRadius: "12px", // ✅ 이미지와 맞춤
      borderBottomRightRadius: "12px",
    }}
  >
    {post.title}
  </div>
</div>
    ))}
  </div>
  <button className="more-btn" onClick={() => setPopularPosts(getPopularPosts())}>더보기 &gt;</button>
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
    {paginatedBlogPosts.map((post) => (
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

    {/* 페이지네이션 버튼 */}
    <div
  className="pagination-buttons"
  style={{
    marginTop: '1rem',
    display: 'flex',
    justifyContent: 'center', // ✅ 가운데 정렬 핵심!
    gap: '8px' // 버튼 간격 조절
  }}
>
      {Array.from({ length: Math.ceil(blogPosts.length / postsPerPage) }, (_, i) => (
    <button
      key={i + 1}
      onClick={() => setBlogPage(i + 1)}
      className={blogPage === i + 1 ? "active-page" : ""}
      style={{
        padding: '6px 12px',
        cursor: 'pointer',
        backgroundColor: blogPage === i + 1 ? '#333' : '#f0f0f0',
        color: blogPage === i + 1 ? '#fff' : '#000',
        border: 'none',
        borderRadius: '4px',
        fontWeight: blogPage === i + 1 ? 'bold' : 'normal'
      }}
    >
      {i + 1}
    </button>
      ))}
    </div>
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
) : activeTab === "search" ? (
  <div className="post-list">
    {searchResults.length === 0 ? (
      <p>검색 결과가 없습니다.</p>
    ) : (
      searchResults.map((post) =>
        activeTabBeforeSearch === "blog" ? (
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
        ) : (
          <div
            key={post.id}
            className="post-item"
            onClick={() => handlePostClick(post)}
          >
            <h2>{post.title}</h2>
            <p className="description">{post.description}</p>
            <p className="timestamp">{post.writer} · {post.time}</p>
          </div>
        )
      )
    )}
  </div>
):null
)}
</div>
</div> );};

export default Board;
