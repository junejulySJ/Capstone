import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { dummyUsers } from "../data/dummyUsers";
import "./Register.css";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    userId: "",
    password: "",
    confirmPassword: "",
    nickname: "",
    email: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }
    const exists = dummyUsers.some((u) => u.userId === form.userId);
    if (exists) {
      alert("이미 존재하는 아이디입니다.");
      return;
    }
    dummyUsers.push(form);
    alert("회원가입이 완료되었습니다.");
    navigate("/login");
  };

  return (
    <div className="register-container">
      <h2>회원가입</h2>
      <form onSubmit={handleSubmit} className="register-form">
        <label>이름:</label>
        <input name="name" value={form.name} onChange={handleChange} required />

        <label>아이디:</label>
        <input name="userId" value={form.userId} onChange={handleChange} required />

        <label>비밀번호:</label>
        <input type="password" name="password" value={form.password} onChange={handleChange} required />

        <label>비밀번호 확인:</label>
        <input type="password" name="confirmPassword" value={form.confirmPassword} onChange={handleChange} required />

        <label>닉네임:</label>
        <input name="nickname" value={form.nickname} onChange={handleChange} required />

        <label>이메일:</label>
        <input type="email" name="email" value={form.email} onChange={handleChange} required />

        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}