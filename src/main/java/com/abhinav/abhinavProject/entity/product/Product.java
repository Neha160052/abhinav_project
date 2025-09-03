package com.abhinav.abhinavProject.entity.product;

import com.abhinav.abhinavProject.entity.AuditData;
import com.abhinav.abhinavProject.entity.category.Category;
import com.abhinav.abhinavProject.entity.user.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    Seller seller;

    String name;

    String brand;

    String description;

    boolean isCancellable;

    boolean isReturnable;

    boolean isActive;

    boolean isDeleted;

    @ManyToOne
    Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductVariation> variations;

    @Embedded
    AuditData auditData = new AuditData();
}
