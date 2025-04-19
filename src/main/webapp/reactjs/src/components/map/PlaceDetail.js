import React, { useEffect, useState } from "react";
import axios from "axios";
import { useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../constants.js";
import styles from "./css/PlaceDetail.module.css";

export default function PlaceDetail() {
  const [contentId, setContentId] = useState("");
  const [place, setPlace] = useState("");
  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const contentId = searchParams.get("contentId") || "";

    setContentId(contentId);
  }, [location.search]);

  useEffect(() => {
    if (!contentId) return;

    axios
      .get(`${API_BASE_URL}/map/detail?contentId=${contentId}`)
      .then((res) => setPlace(res.data))
      .catch((err) => console.error("장소 정보 불러오기 실패:", err));
  }, [contentId]);

  return (
    <div>
      <h1>상세 정보</h1>
      <table className={styles.place}>
        <tr>
          <th>이미지</th>
          <th>우편번호</th>
          <th>장소명</th>
          <th>전화번호</th>
          <th>주소</th>
        </tr>
        <tr>
          <td>
            <img src={place.firstimage} />
          </td>
          <td>{place.zipcode}</td>
          <td>{place.title}</td>
          <td>{place.tel}</td>
          <td>{place.addr}</td>
        </tr>
        <tr>
          <th colSpan={5}>설명</th>
        </tr>
        <tr>
          <td colSpan={5}>{place.overview}</td>
        </tr>
      </table>
    </div>
  );
}
