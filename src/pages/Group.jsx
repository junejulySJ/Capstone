// src/pages/Group.jsx
import React, { useState } from 'react';
import './Group.css';

const mockGroups = [
  {
    id: 1,
    name: '캡스톤 팀플 모임',
    description: '매주 화요일 회의하는 우리 조 모임입니다.',
    latestPost: '다음 회의 안건 정리했어요!'
  },
  {
    id: 2,
    name: '헬스장 친구들',
    description: '운동과 건강 정보 공유해요!',
    latestPost: '이번 주 루틴 공유해요!'
  }
];

export default function Group() {
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [groups, setGroups] = useState(mockGroups);
  const [newGroup, setNewGroup] = useState({ name: '', description: '' });
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [friendName, setFriendName] = useState('');
  const [friends, setFriends] = useState(['홍길동', '김철수']);
  const [selectedFriend, setSelectedFriend] = useState(null);
  const [chatMessages, setChatMessages] = useState({});
  const [chatInput, setChatInput] = useState('');

  // 그룹용 추가 상태들
  const [groupPosts, setGroupPosts] = useState({});
  const [groupPostInput, setGroupPostInput] = useState('');
  const [groupChats, setGroupChats] = useState({});
  const [groupChatInput, setGroupChatInput] = useState('');
  const [groupSchedules, setGroupSchedules] = useState({});
  const [groupMembers, setGroupMembers] = useState({});
  const [scheduleInput, setScheduleInput] = useState('');

  const handleCreateGroup = (e) => {
    e.preventDefault();
    const nextId = groups.length + 1;
    setGroups([...groups, {
      id: nextId,
      name: newGroup.name,
      description: newGroup.description,
      latestPost: '그룹이 생성되었습니다.'
    }]);
    setGroupMembers(prev => ({
      ...prev,
      [nextId]: ['나']
    }));
    setNewGroup({ name: '', description: '' });
    setShowCreateForm(false);
  };

  const handleAddFriend = () => {
    if (friendName.trim()) {
      setFriends([...friends, friendName]);
      setFriendName('');
    }
  };

  const handleSendMessage = (e) => {
    e.preventDefault();
    if (chatInput.trim() && selectedFriend) {
      setChatMessages(prev => ({
        ...prev,
        [selectedFriend]: [...(prev[selectedFriend] || []), { sender: '나', message: chatInput }]
      }));
      setChatInput('');
    }
  };

  const handlePostSubmit = (e) => {
    e.preventDefault();
    if (groupPostInput.trim() && selectedGroup) {
      setGroupPosts(prev => ({
        ...prev,
        [selectedGroup.id]: [...(prev[selectedGroup.id] || []), groupPostInput]
      }));
      setGroupPostInput('');
    }
  };

  const handleGroupChatSubmit = (e) => {
    e.preventDefault();
    if (groupChatInput.trim() && selectedGroup) {
      setGroupChats(prev => ({
        ...prev,
        [selectedGroup.id]: [...(prev[selectedGroup.id] || []), { sender: '나', message: groupChatInput }]
      }));
      setGroupChatInput('');
    }
  };

  const handleScheduleSubmit = (e) => {
    e.preventDefault();
    if (scheduleInput.trim() && selectedGroup) {
      setGroupSchedules(prev => ({
        ...prev,
        [selectedGroup.id]: [...(prev[selectedGroup.id] || []), scheduleInput]
      }));
      setScheduleInput('');
    }
  };

  return (
    <div className="group-page">
      <h2>그룹</h2>

      {!selectedGroup && !selectedFriend && (
        <>
          <div className="action-buttons">
            <button className="quick-btn" onClick={() => setShowCreateForm(!showCreateForm)}>
              {showCreateForm ? '← 닫기' : '➕ 그룹 만들기'}
            </button>
            <div className="friend-form">
              <input
                type="text"
                placeholder="친구 이름"
                value={friendName}
                onChange={(e) => setFriendName(e.target.value)}
              />
              <button onClick={handleAddFriend}>➕ 친구 추가</button>
            </div>
          </div>

          {showCreateForm && (
            <form onSubmit={handleCreateGroup} className="create-form">
              <input
                type="text"
                placeholder="그룹 이름"
                value={newGroup.name}
                onChange={(e) => setNewGroup({ ...newGroup, name: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="그룹 설명"
                value={newGroup.description}
                onChange={(e) => setNewGroup({ ...newGroup, description: e.target.value })}
                required
              />
              <button type="submit">✅ 생성</button>
            </form>
          )}

          <div className="group-list">
            {groups.map(group => (
              <div
                key={group.id}
                className="group-card"
                onClick={() => setSelectedGroup(group)}
              >
                <h3>{group.name}</h3>
                <p>{group.description}</p>
                <span className="latest-post">최근 글: {group.latestPost}</span>
              </div>
            ))}
          </div>

          <div className="friend-list">
            <h3>내 친구</h3>
            <ul>
              {friends.map((name, index) => (
                <li key={index} onClick={() => setSelectedFriend(name)} style={{ cursor: 'pointer' }}>
                  {name}
                </li>
              ))}
            </ul>
          </div>
        </>
      )}

      {selectedGroup && (
        <div className="group-detail">
          <button className="back-btn" onClick={() => setSelectedGroup(null)}>← 그룹 목록으로</button>
          <h3>{selectedGroup.name}</h3>
          <p>{selectedGroup.description}</p>
          <p className="latest-post">최근 글: {selectedGroup.latestPost}</p>

          <div className="group-section">
            <h4>📋 그룹 게시판</h4>
            <form onSubmit={handlePostSubmit} className="group-form">
              <input
                type="text"
                placeholder="게시글 작성"
                value={groupPostInput}
                onChange={(e) => setGroupPostInput(e.target.value)}
              />
              <button type="submit">게시</button>
            </form>
            <ul>
              {(groupPosts[selectedGroup.id] || []).map((post, index) => (
                <li key={index}>{post}</li>
              ))}
            </ul>
          </div>

          <div className="group-section">
            <h4>💬 그룹 채팅</h4>
            <form onSubmit={handleGroupChatSubmit} className="group-form">
              <input
                type="text"
                placeholder="채팅 메시지 입력"
                value={groupChatInput}
                onChange={(e) => setGroupChatInput(e.target.value)}
              />
              <button type="submit">전송</button>
            </form>
            <div className="chat-box">
              {(groupChats[selectedGroup.id] || []).map((msg, index) => (
                <div key={index} className="chat-message">
                  <strong>{msg.sender}: </strong>
                  <span>{msg.message}</span>
                </div>
              ))}
            </div>
          </div>

          <div className="group-section">
            <h4>📅 그룹 일정</h4>
            <form onSubmit={handleScheduleSubmit} className="group-form">
              <input
                type="text"
                placeholder="일정 추가"
                value={scheduleInput}
                onChange={(e) => setScheduleInput(e.target.value)}
              />
              <button type="submit">추가</button>
            </form>
            <ul>
              {(groupSchedules[selectedGroup.id] || []).map((schedule, index) => (
                <li key={index}>{schedule}</li>
              ))}
            </ul>
          </div>

          <div className="group-section">
  <h4>👥 그룹 멤버</h4>
  <ul>
    {(groupMembers[selectedGroup.id] || []).map((member, index) => (
      <li key={index}>{member}</li>
    ))}
  </ul>
</div>

        </div>
      )}

      {selectedFriend && (
        <div className="chat-window">
          <button className="back-btn" onClick={() => setSelectedFriend(null)}>← 나가기</button>
          <h3>{selectedFriend} 님과의 채팅</h3>
          <div className="chat-box">
            {(chatMessages[selectedFriend] || []).map((msg, index) => (
              <div key={index} className="chat-message">
                <strong>{msg.sender}: </strong>
                <span>{msg.message}</span>
              </div>
            ))}
          </div>
          <form onSubmit={handleSendMessage} className="chat-input">
            <input
              type="text"
              value={chatInput}
              onChange={(e) => setChatInput(e.target.value)}
              placeholder="메시지를 입력하세요"
            />
            <button type="submit">전송</button>
          </form>
        </div>
      )}
    </div>
  );
}
