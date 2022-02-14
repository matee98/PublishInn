package com.github.PublishInn.unitTests.mappers;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkMapperTest {
    private final WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
    private final Work work = new Work();
    private final WorkSaveDto workSaveDto = new WorkSaveDto(
            "Test",
            WorkType.FANTASY,
            "Lorem ipsum, lorem ipsum"
    );

    @BeforeAll
    void setup() {
        work.setUserId(2L);
        work.setTitle("Super tytuł");
        work.setStatus(WorkStatus.ACCEPTED);
        work.setText("Lorem ipsum");
        work.setId(4L);
        work.setRating(BigDecimal.valueOf(7.99));
        work.setCreatedBy(2L);
        work.setType(WorkType.CRIME);
    }

    @Test
    void toWorkInfoDtoTest() {
        WorkInfoDto dto = mapper.toWorkInfoDto(work);

        assertThat(dto.getId()).isEqualTo(work.getId());
        assertThat(dto.getRating()).isEqualTo(work.getRating());
        assertThat(dto.getStatus()).isEqualTo(work.getStatus());
        assertThat(dto.getTitle()).isEqualTo(work.getTitle());
        assertThat(dto.getType()).isEqualTo(work.getType());
    }

    @Test
    void toWorkDetailsDtoTest() {
        WorkDetailsDto dto = mapper.toDto(work);

        assertThat(dto.getRating()).isEqualTo(work.getRating());
        assertThat(dto.getText()).isEqualTo(work.getText());
        assertThat(dto.getStatus()).isEqualTo(work.getStatus());
        assertThat(dto.getTitle()).isEqualTo(work.getTitle());
        assertThat(dto.getType()).isEqualTo(work.getType());
    }

    @Test
    void toWorkTest() {
        Work toWork = mapper.fromWorkSaveDto(workSaveDto);

        assertThat(toWork.getText()).isEqualTo(workSaveDto.getText());
        assertThat(toWork.getType()).isEqualTo(workSaveDto.getType());
        assertThat(toWork.getTitle()).isEqualTo(workSaveDto.getTitle());
    }
}
