package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.entity.category.CategoryMetadataField;
import com.abhinav.abhinavProject.repository.CategoryMetadataFieldRepository;
import com.abhinav.abhinavProject.service.MetadataFieldService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.MetadataFieldDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}
