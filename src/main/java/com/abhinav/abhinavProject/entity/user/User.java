package com.abhinav.abhinavProject.entity.user;

import com.abhinav.abhinavProject.entity.AuditData;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
})
//@SoftDelete(columnName = "is_deleted", strategy = SoftDeleteType.DELETED)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Address> address = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    PasswordResetToken passwordResetToken;

    @Embedded
    AuditData auditData = new AuditData();

}
