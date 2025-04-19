// 📁 src/App.js
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Home from './pages/Home';
import Group from './pages/Group';
import Board from './pages/Board';
import Schedule from './pages/Schedule';
import Mypage from './pages/Mypage';
import Login from './pages/Login';
import Register from './pages/Register';
import './App.css';

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  return (
    <Router>
      <div className="App">
        {/* ✅ 햄버거 버튼은 App에서만 렌더링 */}
        <button className="hamburger" onClick={toggleSidebar}>☰</button>

        {/* ✅ 사이드바는 열릴 때만 보여짐 */}
        <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/group" element={<Group />} />
          <Route path="/board" element={<Board />} />
          <Route path="/schedule" element={<Schedule />} />
          <Route path="/mypage" element={<Mypage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
