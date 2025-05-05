package com.capstone.meetingmap.map.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contentTypeNo;

    @Column(length = 2)
    private String contentTypeId;

    @Column(length = 30)
    private String contentTypeName;
}
