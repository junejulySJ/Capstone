import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/home/Home";
import Board from "./components/board/Board";
import BoardDetail from "./components/board/BoardDetail";
import Friend from "./components/friend/Friend";
import Schedule from "./components/schedule/Schedule";
import Login from "./components/user/Login";
import Layout from "./components/home/Layout";
import { AppProvider } from "./context/AppContext";
import BoardCreate from "./components/board/BoardCreate";
import Register from "./components/user/Register";
import AddFriend from "./components/friend/AddFriend";
import SentFriend from "./components/friend/SentFriend";
import ReceivedFriend from "./components/friend/ReceivedFriend";
import Map from "./components/map/Map";
import PlaceDetail from "./components/map/PlaceDetail";
// 자동 로그아웃
// import AutoLogout from './components/common/AutoLogout';

function App() {
  return (
    <AppProvider>
      {" "}
      {/* 전역 상태 감싸줌 */}
      {/* 사용자 활동 감지용, 사용자 활동 기반 자동 로그아웃 */}
      {/* <AutoLogout />  */}
      <Router>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />
            <Route path="board" element={<Board />} />
            <Route path="board/:bno" element={<BoardDetail />} />
            <Route path="board/create" element={<BoardCreate />} />
            <Route path="friend" element={<Friend />} />
            <Route path="friend/add" element={<AddFriend />} />
            <Route path="friend/sent" element={<SentFriend />} />
            <Route path="friend/received" element={<ReceivedFriend />} />
            <Route path="schedule" element={<Schedule />} />
            <Route path="map" element={<Map />} />
            <Route path="map/detail" element={<PlaceDetail />} />
            <Route path="login" element={<Login />} />
            <Route path="register" element={<Register />} />
          </Route>
        </Routes>
      </Router>
    </AppProvider>
  );
}

export default App;
