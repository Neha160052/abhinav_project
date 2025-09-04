package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/static")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ImageController {

    ImageService imageService;

    @GetMapping("/user/{userId}/profile-image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable Long userId, HttpServletRequest request) {
        try {
            Resource resource = imageService.loadUserProfileImage(userId);
            String contentType = getContentType(request, resource);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}/variation/{variationId}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable Long productId,
                                                    @PathVariable Long variationId,
                                                    HttpServletRequest request) {
        try {
            Resource resource = imageService.loadVariationPrimaryImage(productId, variationId);
            String contentType = getContentType(request, resource);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getContentType(HttpServletRequest request, Resource resource) {
        try {
            return request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }
}
