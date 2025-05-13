package com.capstone.meetingmap.board.entity;

import com.capstone.meetingmap.category.entity.Category;
import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BoardTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardNo; //게시글 번호

    private String boardTitle; //게시글 제목
    private String boardContent; //게시글 내용
    private Integer boardViewCount = 0; //게시글 조회수

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) //회원아이디 외래키
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "category_no", nullable = false) //카테고리 번호 외래키
    private Category category;

    @Builder
    public Board(String boardTitle, String boardContent, Category category, User user) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.user = user;
        this.category = category;
    }

    public void increaseViewCount() {
        this.boardViewCount += 1;
    }
}
