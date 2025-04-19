package com.capstone.meetingmap.userrole.repository;

import com.capstone.meetingmap.userrole.entity.UserRole;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserRoleRepositoryImpl implements UserRoleRepository {

    private final EntityManager em;

    public UserRoleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<UserRole> findByUserType(Integer userType) {
        UserRole userRole = em.find(UserRole.class, userType);
        return Optional.ofNullable(userRole);
    }

    @Override
    public List<UserRole> findAll() {
        return em.createQuery("select u from UserRole u", UserRole.class)
                .getResultList();
    }
}
