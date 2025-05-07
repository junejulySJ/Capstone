// src/pages/Group.jsx
import React, { useState } from 'react';
import './Group.css';

export default function Group() {
  const [friends, setFriends] = useState(['홍길동', '김철수', '이영희']);
  const [friendRequests, setFriendRequests] = useState(['박이삼', '김가나']);
  const [groups, setGroups] = useState(['A 그룹', 'B 그룹', 'C 그룹']);
  const [groupSchedules, setGroupSchedules] = useState({
    'A 그룹': ['5/20 강남역 모임', '5/25 카페 투어'],
    'B 그룹': ['5/22 영화관 약속'],
    'C 그룹': []
  });
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [searchInput, setSearchInput] = useState('');

  const handleDeleteFriend = (name) => {
    setFriends(prev => prev.filter(friend => friend !== name));
  };

  const handleAcceptRequest = (name) => {
    setFriends(prev => [...prev, name]);
    setFriendRequests(prev => prev.filter(req => req !== name));
  };

  const handleRejectRequest = (name) => {
    setFriendRequests(prev => prev.filter(req => req !== name));
  };

  const handleAddGroup = () => {
    const newGroup = prompt('새 그룹 이름을 입력하세요:');
    if (newGroup) {
      setGroups(prev => [...prev, newGroup]);
      setGroupSchedules(prev => ({ ...prev, [newGroup]: [] }));
    }
  };

  const handleAddSchedule = () => {
    const newSchedule = prompt('추가할 일정을 입력하세요:');
    if (newSchedule && selectedGroup) {
      setGroupSchedules(prev => ({
        ...prev,
        [selectedGroup]: [...prev[selectedGroup], newSchedule]
      }));
    }
  };

  const handleDeleteSchedule = (idx) => {
    if (selectedGroup) {
      setGroupSchedules(prev => ({
        ...prev,
        [selectedGroup]: prev[selectedGroup].filter((_, i) => i !== idx)
      }));
    }
  };

  const handleEditSchedule = (idx) => {
    const newText = prompt('수정할 일정을 입력하세요:');
    if (newText && selectedGroup) {
      setGroupSchedules(prev => ({
        ...prev,
        [selectedGroup]: prev[selectedGroup].map((item, i) => i === idx ? newText : item)
      }));
    }
  };

  const handleSearchFriend = () => {
    if (!searchInput.trim()) return;
    if (friends.includes(searchInput)) {
      alert(`친구 목록에 존재합니다: ${searchInput}`);
    } else {
      if (window.confirm(`${searchInput}님이 친구 목록에 없습니다. 추가하시겠습니까?`)) {
        setFriends(prev => [...prev, searchInput]);
      }
    }
    setSearchInput('');
  };

  return (
    <div className="group-page">
      <h1 className="group-title">Group</h1>
      <div className="group-layout">
        {/* 왼쪽 친구 관리 */}
        <div className="friend-section">
          <div className="section-block">
            <h3># 친구 목록</h3>
            {friends.map(friend => (
              <div key={friend} className="list-item">
                {friend}
                <div className="button-group">
                  <button className="small-btn delete" onClick={() => handleDeleteFriend(friend)}>삭제</button>
                </div>
              </div>
            ))}
          </div>

          <div className="section-block">
            <h3># 친구 초대 및 추가</h3>
            <input
              type="text"
              placeholder="아이디를 검색하세요."
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              className="search-input"
            />
            <button className="small-btn search" onClick={handleSearchFriend}>검색</button>
          </div>

          <div className="section-block">
            <h3># 친구 요청 목록</h3>
            {friendRequests.map(request => (
              <div key={request} className="list-item">
                {request}
                <div className="button-group">
                  <button className="small-btn accept" onClick={() => handleAcceptRequest(request)}>수락</button>
                  <button className="small-btn reject" onClick={() => handleRejectRequest(request)}>거절</button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 오른쪽 그룹 관리 */}
        <div className="group-section">
          <div className="section-block">
            <h3># 그룹 목록</h3>
            {groups.map(group => (
              <div key={group} className="list-item" onClick={() => setSelectedGroup(group)} style={{ cursor: 'pointer' }}>
                {group}
              </div>
            ))}
            <button className="big-btn" onClick={handleAddGroup}>➕ 그룹 추가</button>
          </div>

          {/* 그룹 일정 표시 */}
          {selectedGroup && (
            <div className="section-block">
              <h3># {selectedGroup} 일정 및 약속</h3>
              {groupSchedules[selectedGroup].length > 0 ? (
                groupSchedules[selectedGroup].map((schedule, idx) => (
                  <div key={idx} className="list-item">
                    {schedule}
                    <div>
                      <button className="small-btn edit" onClick={() => handleEditSchedule(idx)}>✏ 수정</button>
                      <button className="small-btn delete" onClick={() => handleDeleteSchedule(idx)}>🗑 삭제</button>
                    </div>
                  </div>
                ))
              ) : (
                <p>등록된 일정이 없습니다.</p>
              )}
              <button className="big-btn" onClick={handleAddSchedule}>➕ 일정 추가</button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}