package com.abhinav.abhinavProject.entity.user;

import com.abhinav.abhinavProject.entity.AuditData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String email;

    String firstName;

    String middleName;

    String lastName;

    String password;

    boolean isDeleted;

    boolean isActive;

    boolean isExpired;

    boolean isLocked;

    int invalidAttemptCount;

    ZonedDateTime passwordUpdateDate;

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    Role role;

    @ManyToMany
    Set<Role> roles = new HashSet<>();

    @Embedded
    AuditData auditData;

}
