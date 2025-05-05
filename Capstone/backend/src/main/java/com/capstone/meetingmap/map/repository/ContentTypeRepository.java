package com.capstone.meetingmap.map.repository;

import com.capstone.meetingmap.map.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {
    Optional<ContentType> findByContentTypeId(String contentTypeId);
}
