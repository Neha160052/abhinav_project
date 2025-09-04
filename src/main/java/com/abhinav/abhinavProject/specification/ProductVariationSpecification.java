package com.abhinav.abhinavProject.specification;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductVariationSpecification {

    public static Specification<ProductVariation> hasSellerId(Long sellerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("seller").get("id"), sellerId);
    }

    public static Specification<ProductVariation> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    public static Specification<ProductVariation> quantityAvailable(int quantity) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("quantityAvailable"), quantity);
    }

    public static Specification<ProductVariation> priceEquals(double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<ProductVariation> isVariationActive(boolean isActive) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), isActive);
    }
}
