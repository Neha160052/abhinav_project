package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.repository.CategoryMetadataFieldValuesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AbhinavProjectApplicationTests {

    @Autowired
    CategoryMetadataFieldValuesRepository repo;

    @Test
    void contextLoads() {
    }

    @Test
    void metadataSet() {
    }

}
