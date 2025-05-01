// src/data/initialPosts.js

// 더미 게시글 데이터 (30개)
const initialPosts = [
    {
      id: 1,
      author: '김지은',
      title: '홍대 파스타 전문점 방문기',
      content: `이번 주말에 홍대에 새로 오픈한 파스타 전문점을 다녀왔어요. 
  가게 인테리어가 굉장히 아늑하고, 오픈 키친이라 요리 과정을 직접 볼 수 있어서 좋았어요. 
  특히 해산물 토마토 파스타는 신선한 조개와 새우가 듬뿍 들어있었고, 소스 맛이 깊었어요. 
  가격대는 1만 5천원 정도였고, 웨이팅이 살짝 있었지만 그만한 가치가 있었습니다. 
  식사 후에는 바로 옆에 있는 디저트 카페도 들렀는데, 티라미수가 부드러워서 감동했어요.`,
      createdAt: '2025-04-01T13:00:00Z',
      images: ['https://via.placeholder.com/300'],
      comments: [
        { id: 1, author: '사용자A', text: '저도 여기 가봤어요! 진짜 소스가 맛있더라구요.', createdAt: '2025-04-01T15:00:00Z' }
      ]
    },
    {
      id: 2,
      author: '이민호',
      title: '강남 신상 카페 투어 후기',
      content: `최근 강남역 부근에 새로 생긴 '브루클린 커피'에 다녀왔어요. 
  화이트톤 인테리어에 통창으로 햇살이 쏟아져서 사진 찍기 정말 좋았어요. 
  아메리카노는 산미가 살짝 도는 원두를 사용해서 깔끔한 맛이었고, 
  브런치 메뉴로 나온 에그 베네딕트도 기대 이상이었습니다. 
  주말에는 사람이 많아서 살짝 웨이팅이 있지만 평일 낮에는 조용하다고 하네요.`,
      createdAt: '2025-04-03T10:30:00Z',
      images: ['https://via.placeholder.com/300'],
      comments: []
    },
    {
      id: 3,
      author: '박소영',
      title: '제주 협재 해변에서의 힐링 타임',
      content: `제주도 협재 해수욕장은 정말 기대 이상이었어요. 
  맑고 투명한 에메랄드 빛 바다를 보는 순간, 스트레스가 싹 풀렸습니다. 
  특히 일몰 시간이 다가오면서 하늘과 바다가 붉게 물드는 모습은 정말 감동적이었어요. 
  주변에 작은 카페들도 많아서 바다를 바라보며 커피를 마실 수도 있었고, 
  바로 옆에서 승마 체험도 할 수 있어 가족 단위 여행에도 좋을 것 같아요.`,
      createdAt: '2025-04-04T18:00:00Z',
      images: ['https://via.placeholder.com/300'],
      comments: [
        { id: 1, author: '익명', text: '사진만 봐도 힐링되네요. 저도 가보고 싶어요!', createdAt: '2025-04-04T20:00:00Z' }
      ]
    },
    {
      id: 4,
      author: '최준영',
      title: '홍대 스트리트 푸드 탐방기',
      content: `홍대 거리 음식 투어를 하고 왔습니다! 
  먼저 길거리에서 판매하는 '치즈 폭탄 핫도그'를 먹었는데, 
  겉은 바삭하고 안은 치즈가 쭉 늘어나서 사진 찍기 딱 좋았어요. 
  그 다음은 즉석 떡볶이, 매콤 달콤하면서 쫄깃한 떡이 정말 중독성이 있었어요. 
  마지막으로 디저트로는 수제 아이스크림을 먹었는데, 진짜 맛있었습니다. 
  주말이라 사람이 많긴 했지만, 골목골목 돌면서 먹거리 탐방하는 재미가 쏠쏠했어요.`,
      createdAt: '2025-04-05T12:20:00Z',
      images: [],
      comments: []
    },
    {
      id: 5,
      author: '한지민',
      title: '여의도 봄꽃축제 산책 후기',
      content: `여의도 봄꽃축제에 다녀왔어요! 
  벚꽃이 만개한 여의도 윤중로를 걷는 기분은 정말 환상적이었습니다. 
  날씨도 따뜻하고 미세먼지도 적어서 오랜만에 마스크 벗고 산책할 수 있었어요. 
  곳곳에 포토존도 마련되어 있어서 친구들이랑 인생샷도 찍었답니다. 
  다만 주말 오후에는 사람이 너무 많아서 사진 찍을 때 살짝 힘들 수 있어요. 
  가능하면 평일 오전 시간대를 추천합니다!`,
      createdAt: '2025-04-06T09:00:00Z',
      images: ['https://via.placeholder.com/300'],
      comments: []
    },
    {
      id: 6,
      author: '김민재',
      title: '이태원 맛집: 글로벌 퓨전 레스토랑 리뷰',
      content: `이태원에 위치한 글로벌 퓨전 레스토랑에 다녀왔습니다. 
  여기는 유럽식 요리와 아시아식 요리를 결합한 독특한 메뉴가 많아요. 
  특히 크림 파스타와 불고기 타코 조합이 정말 인상적이었습니다. 
  가격대는 약간 높은 편이지만, 양도 푸짐하고 분위기도 좋아서 특별한 날 가기 딱이에요. 
  와인 리스트도 다양해서, 커플 데이트 코스로도 강추합니다!`,
      createdAt: '2025-04-07T20:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 7,
      author: '송하늘',
      title: '부산 광안리 해변 야경 후기',
      content: `부산 광안리 해변은 낮에도 예쁘지만, 밤이 더 아름다웠어요. 
  광안대교에 불이 켜지면서 바다와 함께 환상적인 야경을 만들어냅니다. 
  해변을 따라 산책로가 잘 조성되어 있어서, 야경을 감상하며 산책하기 정말 좋아요. 
  주변 카페에서 테이크아웃 커피를 사서 바닷가에 앉아 바라본 광경은 정말 영화 같았어요. 
  부산에 간다면 무조건 밤에도 광안리를 들러야 합니다!`,
      createdAt: '2025-04-08T19:30:00Z',
      images: ['https://www.google.com/url?sa=i&url=http%3A%2F%2Fthekorea.kr%2Fbbs%2Fboard.php%3Fbo_table%3Dnews%26wr_id%3D133106&psig=AOvVaw2C2QGjKtQPDwWGwhbocRav&ust=1745739193400000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJDw3baX9YwDFQAAAAAdAAAAABAE'],
      comments: []
    },
    {
      id: 8,
      author: '이정우',
      title: '북촌 한옥마을 감성 산책',
      content: `서울 북촌 한옥마을은 사계절 내내 아름답지만, 봄에는 유난히 분위기가 좋아요. 
  한옥 지붕 너머로 보이는 푸른 하늘과 살랑거리는 벚꽃은 정말 그림 같았습니다. 
  전통 찻집에서 말차라떼를 마시며 잠시 쉬기도 했고, 작은 골목길마다 사진 찍기 좋은 포인트가 많아서 즐거웠어요. 
  북촌은 아침 일찍 방문하면 한적하게 산책할 수 있어서 추천합니다.`,
      createdAt: '2025-04-09T08:40:00Z',
      images: [],
      comments: []
    },
    {
      id: 9,
      author: '문채원',
      title: '광주 떡갈비 맛집 방문기',
      content: `광주 하면 떠오르는 음식, 바로 떡갈비! 
  광주에 있는 전통 떡갈비 전문점에 다녀왔는데, 
  불맛이 가득한 떡갈비를 밥과 함께 먹으니 정말 꿀맛이었어요. 
  가격은 1인분에 18,000원이었지만, 고기의 퀄리티를 생각하면 만족스러운 가격이었습니다. 
  매장 인테리어도 고풍스럽고 서비스도 친절했어요.`,
      createdAt: '2025-04-10T12:10:00Z',
      images: ['https://via.placeholder.com/300'],
      comments: []
    },
    {
      id: 10,
      author: '조민수',
      title: '제주 흑돼지 구이 체험기',
      content: `제주도에 왔다면 흑돼지를 빼놓을 수 없죠! 
  현지인 추천을 받아 찾은 흑돼지 전문점은, 숯불 향 가득한 고기를 제공했어요. 
  고기가 정말 부드럽고 육즙이 살아있었습니다. 
  같이 나온 멜젓(멸치젓)에 찍어 먹으면 감칠맛이 배가돼요. 
  저녁시간은 웨이팅이 많으니, 미리 예약하거나 오픈시간 맞춰 가는 걸 추천합니다.`,
      createdAt: '2025-04-11T18:20:00Z',
      images: [],
      comments: []
    },
    {
      id: 11,
      author: '김유진',
      title: '한강 공원 러닝 코스',
      content: '한강 공원 산책로를 추천합니다. 자전거 대여도 가능해요.',
      createdAt: '2025-04-11T07:30:00Z',
      images: [],
      comments: []
    },
    {
      id: 12,
      author: '박해준',
      title: '가평 1박2일 여행 계획',
      content: '가평 펜션과 맛집 리스트를 정리했습니다.',
      createdAt: '2025-04-12T09:15:00Z',
      images: [],
      comments: []
    },
    {
      id: 13,
      author: '유소영',
      title: '대전 카페거리 추천',
      content: '대전 OO 카페거리가 아기자기하고 예뻐요.',
      createdAt: '2025-04-13T14:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 14,
      author: '안성진',
      title: '순천만 국가정원 후기',
      content: '순천만 국가정원의 꽃밭이 아름다웠습니다.',
      createdAt: '2025-04-14T11:50:00Z',
      images: ['https://via.placeholder.com/150'],
      comments: []
    },
    {
      id: 15,
      author: '최지현',
      title: '강릉 커피거리 핫플레이스',
      content: '강릉 OO 커피거리가 힐링됩니다.',
      createdAt: '2025-04-15T16:25:00Z',
      images: [],
      comments: []
    },
    {
      id: 16,
      author: '이동휘',
      title: '서울 스카이 전망대 후기',
      content: '롯데월드타워 전망대에서 전망이 최고입니다.',
      createdAt: '2025-04-16T18:40:00Z',
      images: [],
      comments: []
    },
    {
      id: 17,
      author: '정소민',
      title: '부산 해운대 맛집 추천',
      content: '해운대 OO 회센터 리뷰.',
      createdAt: '2025-04-17T13:10:00Z',
      images: ['https://via.placeholder.com/150'],
      comments: []
    },
    {
      id: 18,
      author: '김건우',
      title: '서울 예술의 전당 공연 후기',
      content: '최신 공연을 보고 왔습니다. 음악이 감동적이었어요.',
      createdAt: '2025-04-18T20:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 19,
      author: '이수민',
      title: '경주 역사 유적 여행',
      content: '경주 불국사·첨성대를 방문했습니다.',
      createdAt: '2025-04-19T10:30:00Z',
      images: [],
      comments: []
    },
    {
      id: 20,
      author: '박시우',
      title: '인천 차이나타운 추천',
      content: '인천 차이나타운의 분위기와 맛집을 소개합니다.',
      createdAt: '2025-04-20T15:45:00Z',
      images: [],
      comments: []
    },
    {
      id: 21,
      author: '최민지',
      title: '서울 남산타워 야경',
      content: '남산 타워에서 보는 서울의 야경이 환상적입니다.',
      createdAt: '2025-04-21T21:20:00Z',
      images: ['/images/namsan.jpg'],
      comments: []
    },
    {
      id: 22,
      author: '안유진',
      title: '춘천 닭갈비 맛집 후기',
      content: '춘천 OO 닭갈비 골목 탐방기.',
      createdAt: '2025-04-22T12:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 23,
      author: '홍석준',
      title: '서울 카페 오픈 정보',
      content: '최근 오픈한 서울 OO 카페 방문 후기.',
      createdAt: '2025-04-23T10:10:00Z',
      images: [],
      comments: []
    },
    {
      id: 24,
      author: '윤지아',
      title: '울산 태화강 십리대숲 산책',
      content: '태화강 십리대숲 코스가 상쾌합니다.',
      createdAt: '2025-04-24T09:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 25,
      author: '송은채',
      title: '서울 한강 유람선 탑승 후기',
      content: '한강 유람선에서 보는 서울 풍경이 색다릅니다.',
      createdAt: '2025-04-25T19:30:00Z',
      images: [],
      comments: []
    },
    {
      id: 26,
      author: '김철민',
      title: '경기도 수원 화성 산책',
      content: '수원 화성을 둘러보며 역사 공부를 했습니다.',
      createdAt: '2025-04-26T14:20:00Z',
      images: [],
      comments: []
    },
    {
      id: 27,
      author: '박지훈',
      title: '대구 동성로 맛집 탐방',
      content: '동성로 OO 떡볶이가 최고였어요.',
      createdAt: '2025-04-27T11:45:00Z',
      images: [],
      comments: []
    },
    {
      id: 28,
      author: '정다영',
      title: '서울 북촌 한옥 촬영 스폿',
      content: '북촌에서 인생샷 찍기 좋은 장소를 공유합니다.',
      createdAt: '2025-04-28T16:00:00Z',
      images: [],
      comments: []
    },
    {
      id: 29,
      author: '윤성호',
      title: '충남 보령 대천 해수욕장 후기',
      content: '대천 해수욕장에서 바다를 즐겼습니다.',
      createdAt: '2025-04-29T13:15:00Z',
      images: [],
      comments: []
    },
    {
      id: 30,
      author: '이은비',
      title: '서울 코엑스 아쿠아리움 방문기',
      content: '코엑스 아쿠아리움의 해양 생물들이 인상적이었습니다.',
      createdAt: '2025-04-30T10:50:00Z',
      images: [],
      comments: []
    }
  ];
  
  export default initialPosts;
  