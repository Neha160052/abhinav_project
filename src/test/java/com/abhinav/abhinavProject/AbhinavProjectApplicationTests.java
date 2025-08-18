package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.entity.user.Customer;
import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class AbhinavProjectApplicationTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void customerSaveTest() {
        User user = new User();
        user.setActive(true);
        user.setDeleted(false);
        user.setEmail("abhinav@mail.com");
        user.setExpired(false);
        user.setFirstName("Abhinav");
        user.setLastName("Chaudhary");
        user.setInvalidAttemptCount(0);
        user.setLocked(false);
        user.setMiddleName("");
        user.setPassword("abc@123");
        user.setRole(roleRepository.findByAuthority("ROLE_SELLER"));

        Customer customer = new Customer();
        customer.setContact(9919788111L);
    }

}
