package com.github.PublishInn.dto.mappers;

import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.model.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper
public interface AppUserMapper {
    UserInfoDto toUserInfoDto(AppUser user);
}
