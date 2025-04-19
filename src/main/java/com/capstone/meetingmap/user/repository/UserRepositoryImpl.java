package com.capstone.meetingmap.user.repository;

import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        User user = em.find(User.class, userId);
        return Optional.ofNullable(user);
    }

    public boolean existsByUserId(String userId) {
        String jpql = "SELECT COUNT(u) > 0 FROM User u WHERE u.userId = :userId";
        Boolean exists = em.createQuery(jpql, Boolean.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return exists != null && exists; // null 체크와 함께 Boolean 값을 반환
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
}
