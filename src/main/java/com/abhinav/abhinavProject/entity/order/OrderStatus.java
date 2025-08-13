package com.abhinav.abhinavProject.entity.order;


import com.abhinav.abhinavProject.entity.AuditData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatus {

    @Id
    @OneToOne
    OrderProduct orderProduct;

    @Enumerated(EnumType.STRING)
    OrderStatusEnum fromStatus;

    @Enumerated(EnumType.STRING)
    OrderStatusEnum toStatus;

    String transitionNotesComments;

    @Embedded
    @AttributeOverride(name="createdDate", column=@Column(name="transition_date"))
    AuditData auditData;

}
