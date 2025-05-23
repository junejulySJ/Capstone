import React, { useState } from 'react';
import './Group.css';

const initialMembers = [
  { id: 1, name: '김지은', email: 'jieun@example.com', group: '헬스' },
  { id: 2, name: '이철수', email: 'chulsoo@example.com', group: 'UX팀' },
  { id: 3, name: '박영희', email: 'younghee@example.com', group: 'UI' },
  { id: 4, name: '오지훈', email: 'jihun@example.com', group: '헬스' },
];

const Group = () => {
  const [activeTab, setActiveTab] = useState('member');
  const [members, setMembers] = useState(initialMembers);
  const [newMember, setNewMember] = useState({ name: '', email: '', group: '' });
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [search, setSearch] = useState('');

  const filteredMembers = members.filter((m) =>
    m.name.toLowerCase().includes(search.toLowerCase()) ||
    m.email.toLowerCase().includes(search.toLowerCase())
  );

  const groupMembers = members.filter((m) => m.group === selectedGroup);
  const uniqueGroups = [...new Set(members.map((m) => m.group))];

  const handleAddMember = () => {
    if (!newMember.name || !newMember.email || !newMember.group) {
      alert('이름, 이메일, 그룹을 입력해주세요.');
      return;
    }
    const id = Math.max(...members.map((m) => m.id)) + 1;
    setMembers([...members, { ...newMember, id }]);
    setNewMember({ name: '', email: '', group: '' });
  };

  const handleDelete = (id) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      setMembers(members.filter((m) => m.id !== id));
    }
  };

  return (
    <div className="group-page">
      <div className="menu-offset" />

      <div className="group-tab-buttons">
        <button
          className={activeTab === 'member' ? 'active-tab' : ''}
          onClick={() => { setActiveTab('member'); setSelectedGroup(null); }}
        >
          Member
        </button>
        <button
          className={activeTab === 'group' ? 'active-tab' : ''}
          onClick={() => { setActiveTab('group'); setSelectedGroup(null); }}
        >
          Group
        </button>
      </div>

      <div className="group-main-content">
        {activeTab === 'member' && !selectedGroup && (
          <>
            <div className="search-add-wrapper">
              <input
                type="text"
                placeholder="이름이나 이메일 검색"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
              <div className="add-member-form">
                <input
                  type="text"
                  placeholder="이름"
                  value={newMember.name}
                  onChange={(e) => setNewMember({ ...newMember, name: e.target.value })}
                />
                <input
                  type="email"
                  placeholder="이메일"
                  value={newMember.email}
                  onChange={(e) => setNewMember({ ...newMember, email: e.target.value })}
                />
                <input
                  type="text"
                  placeholder="그룹"
                  value={newMember.group}
                  onChange={(e) => setNewMember({ ...newMember, group: e.target.value })}
                />
                <button onClick={handleAddMember}>멤버 추가</button>
              </div>
            </div>

            <table className="member-table">
              <thead>
                <tr>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>그룹</th>
                  <th>삭제</th>
                </tr>
              </thead>
              <tbody>
                {filteredMembers.map((m) => (
                  <tr key={m.id}>
                    <td>{m.name}</td>
                    <td>{m.email}</td>
                    <td>{m.group}</td>
                    <td>
                      <button onClick={() => handleDelete(m.id)}>삭제</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}

        {activeTab === 'group' && !selectedGroup && (
          <div className="group-list">
            <h3>그룹 목록</h3>
            <ul>
              {uniqueGroups.map((g, idx) => (
                <li key={idx} onClick={() => setSelectedGroup(g)} style={{ cursor: 'pointer' }}>
                  ✅ {g} 커뮤니티
                </li>
              ))}
            </ul>
          </div>
        )}

        {selectedGroup && (
          <div className="community-page">
            <h2>{selectedGroup} 커뮤니티</h2>
            <button onClick={() => setSelectedGroup(null)}>← 뒤로가기</button>

            <div className="community-section">
              <h3>공지사항</h3>
              <p>공지사항이 없습니다.</p>
            </div>

            <div className="community-section">
              <h3>소통 공간</h3>
              <p>그룹 멤버들과 자유롭게 이야기 나눠보세요!</p>
            </div>

            <div className="community-section">
              <h3>멤버 목록</h3>
              <ul>
                {groupMembers.map((m) => (
                  <li key={m.id}>{m.name}</li>
                ))}
              </ul>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Group;
