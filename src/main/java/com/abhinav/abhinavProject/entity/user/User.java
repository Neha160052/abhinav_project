package com.abhinav.abhinavProject.entity.user;

import com.abhinav.abhinavProject.entity.AuditData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
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

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    Set<Address> address;

    @Embedded
    AuditData auditData;

}
