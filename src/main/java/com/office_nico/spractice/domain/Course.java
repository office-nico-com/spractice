package com.office_nico.spractice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="courses")
@Data
public class Course {


    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String courseNameJa;

    @Column(nullable = true)
    private byte[] thumbnail;

    @Column(nullable = true)
    private String description = null;

    @Column(nullable = false)
    private LocalDate startDate = null;

    @Column(nullable = false)
    private LocalDate endDate = null;

    @Column(nullable = true)
    private String note = null;

    @Column(nullable = false)
    private Integer orderNumber = null;

    @Column(nullable = false)
    private Boolean isInvalided = null;

    @Column(nullable = false)
    private Boolean isDeleted = null;

    @Column(nullable = false)
    private LocalDateTime createdAt = null;

    @Column(nullable = true)
    private LocalDateTime updatedAt = null;

    @Column(nullable = false)
    private Long createdBy = null;

    @Column(nullable = true)
    private Long updatedBy = null;


}
