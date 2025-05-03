package com.capstone.meetingmap.user.entity;

import com.capstone.meetingmap.friendship.entity.Friendship;
import com.capstone.meetingmap.userrole.entity.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private String userId;
    private String userEmail;
    private String userPasswd;
    private String userNick;
    private String userImg;
    private String userAddress;

    @ManyToOne
    @JoinColumn(name = "user_type")
    private UserRole userRole;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Friendship> friendshipList = new ArrayList<>();

    @OneToMany(mappedBy = "opponent", fetch = FetchType.LAZY)
    private List<Friendship> opponentFriendshipList = new ArrayList<>();

    @Builder
    public User(String userId, String userEmail, String userPasswd, String userNick, String userImg, String userAddress, UserRole userRole) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPasswd = userPasswd;
        this.userNick = userNick;
        this.userImg = userImg;
        this.userAddress = userAddress;
        this.userRole = userRole;
    }

    public User updateInfo(String userEmail, String userNick, String userAddress) {
        if (userEmail != null)
            this.userEmail = userEmail;
        if (userNick != null)
            this.userNick = userNick;
        if (userAddress != null)
            this.userAddress = userAddress;
        return this;
    }
}
