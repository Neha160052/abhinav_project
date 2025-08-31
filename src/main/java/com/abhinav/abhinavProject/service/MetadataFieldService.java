package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.NewMetadataFieldCO;
import com.abhinav.abhinavProject.vo.MetadataFieldDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MetadataFieldService {
    PageResponseVO<List<MetadataFieldDetailsVO>> getAllFields(Pageable pageable, String query);
    MetadataFieldDetailsVO addNewField(NewMetadataFieldCO newMetadataFieldCO);
}
