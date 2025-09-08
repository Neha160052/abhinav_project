package com.abhinav.abhinavProject.entity.category;


import com.abhinav.abhinavProject.entity.AuditData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category_id", "categoryMetadataField_id"})
})
public class CategoryMetadataFieldValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    Category category;

    @ManyToOne
    CategoryMetadataField categoryMetadataField;

    String valuesList;

    @Embedded
    AuditData auditData = new AuditData();

    public void setValuesList(Set<String> valueSet) {
        this.valuesList = valueSet.stream().collect(Collectors.joining(", "));
    }

    public Set<String> getValuesList() {
        if(valuesList == null || valuesList.isEmpty()){
            return Set.of();
        }
        return Arrays.stream(valuesList.split(", ")).collect(Collectors.toSet());
    }
}
