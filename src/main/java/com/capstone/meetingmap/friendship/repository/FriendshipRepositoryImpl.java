package com.capstone.meetingmap.friendship.repository;

import com.capstone.meetingmap.friendship.entity.Friendship;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final EntityManager em;

    public FriendshipRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Friendship save(Friendship friendship) {
        em.persist(friendship);
        return friendship;
    }

    @Override
    public Optional<Friendship> findByFriendshipId(Integer friendshipId) {
        Friendship friendship = em.find(Friendship.class, friendshipId);
        return Optional.ofNullable(friendship);
    }

    @Override
    public List<Friendship> findAllByUserId(String userId) {
        return em.createQuery("select f from Friendship f where f.user.userId = :userId", Friendship.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    @Override
    public List<Friendship> findAll() {
        return em.createQuery("select f from Friendship f", Friendship.class)
                .getResultList();
    }
}
