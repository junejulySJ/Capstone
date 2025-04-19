import { useIdleTimer } from 'react-idle-timer';
import { useAppContext } from '../../context/AppContext';

export default function AutoLogout() {
  const { user, setUser } = useAppContext();

  const handleIdle = () => {
    if (user) {
      alert('⏰ 활동이 없어 자동 로그아웃되었습니다.');
      localStorage.removeItem('user');
      setUser(null);
    }
  };

  useIdleTimer({
    timeout: 30 * 60 * 1000, // 30분 (원하면 시간 줄여서 테스트해도 됨)
    onIdle: handleIdle,
    debounce: 500
  });

  return null; // 이 컴포넌트는 화면에 아무 것도 출력 안 함
}
