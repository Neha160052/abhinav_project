package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    void saveUserProfileImage(MultipartFile file, Long userId) throws IOException;

    Resource loadUserProfileImage(Long userId) throws IOException;

    Optional<Path> fileExists(Long userId) throws IOException;

    String saveVariationPrimaryImage(ProductVariation savedVariation, MultipartFile primaryImage) throws IOException;

    void saveVariationSecondaryImages(ProductVariation variation, List<MultipartFile> secondaryImages) throws IOException;

    Resource loadVariationPrimaryImage(Long productId, Long variationId) throws IOException;

    List<String> listSecondaryFiles(long productId, Long variationId);

    Resource loadVariationSecondaryImage(Long productId, Long variationId, String imageName) throws IOException;
}
