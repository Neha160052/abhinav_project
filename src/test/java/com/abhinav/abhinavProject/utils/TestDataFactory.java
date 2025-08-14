package com.abhinav.abhinavProject.utils;

import com.abhinav.abhinavProject.entity.category.*;
import com.abhinav.abhinavProject.entity.user.*;
import com.abhinav.abhinavProject.entity.product.*;
import com.abhinav.abhinavProject.entity.order.*;
import com.abhinav.abhinavProject.entity.*;
import com.abhinav.abhinavProject.repository.CustomerRepository;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.SellerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestDataFactory {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    RoleRepository roleRepository;
//    @Autowired CategoryRepository categoryRepo;
//    @Autowired ProductRepository productRepo;
//    @Autowired ProductVariationRepository productVariationRepo;
//    @Autowired ProductReviewRepository productReviewRepo;
//    @Autowired CategoryMetadataFieldRepository metadataFieldRepo;
//    @Autowired CategoryMetadataFieldValuesRepository metadataFieldValueRepo;
//    @Autowired OrderRepository orderRepo;
//    @Autowired OrderProductRepository orderItemRepository;
//    @Autowired OrderStatusRepository orderStatusRepository;




//    @Test
//    void createSeller() {
//        User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setRole(roleRepository.findByAuthority("ROLE_SELLER"));
//
//        Address address = new Address();
//        address.setCity("Lucknow");
//        address.setCountry("India");
//        address.setLabel("Home");
//        address.setState("UP");
//
//        user.setAddress(Set.of(address));
//
//        Seller seller = new Seller();
//        seller.setCompanyContact(9919203949L);
//        seller.setCompanyName("ABC Ltd.");
//        seller.setGst("ABC1023COM");
//        seller.setUser(user);
//
//        System.out.println(sellerRepository.save(seller));
//    }



//    @Test
//    void createCustomer() {
//        User user = new User();
//        user.setFirstName("Johnny");
//        user.setLastName("Doeing");
//        user.setRole(roleRepository.findByAuthority("ROLE_CUSTOMER"));
//
//        Customer customer = new Customer();
//        customer.setContact(9912911939L);
//        customer.setUser(user);
//
//        System.out.println(customerRepository.save(customer));
//    }



//    void createCategoryMetadataField() {
//        if (defaultMetadataField == null) {
//            CategoryMetadataField mf = new CategoryMetadataField();
//            mf.setName("Color");
//            defaultMetadataField = metadataFieldRepo.save(mf);
//        }
//        return defaultMetadataField;
//    }



//    public Category createCategoryWithMetadata() {
//        if (defaultCategory == null) {
//            Category category = new Category();
//            category.setName("Electronics");
//            category = categoryRepo.save(category);
//
//            CategoryMetadataFieldValue mfv = new CategoryMetadataFieldValue();
//            mfv.setCategory(category);
//            mfv.setCategoryMetadataField(createCategoryMetadataField());
//            mfv.setValueSet(List.of("Black", "White", "Silver"));
//            mfv = metadataFieldValueRepo.save(mfv);
//
//            defaultCategory = category;
//        }
//        return defaultCategory;
//    }
//
//    public Product createProduct() {
//        if (defaultProduct == null) {
//            Product product = new Product();
//            product.setName("Smartphone");
//            product.setSeller(createSeller());
//            product.setCategory(createCategoryWithMetadata());
//            product.setIsCancellable(true);
//            product.setIsReturnable(true);
//            product.setBrand("BrandX");
//            product.setIsActive(true);
//            product.setIsDeleted(false);
//            defaultProduct = productRepo.save(product);
//        }
//        return defaultProduct;
//    }
//
//    public ProductVariation createProductVariation() {
//        if (defaultProductVariation == null) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode metadata = mapper.readTree("{\"Color\":\"Black\", \"OS\":\"Andi\"}");
//                ProductVariation variation = new ProductVariation();
//                variation.setProduct(createProduct());
//                variation.setStock(50);
//                variation.setPrice(699.99);
//                variation.setMetadata(metadata);
//                variation.setPrimaryImageName("phone.jpg");
//                variation.setIsActive(true);
//                defaultProductVariation = productVariationRepo.save(variation);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("Error creating product variation metadata", e);
//            }
//        }
//        return defaultProductVariation;
//    }
//
//    public ProductReview createProductReview() {
//        if (defaultProductReview == null) {
//            Product product = createProduct();
//            Customer customer = createCustomer();
//
//            ProductReviewKey key = new ProductReviewKey();
//            key.setCustomerUserId(customer.getId());
//            key.setProductId(product.getId());
//
//            ProductReview review = new ProductReview();
//            review.setProductReviewId(key);
//            review.setProduct(product);
//            review.setCustomer(customer);
//            review.setReview("Excellent product!");
//            review.setRating(Rating.FIVE);
//            defaultProductReview = productReviewRepo.save(review);
//        }
//        return defaultProductReview;
//    }
//
//    public Order createOrder() {
//        if (defaultOrder == null) {
//            Order orderToSave = Order.builder()
//                    .amountPaid(90.00)
//                    .customer(createCustomer())
//                    .paymentMethod(PaymentMethod.INTERNET_BANKING)
//                    .build();
//            defaultOrder = orderRepo.save(orderToSave);
//        }
//        return defaultOrder;
//    }
//
//    public OrderProduct createOrderProduct() {
//        if (defaultOrderProduct == null) {
//            OrderProduct oi = OrderProduct.builder()
//                    .productVariation(createProductVariation())
//                    .itemQuantity(3)
//                    .itemTotalPrice(40.00)
//                    .order(createOrder())
//                    .build();
//            defaultOrderProduct = orderItemRepository.save(oi);
//        }
//        return defaultOrderProduct;
//    }
//
//    public OrderStatus createOrderStatus() {
//        if (defaultOrderStatus == null) {
//            OrderProduct managedOrderProduct = orderItemRepository.save(createOrderProduct());
//
//            OrderStatus os = OrderStatus.builder()
//                    .fromStatus(OrderState.DELIVERED)
//                    .toStatus(OrderState.CLOSED)
//                    .orderItem(managedOrderProduct)
//                    .transitionDate(Instant.now())
//                    .transitionNotes("delivered hes yes")
//                    .build();
//
//            defaultOrderStatus = orderStatusRepository.save(os);
//        }
//        return defaultOrderStatus;
//    }
}