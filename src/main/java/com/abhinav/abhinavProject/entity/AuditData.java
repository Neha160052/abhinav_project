package com.abhinav.abhinavProject.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditData {

    @CreatedBy
    String createdBy;

    @CreatedDate
    LocalDateTime createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    LocalDateTime lastModifiedDate;

}