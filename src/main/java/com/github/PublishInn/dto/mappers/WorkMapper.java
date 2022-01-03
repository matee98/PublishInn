package com.github.PublishInn.dto.mappers;

import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.model.entity.Work;
import org.mapstruct.Mapper;

@Mapper
public interface WorkMapper {
    Work fromWorkSaveDto(WorkSaveDto workSaveDto);
}
