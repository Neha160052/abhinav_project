package com.abhinav.abhinavProject.specification;

import com.abhinav.abhinavProject.entity.product.Product;
import com.abhinav.abhinavProject.entity.product.ProductVariation;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpecification {

    public static Specification<Product> hasSellerId(Long sellerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("seller").get("id"), sellerId);
    }

    public static Specification<Product> nameOrBrandContains(String searchTerm) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("brand")), "%" + searchTerm.toLowerCase() + "%")
        );
    }

    public static Specification<Product> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Product> hasCategoryIn(List<Long> categoryIds) {
        return (root, query, cb) -> root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Product> hasActiveVariations() {
        return (root, query, cb) -> {
            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ProductVariation> subRoot = subquery.from(ProductVariation.class);
            subquery.select(subRoot.get("product").get("id"));
            subquery.where(cb.isTrue(subRoot.get("isActive")));
            return cb.in(root.get("id")).value(subquery);
        };
    }

    public static Specification<Product> hasMetadata(Map<String, String> metadataFilters) {
        return (root, query, cb) -> {
            if (metadataFilters == null || metadataFilters.isEmpty()) {
                return cb.conjunction();
            }

            Join<Product, ProductVariation> variationJoin = root.join("variations", JoinType.INNER);
            Predicate metadataPredicate = cb.conjunction();

            for (Map.Entry<String, String> entry : metadataFilters.entrySet()) {
                // This function call is specific to MySQL for querying JSON.
                // It extracts the value of a key from the 'metadata' JSON column.
                // The path '$.key' is used to access the value.
                Expression<String> jsonExtract = cb.function(
                        "JSON_EXTRACT",
                        String.class,
                        variationJoin.get("metadata"),
                        cb.literal("$." + entry.getKey())
                );
                Expression<String> jsonValue = cb.function(
                        "JSON_UNQUOTE",
                        String.class,
                        jsonExtract
                );

                metadataPredicate = cb.and(metadataPredicate, cb.equal(jsonValue, entry.getValue()));
            }
            assert query != null;
            query.distinct(true); // Ensure distinct products are returned
            return cb.and(metadataPredicate, cb.isTrue(variationJoin.get("isActive")));
        };
    }
}
