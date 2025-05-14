// dummyPosts.js

const dummyPosts = [
  // 맛집
  ...Array.from({ length: 20 }, (_, i) => ({
    id: `food-${i + 1}`,
    category: "맛집",
    title: `🍽️ 맛집 추천 ${i + 1}`,
    description: `이 집은 꼭 가야 합니다! (${i + 1})`,
    writer: `맛객${i + 1}`,
    time: `${i + 1}시간 전`,
    content: `정말 맛있어요. 분위기도 좋고 재방문의사 100%! (${i + 1})`,
    image: `food${(i % 5) + 1}.jpg`, // 예: food1.jpg ~ food5.jpg
  })),

  // 카페
  ...Array.from({ length: 20 }, (_, i) => ({
    id: `cafe-${i + 1}`,
    category: "카페",
    title: `☕ 분위기 좋은 카페 ${i + 1}`,
    description: `커피 맛이 정말 끝내줘요! (${i + 1})`,
    writer: `카페러버${i + 1}`,
    time: `${i + 1}시간 전`,
    content: `분위기 최고! 디저트도 맛있고 사진 찍기 좋은 카페입니다. (${i + 1})`,
    image: `cafe${(i % 5) + 1}.jpg`,
  })),

  // 놀거리
  ...Array.from({ length: 20 }, (_, i) => ({
    id: `play-${i + 1}`,
    category: "놀거리",
    title: `🎡 핫한 놀거리 추천 ${i + 1}`,
    description: `주말에 가볼만한 놀거리 모음 (${i + 1})`,
    writer: `놀이터${i + 1}`,
    time: `${i + 1}시간 전`,
    content: `요즘 대세 놀거리 총집합! 재미있고 스트레스 풀려요. (${i + 1})`,
    image: `play${(i % 5) + 1}.jpg`,
  })),

  // 일정/코스
  ...Array.from({ length: 20 }, (_, i) => ({
    id: `course-${i + 1}`,
    category: "일정/코스",
    title: `📍 여행 일정 추천 ${i + 1}`,
    description: `완벽한 일정으로 여행 즐기기 (${i + 1})`,
    writer: `여행자${i + 1}`,
    time: `${i + 1}시간 전`,
    content: `이 코스대로만 따라가면 실패 없는 여행! 참고해보세요. (${i + 1})`,
    image: `course${(i % 5) + 1}.jpg`,
  })),
];

export default dummyPosts;
