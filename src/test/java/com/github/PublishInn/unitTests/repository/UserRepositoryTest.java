package com.github.PublishInn.unitTests.repository;

import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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
    void findByEmailTest() {
        AppUser user = userRepository.findByEmail("john@example.com").orElseThrow();

        assertThat(user.getUsername()).isEqualTo(appUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(appUser.getPassword());
        assertThat(user.getEmail()).isEqualTo(appUser.getEmail());
        assertThat(user.isLocked()).isEqualTo(appUser.isLocked());
        assertThat(user.getCreatedOn()).isBefore(LocalDateTime.now());
        assertThat(user.getId()).isNotNull();
        assertThat(user.getModifiedOn()).isNull();
        assertThat(user.getVersion()).isEqualTo(0);
    }

    @Test
    void findByUsernameTest() {
        AppUser user = userRepository.findByUsername("john1223").orElseThrow();

        assertThat(user.getUsername()).isEqualTo(appUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(appUser.getPassword());
        assertThat(user.getEmail()).isEqualTo(appUser.getEmail());
        assertThat(user.getCreatedOn()).isBefore(LocalDateTime.now());
        assertThat(user.getId()).isNotNull();
    }
}