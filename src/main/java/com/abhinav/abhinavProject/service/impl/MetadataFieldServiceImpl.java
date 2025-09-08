package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.NewMetadataFieldCO;
import com.abhinav.abhinavProject.entity.category.CategoryMetadataField;
import com.abhinav.abhinavProject.repository.CategoryMetadataFieldRepository;
import com.abhinav.abhinavProject.service.MetadataFieldService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.MetadataFieldDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MetadataFieldServiceImpl implements MetadataFieldService {

    CategoryMetadataFieldRepository metadataFieldRepository;
    MessageUtil messageUtil;

    @Override
    public PageResponseVO<List<MetadataFieldDetailsVO>> getAllFields(Pageable pageable, String query) {
        Page<CategoryMetadataField> resultSet = metadataFieldRepository.findByNameContainingIgnoreCase(query, pageable);
        Page<MetadataFieldDetailsVO> fields = resultSet.map(MetadataFieldDetailsVO::new);
        return new PageResponseVO<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                fields.hasNext(),
                fields.getContent()
        );
    }

    @Override
    public MetadataFieldDetailsVO addNewField(NewMetadataFieldCO newMetadataFieldCO) {
        if(metadataFieldRepository.existsByNameIgnoreCase(newMetadataFieldCO.getName())) {
            log.info("Validation failed: Metadata field with name '{}' already exists.", newMetadataFieldCO.getName());
            throw new ValidationException(messageUtil.getMessage("metadatafield.name.exists"));
        }

        CategoryMetadataField metadataField = new CategoryMetadataField();
        metadataField.setName(newMetadataFieldCO.getName());
        CategoryMetadataField savedField = metadataFieldRepository.save(metadataField);

        log.info("Successfully saved new metadata field with ID: {} and Name: {}", savedField.getId(), savedField.getName());
        return new MetadataFieldDetailsVO(savedField);
    }
}
