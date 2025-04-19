package com.capstone.meetingmap.friendship.entity;

import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendshipNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    private User opponent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isFrom;

    private Integer counterpartFriendshipNo;

    @Builder
    public Friendship(User user, User opponent, FriendshipStatus status, boolean isFrom, Integer counterpartFriendshipNo) {
        this.user = user;
        this.opponent = opponent;
        this.status = status;
        this.isFrom = isFrom;
        this.counterpartFriendshipNo = counterpartFriendshipNo;
    }

    public void acceptFriendshipRequest() {
        status = FriendshipStatus.ACCEPTED;
    }

    public void setCounterpartFriendshipNo(Integer counterpartFriendshipNo) {
        this.counterpartFriendshipNo = counterpartFriendshipNo;
    }
}
