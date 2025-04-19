package com.capstone.meetingmap.category.repository;

import com.capstone.meetingmap.category.entity.Category;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final EntityManager em;

    public CategoryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Category save(Category category) {
        em.persist(category);
        return category;
    }

    @Override
    public Optional<Category> findByCategoryNo(Integer categoryNo) {
        Category category = em.find(Category.class, categoryNo);
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }
}
