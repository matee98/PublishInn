package com.github.PublishInn.unitTests.mappers;

import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.dto.UserShortProfileDto;
import com.github.PublishInn.dto.mappers.AppUserMapper;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppUserMapperTest {

    private final AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
    private final AppUser appUser = new AppUser();

    @BeforeAll
    void setup() {
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("john@example.com");
        appUser.setPassword("6165d066d7dca67b64005d450b0d40ea06f58434084c54134d2e77fe190e47a7");
        appUser.setLocked(false);
        appUser.setEnabled(true);
        appUser.setUsername("john1223");
    }


    @Test
    void toUserInfoDto() {
        UserInfoDto dto = mapper.toUserInfoDto(appUser);

        assertThat(dto.getAppUserRole()).isEqualTo(appUser.getAppUserRole());
        assertThat(dto.getEmail()).isEqualTo(appUser.getEmail());
        assertThat(dto.getUsername()).isEqualTo(appUser.getUsername());
    }

    @Test
    void toUserShortProfileDto() {
        UserShortProfileDto dto = mapper.toUserShortProfileDto(appUser);

        assertThat(dto.getUsername()).isEqualTo(appUser.getUsername());
        assertThat(dto.getUserRole()).isEqualTo(appUser.getAppUserRole().toString());
        assertThat(dto.getCreatedOn()).isNull();
        assertThat(dto.getWorksCount()).isEqualTo(0);
    }
}