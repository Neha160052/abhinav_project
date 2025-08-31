package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.service.MetadataFieldService;
import com.abhinav.abhinavProject.vo.MetadataFieldDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/category/metadata")
public class MetadataFieldController {

    MetadataFieldService metadataFieldService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponseVO<List<MetadataFieldDetailsVO>>> getAllMetadataFields(
            @PageableDefault(sort = "name") Pageable pageable,
            @RequestParam("query") String query
            ) {
        return ResponseEntity.ok(metadataFieldService.getAllFields(pageable, query));
    }
}
