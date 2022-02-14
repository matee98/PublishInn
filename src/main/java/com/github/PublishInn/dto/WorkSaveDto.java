package com.github.PublishInn.dto;

import com.github.PublishInn.model.entity.enums.WorkType;
import com.github.PublishInn.validation.Title;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class WorkSaveDto {
    @Title
    private String title;
    @NotNull
    private WorkType type;
    @Size(min = 1)
    @NotBlank
    private String text;
}
