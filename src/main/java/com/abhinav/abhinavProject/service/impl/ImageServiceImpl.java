package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.service.ImageService;
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

@Service
public class ImageServiceImpl implements ImageService {

    private final Path userImageStorageLocation;
    private final List<String> supportedFormats = Arrays.asList("jpg", "jpeg", "png", "bmp");

    public ImageServiceImpl(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        Path basePath = Paths.get(uploadDir);
        this.userImageStorageLocation = basePath.resolve("users").toAbsolutePath().normalize();

        Files.createDirectories(this.userImageStorageLocation);
    }

    @Override
    public void save(MultipartFile file, Long userId) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = Optional.of(originalFilename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase())
                .orElseThrow(() -> new IOException("Invalid file format. No extension found."));

        if (!supportedFormats.contains(extension)) {
            throw new IOException("Invalid file format. Supported formats are: " + supportedFormats);
        }

        deleteExistingFile(userId);

        String newFileName = userId + "." + extension;
        Path targetLocation = userImageStorageLocation.resolve(newFileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public Resource load(Long userId) throws IOException {
        Optional<Path> foundFile = Files.walk(userImageStorageLocation, 1)
                .filter(path -> {
                    String filename = path.getFileName().toString();
                    return filename.startsWith(userId + ".");
                })
                .findFirst();

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

    public Optional<Path> fileExists(Long userId) throws IOException {
        return Files.walk(userImageStorageLocation, 1)
                .filter(path -> path.getFileName().toString().startsWith(userId + "."))
                .findFirst();
    }

    private void deleteExistingFile(Long userId) throws IOException {
        Optional<Path> existingFile = fileExists(userId);

        if (existingFile.isPresent()) {
            Files.delete(existingFile.get());
        }
    }
}
