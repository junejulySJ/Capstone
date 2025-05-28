// 📁 src/App.js
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Header from './pages/Header'; // ✅ 추가
import Home from './pages/Home';
import Group from './pages/Group';
import Board from './pages/Board';
import Schedule from './pages/Schedule';
import Mypage from './pages/Mypage';
import Login from './pages/Login';
import Register from './pages/Register';
import Map from './pages/Map';
import './App.css';
import { AppProvider } from "./AppContext";
import KakaoLogin from './pages/KakaoLogin';

function AppContent() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const location = useLocation();

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  // 로그인/회원가입 페이지가 아닐 때만 Header 표시
  const showHeader = location.pathname !== '/login' && location.pathname !== '/register';

  return (
    <div className="App">
      <AppProvider>

      {showHeader && <Header toggleSidebar={toggleSidebar} />}
      <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/group" element={<Group />} />
        <Route path="/board" element={<Board />} />
        <Route path="/schedule" element={<Schedule />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/map" element={<Map />} />
        <Route path="/auth/kakao/callback" element={<KakaoLogin />} />
      </Routes>
      </AppProvider>
    </div>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
