package com.abhinav.abhinavProject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface ImageService {
    void save(MultipartFile file, Long userId) throws IOException;
    Resource load(Long userId) throws IOException;
    Optional<Path> fileExists(Long userId) throws IOException;
}
