package com.github.PublishInn.unitTests.repository;

import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private final AppUser appUser = new AppUser();

    @BeforeAll
    void setup() {
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("john@example.com");
        appUser.setPassword("6165d066d7dca67b64005d450b0d40ea06f58434084c54134d2e77fe190e47a7");
        appUser.setLocked(false);
        appUser.setEnabled(true);
        appUser.setUsername("john1223");

        userRepository.save(appUser);
    }

    @Test
    void findByEmail() {
        AppUser user = userRepository.findByEmail("john@example.com").orElseThrow();

        assertThat(user).isEqualTo(appUser);
        assertThat(user.getCreatedOn()).isBefore(LocalDateTime.now());
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void findByUsername() {
        AppUser user = userRepository.findByUsername("john1223").orElseThrow();

        assertThat(user).isEqualTo(appUser);
        assertThat(user.getCreatedOn()).isBefore(LocalDateTime.now());
        assertThat(user.getId()).isNotNull();
    }
}