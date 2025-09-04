package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.entity.product.ProductVariation;
import com.abhinav.abhinavProject.service.ImageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

    Path userImageStorageLocation;
    Path productVariationImageStorageLocation;
    private final List<String> supportedFormats = Arrays.asList("jpg", "jpeg", "png", "bmp");

    public ImageServiceImpl(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        Path basePath = Paths.get(uploadDir);
        this.userImageStorageLocation = basePath.resolve("users").toAbsolutePath().normalize();
        this.productVariationImageStorageLocation = basePath.resolve("products").toAbsolutePath().normalize();

        Files.createDirectories(this.userImageStorageLocation);
        Files.createDirectories(this.productVariationImageStorageLocation);
    }

    @Override
    public void saveUserProfileImage(MultipartFile file, Long userId) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = getFileExtension(originalFilename);
        validateFileExtension(extension);

        deleteExistingFile(userId);

        String newFileName = userId + "." + extension;
        Path targetLocation = userImageStorageLocation.resolve(newFileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public Resource loadUserProfileImage(Long userId) throws IOException {
        Optional<Path> foundFile;
        try (Stream<Path> files = Files.walk(userImageStorageLocation, 1)) {
            foundFile = files
                    .filter(path -> path.getFileName().toString().startsWith(userId + "."))
                    .findFirst();
        }

        if (foundFile.isPresent()) {
            Path filePath = foundFile.get();
            try {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new IOException("Could not read file for user: " + userId);
                }
            } catch (MalformedURLException ex) {
                throw new IOException("Could not form URL for file for user: " + userId, ex);
            }
        } else {
            throw new FileNotFoundException("File not found for user: " + userId);
        }
    }

    @Override
    public String saveVariationPrimaryImage(ProductVariation variation, MultipartFile primaryImage) throws IOException {
        String fileName = String.valueOf(variation.getId());
        return saveVariationImage(variation, primaryImage, fileName);
    }

    @Override
    public Resource loadVariationPrimaryImage(Long productId, Long variationId) throws IOException {
        Path variationImagesPath = productVariationImageStorageLocation
                .resolve(String.valueOf(productId))
                .resolve("variations");

        if (!Files.exists(variationImagesPath) || !Files.isDirectory(variationImagesPath)) {
            throw new FileNotFoundException("Image directory not found for product: " + productId);
        }

        Optional<Path> foundFile;
        try (Stream<Path> files = Files.walk(variationImagesPath, 1)) {
            foundFile = files
                    .filter(path -> path.getFileName().toString().startsWith(variationId + "."))
                    .findFirst();
        }

        if (foundFile.isPresent()) {
            Path filePath = foundFile.get();
            try {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() && resource.isReadable()) {
                    return resource;
                } else {
                    throw new IOException("Could not read file: " + filePath);
                }
            } catch (MalformedURLException ex) {
                throw new IOException("Could not form URL for file: " + filePath, ex);
            }
        } else {
            throw new FileNotFoundException("Primary image not found for product variation: " + variationId);
        }
    }

    @Override
    public void saveVariationSecondaryImages(ProductVariation variation, List<MultipartFile> secondaryImages) throws IOException {
        if (secondaryImages == null || secondaryImages.isEmpty()) {
            return;
        }

        int imageCounter = 1;
        for (MultipartFile image : secondaryImages) {
            String fileName = variation.getId() + "_" + imageCounter++;
            saveVariationImage(variation, image, fileName);
        }
    }

    private String saveVariationImage(ProductVariation variation, MultipartFile image, String fileNameWithoutExtension) throws IOException {
        long productId = variation.getProduct().getId();

        Path variationPath = productVariationImageStorageLocation
                .resolve(String.valueOf(productId))
                .resolve("variations");

        Files.createDirectories(variationPath);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String extension = getFileExtension(originalFilename);
        validateFileExtension(extension);

        String newFileName = fileNameWithoutExtension + "." + extension;
        Path targetLocation = variationPath.resolve(newFileName);

        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    private void validateFileExtension(String extension) throws IOException {
        if (extension.isEmpty()) {
            throw new IOException("Invalid file format. No extension found.");
        }
        if (!supportedFormats.contains(extension)) {
            throw new IOException("Invalid file format. Supported formats are: " + supportedFormats);
        }
    }

    private String getFileExtension(String fileName) {
        return Optional.of(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1).toLowerCase())
                .orElse("");
    }

    public Optional<Path> fileExists(Long userId) throws IOException {
        try (Stream<Path> files = Files.walk(userImageStorageLocation, 1)) {
            return files.filter(path -> path.getFileName().toString().startsWith(userId + "."))
                    .findFirst();
        }
    }

    private void deleteExistingFile(Long userId) throws IOException {
        Optional<Path> existingFile = fileExists(userId);

        if (existingFile.isPresent()) {
            Files.delete(existingFile.get());
        }
    }
}
