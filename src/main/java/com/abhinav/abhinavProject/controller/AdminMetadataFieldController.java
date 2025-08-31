package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.NewMetadataFieldCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.MetadataFieldService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.MetadataFieldDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminMetadataFieldController {

    MetadataFieldService metadataFieldService;
    MessageUtil messageUtil;

    @GetMapping("/metadata-field")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponseVO<List<MetadataFieldDetailsVO>>> getAllMetadataFields(
            @PageableDefault(sort = "name") Pageable pageable,
            @RequestParam(value = "query", required = false) String query
            ) {
        return ResponseEntity.ok(metadataFieldService.getAllFields(pageable, query));
    }

    @PostMapping("/metadata-field")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addNewField(@RequestBody @Valid NewMetadataFieldCO newMetadataFieldCO) {
        MetadataFieldDetailsVO newField = metadataFieldService.addNewField(newMetadataFieldCO);

        return ResponseEntity.ok(
                new ApiResponse(messageUtil.getMessage("metadatafield.added.success"), newField)
        );
    }
}
