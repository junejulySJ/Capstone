import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { API_BASE_URL } from "../../constants.js";

export default function Home() {
  const [areaCodes, setAreaCodes] = useState([]);
  const [sigunguCodes, setSigunguCodes] = useState([]);
  const [area, setArea] = useState("1");
  const [sigungu, setSigungu] = useState("1");
  const [contentType, setContentType] = useState("0");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/map/region`)
      .then((res) => setAreaCodes(res.data))
      .catch((err) => console.error("지역코드 불러오기 실패:", err));
  }, []);

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/map/region?code=${area}`)
      .then((res) => setSigunguCodes(res.data))
      .catch((err) => console.error("시군구코드 불러오기 실패:", err));
  }, [area]);

  const handleSearch = (e) => {
    e.preventDefault();

    navigate(
      `/map?areaCode=${area}&sigunguCode=${sigungu}&contentTypeId=${contentType}`
    );
  };

  return (
    <div>
      <h1>Meeting Map</h1>
      <form onSubmit={handleSearch}>
        <table>
          <tr>
            <td>
              <label htmlFor="area">지역명</label>
            </td>
            <td>
              <label htmlFor="sigungu">시군구명</label>
            </td>
            <td>
              <label htmlFor="contentType">카테고리</label>
            </td>
          </tr>
          <tr>
            <td>
              <select
                id="area"
                value={area}
                onChange={(e) => setArea(e.target.value)}
              >
                {areaCodes.map((areaCode) => (
                  <option value={areaCode.code}>{areaCode.name}</option>
                ))}
              </select>
            </td>
            <td>
              <select
                id="sigungu"
                value={sigungu}
                onChange={(e) => setSigungu(e.target.value)}
              >
                {sigunguCodes.map((sigunguCode) => (
                  <option value={sigunguCode.code}>{sigunguCode.name}</option>
                ))}
              </select>
            </td>
            <td>
              <select
                id="contentType"
                value={contentType}
                onChange={(e) => setContentType(e.target.value)}
              >
                <option value="0">전체</option>
                <option value="12">관광지</option>
                <option value="14">문화시설</option>
                <option value="15">행사/공연/축제</option>
                <option value="25">여행코스</option>
                <option value="28">레포츠</option>
                <option value="32">숙박</option>
                <option value="38">쇼핑</option>
                <option value="39">음식점</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>
              <button type="submit">Go!</button>
            </td>
          </tr>
        </table>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
