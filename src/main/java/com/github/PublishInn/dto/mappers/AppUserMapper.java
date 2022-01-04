package com.github.PublishInn.dto.mappers;

import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.dto.UserShortProfileDto;
import com.github.PublishInn.model.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AppUserMapper {
    UserInfoDto toUserInfoDto(AppUser user);

    @Mappings({
            @Mapping(source = "appUserRole", target = "userRole")
    })
    UserShortProfileDto toUserShortProfileDto(AppUser user);
}
