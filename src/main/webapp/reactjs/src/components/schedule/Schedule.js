import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAppContext } from "../../context/AppContext";
import { useNavigate } from "react-router-dom";
import styles from "./css/Schedule.module.css"; // ✅ CSS 모듈
import { API_BASE_URL } from "../../constants.js";

const { kakao } = window;

export default function Schedule() {
  const { user } = useAppContext();
  const [schedules, setSchedules] = useState([]);
  const [expandedScheduleNo, setExpandedScheduleNo] = useState(null);
  const [detailsMap, setDetailsMap] = useState({});
  const navigate = useNavigate();

  const [showForm, setShowForm] = useState(false);
  const [newSchedule, setNewSchedule] = useState({
    scheduleName: "",
    scheduleAbout: "",
    details: [],
  });

  // user가 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!user) {
      navigate("/login"); // 로그인 페이지로 이동
    }
  }, [user, navigate]);

  // 일정 수정
  const [isEdit, setIsEdit] = useState(false);
  const [editScheduleNo, setEditScheduleNo] = useState(null);

  // 지도 초기화
  useEffect(() => {
    if (window.kakao && window.kakao.maps) {
      const container = document.getElementById("map");
      const options = {
        center: new window.kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };
      new window.kakao.maps.Map(container, options);
    } else {
      // 아직 로드 안 됐으면 로드 될 때까지 기다리기
      const interval = setInterval(() => {
        if (window.kakao && window.kakao.maps) {
          const container = document.getElementById("map");
          const options = {
            center: new window.kakao.maps.LatLng(33.450701, 126.570667),
            level: 3,
          };
          new window.kakao.maps.Map(container, options);
          clearInterval(interval);
        }
      }, 100); // 0.1초마다 확인
    }
  }, []);

  // 일정 목록 조회
  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/schedules`)
      .then((res) => setSchedules(res.data))
      .catch((err) => console.error("일정 불러오기 실패:", err));
  }, []);

  // 상세일정 토글
  const toggleDetails = (scheduleNo) => {
    if (expandedScheduleNo === scheduleNo) {
      setExpandedScheduleNo(null);
    } else {
      if (detailsMap[scheduleNo]) {
        setExpandedScheduleNo(scheduleNo);
        return;
      }
      axios
        .get(`${API_BASE_URL}/schedules/${scheduleNo}/details`)
        .then((res) => {
          setDetailsMap((prev) => ({ ...prev, [scheduleNo]: res.data }));
          setExpandedScheduleNo(scheduleNo);
        })
        .catch((err) => console.error("상세 일정 불러오기 실패:", err));
    }
  };

  // 상세일정 추가
  const addDetail = () => {
    setNewSchedule((prev) => ({
      ...prev,
      details: [
        ...prev.details,
        {
          scheduleContent: "",
          scheduleAddress: "",
          latitude: "",
          longitude: "",
          scheduleStartTime: "",
          scheduleEndTime: "",
        },
      ],
    }));
  };

  // 상세일정 제거
  const removeDetail = (index) => {
    const updated = [...newSchedule.details];
    updated.splice(index, 1);
    setNewSchedule({ ...newSchedule, details: updated });
  };

  // 일정 제목/소개 변경
  const handleChange = (e) => {
    setNewSchedule({ ...newSchedule, [e.target.name]: e.target.value });
  };

  // 상세일정 필드 변경
  const handleDetailChange = (index, e) => {
    const updatedDetails = [...newSchedule.details];
    updatedDetails[index][e.target.name] = e.target.value;
    setNewSchedule({ ...newSchedule, details: updatedDetails });
  };

  // 일정 등록
  const handleSubmit = (e) => {
    e.preventDefault();

    const payload = {
      userId: user.userId,
      scheduleName: newSchedule.scheduleName,
      scheduleAbout: newSchedule.scheduleAbout,
      details: newSchedule.details,
    };

    const request = isEdit
      ? axios.put(`${API_BASE_URL}/schedules/${editScheduleNo}`, payload)
      : axios.post(`${API_BASE_URL}/schedules`, payload);

    request
      .then(() => {
        alert(isEdit ? "일정 수정 완료!" : "일정 등록 완료!");
        setShowForm(false);
        setIsEdit(false);
        setEditScheduleNo(null);
        setNewSchedule({ scheduleName: "", scheduleAbout: "", details: [] });

        // ✅ 수정 모드일 때만 상세 일정도 다시 불러오기
        return Promise.all([
          axios.get(`${API_BASE_URL}/schedules`), // 전체 일정
          isEdit
            ? axios.get(`${API_BASE_URL}/schedules/${editScheduleNo}/details`) // 상세 일정
            : Promise.resolve(null),
        ]);
      })
      .then(([scheduleRes, detailsRes]) => {
        setSchedules(scheduleRes.data);
        if (detailsRes) {
          setDetailsMap((prev) => ({
            ...prev,
            [editScheduleNo]: detailsRes.data,
          }));
        }
      })
      .catch((err) => {
        console.error("저장 실패:", err);
        alert("저장 중 오류 발생");
      });
  };

  // 일정 삭제 기능
  const handleDelete = (scheduleNo) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    axios
      .delete(`${API_BASE_URL}/schedules/${scheduleNo}`)
      .then(() => {
        alert("삭제 완료");
        setSchedules(schedules.filter((s) => s.scheduleNo !== scheduleNo));
        setExpandedScheduleNo(null); // 혹시 열려 있었으면 닫기
      })
      .catch((err) => {
        console.error("삭제 실패:", err);
        alert("삭제 중 오류 발생");
      });
  };

  // 일정 수정 기능
  const handleEdit = (schedule) => {
    setIsEdit(true);
    setEditScheduleNo(schedule.scheduleNo);

    axios
      .get(`${API_BASE_URL}/schedules/${schedule.scheduleNo}/details`)
      .then((res) => {
        setNewSchedule({
          scheduleName: schedule.scheduleName,
          scheduleAbout: schedule.scheduleAbout,
          details: res.data,
        });
        setShowForm(true); // 폼 열기
      });
  };

  return (
    <div className={styles.schedule_container}>
      <h1>지도</h1>
      <div id="map" className={styles.schedule_map}></div>

      <div className={styles.schedule_nav}>
        <h2>전체 일정</h2>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? "입력 취소" : "일정 생성하기"}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className={styles.schedule_form}>
          <div>
            <label>일정 제목: </label>
            <input
              type="text"
              name="scheduleName"
              value={newSchedule.scheduleName}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label>일정 소개: </label>
            <textarea
              name="scheduleAbout"
              value={newSchedule.scheduleAbout}
              onChange={handleChange}
            />
          </div>

          <h4>상세 일정</h4>
          {newSchedule.details.map((detail, index) => (
            <div key={index} className={styles.schedule_detail_item}>
              <input
                type="text"
                name="scheduleContent"
                placeholder="내용"
                value={detail.scheduleContent}
                onChange={(e) => handleDetailChange(index, e)}
                required
              />
              <input
                type="text"
                name="scheduleAddress"
                placeholder="주소"
                value={detail.scheduleAddress}
                onChange={(e) => handleDetailChange(index, e)}
              />
              <input
                type="number"
                step="any"
                name="latitude"
                placeholder="위도"
                value={detail.latitude}
                onChange={(e) => handleDetailChange(index, e)}
              />
              <input
                type="number"
                step="any"
                name="longitude"
                placeholder="경도"
                value={detail.longitude}
                onChange={(e) => handleDetailChange(index, e)}
              />
              <input
                type="datetime-local"
                name="scheduleStartTime"
                value={detail.scheduleStartTime}
                onChange={(e) => handleDetailChange(index, e)}
                required
              />
              <input
                type="datetime-local"
                name="scheduleEndTime"
                value={detail.scheduleEndTime}
                onChange={(e) => handleDetailChange(index, e)}
                required
              />
              <button type="button" onClick={() => removeDetail(index)}>
                -
              </button>
            </div>
          ))}
          <button type="button" onClick={addDetail}>
            + 상세 일정 추가
          </button>
          <br />
          <br />
          <button type="submit">{isEdit ? "일정 수정" : "일정 등록"}</button>
        </form>
      )}

      <ul className={styles.schedule_list}>
        {schedules.map((schedule) => (
          <>
            <li key={schedule.scheduleNo} className={styles.schedule_card}>
              <strong>{schedule.scheduleName}</strong>
              <br />
              <span>{schedule.scheduleAbout}</span>
              <br />
              <small>
                작성자: {schedule.userId} | 생성일:{" "}
                {schedule.scheduleCreatedDate}
              </small>
              <br />
              <button onClick={() => toggleDetails(schedule.scheduleNo)}>
                {expandedScheduleNo === schedule.scheduleNo
                  ? "상세 일정 닫기"
                  : "상세 일정 보기"}
              </button>
              <div className={styles.schedule_actions}>
                <button onClick={() => handleEdit(schedule)}>수정</button>
                {user?.userId === schedule.userId && (
                  <button onClick={() => handleDelete(schedule.scheduleNo)}>
                    삭제
                  </button>
                )}
              </div>

              {expandedScheduleNo === schedule.scheduleNo &&
                detailsMap[schedule.scheduleNo] && (
                  <ul style={{ marginTop: "0.5rem", paddingLeft: "1rem" }}>
                    {detailsMap[schedule.scheduleNo].map((detail) => (
                      <li key={detail.scheduleDetailNo}>
                        🕒 {detail.scheduleStartTime} ~ {detail.scheduleEndTime}
                        <br />
                        📍 {detail.scheduleContent} ({detail.scheduleAddress})
                      </li>
                    ))}
                  </ul>
                )}
            </li>
          </>
        ))}
      </ul>
    </div>
  );
}
