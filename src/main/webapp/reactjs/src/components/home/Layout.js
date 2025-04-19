import { Outlet, Link, useNavigate } from "react-router-dom";
import styles from "./css/Layout.module.css";
import { useAppContext } from "../../context/AppContext";
import axios from "axios";
import { API_BASE_URL } from "../../constants.js";

export default function Layout() {
  const { user, setUser } = useAppContext();
  const navigate = useNavigate();

  const handleLogout = () => {
    axios
      .post(`${API_BASE_URL}/logout`, {}, { withCredentials: true })
      .then(() => {
        setUser(null);
        navigate("/login"); //로그인으로 리다이렉트
      })
      .catch((error) => console.error("로그아웃 실패", error));
  };

  return (
    <div className={styles.layout_container}>
      <div>
        <nav className={styles.layout_nav}>
          <Link to="/">Home</Link>
          <Link to="/board">Board</Link>
          <Link to="/friend">Friend</Link>
          <Link to="/schedule">Schedule</Link>
          {user ? (
            <>
              <span>{user.userNick}님 환영합니다</span>
              <button onClick={handleLogout}>로그아웃</button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </nav>
        <Outlet /> {/* 여기에 자식 컴포넌트가 렌더링됨 */}
      </div>

      <footer className={styles.layout_footer}>
        <p>© 2025 My App</p>
      </footer>
    </div>
  );
}
