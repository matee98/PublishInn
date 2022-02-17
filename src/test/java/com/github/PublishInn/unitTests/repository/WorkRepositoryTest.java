package com.github.PublishInn.unitTests.repository;

import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import com.github.PublishInn.model.repository.WorkRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkRepositoryTest {

    @Autowired
    private WorkRepository workRepository;
    private final Work work = new Work();
    private final Work work2 = new Work();
    private final Work work3 = new Work();

    @BeforeAll
    void setup() {
        work.setUserId(2L);
        work.setType(WorkType.FANTASY);
        work.setTitle("Fantastyczna opowieść");
        work.setStatus(WorkStatus.ACCEPTED);
        work.setCreatedBy(2L);
        work.setText("Lorem ipsum fantasy");

        work2.setUserId(2L);
        work2.setType(WorkType.CRIME);
        work2.setTitle("Kryminalna opowieść");
        work2.setStatus(WorkStatus.ACCEPTED);
        work2.setCreatedBy(2L);
        work2.setText("Lorem ipsum crime");

        work3.setUserId(1L);
        work3.setType(WorkType.OTHER);
        work3.setTitle("Inna opowieść");
        work3.setStatus(WorkStatus.BLOCKED);
        work3.setCreatedBy(1L);
        work3.setText("Lorem ipsum inne");

        workRepository.save(work);
        workRepository.save(work2);
        workRepository.save(work3);
    }

    @Test
    void findAllByTypeEqualsTest() {
        List<Work> works = workRepository.findAllByTypeEquals(WorkType.FANTASY);
        Work testWork = works.get(0);

        assertThat(works.size()).isEqualTo(1);
        assertThat(testWork.getTitle()).isEqualTo("Fantastyczna opowieść");
        assertThat(testWork.getType()).isEqualTo(WorkType.FANTASY);
        assertThat(testWork.getUserId()).isEqualTo(2L);
    }

    @Test
    void findAllByTypeIsNotLikeTest() {
        List<Work> works = workRepository.findAllByTypeIsNotLike(WorkType.FANTASY);
        Optional<Work> crimeWork = works
                .stream()
                .filter(work1 -> work1.getType().equals(WorkType.CRIME))
                .findFirst();

        assertThat(works.size()).isEqualTo(2);
        assertThat(crimeWork.isPresent()).isTrue();
        assertThat(crimeWork.get().getTitle()).isEqualTo("Kryminalna opowieść");
    }

    @Test
    void findAllByUserIdEqualsTest() {
        List<Work> works = workRepository.findAllByUserIdEquals(1L);
        Work testWork = works.get(0);

        assertThat(works.size()).isEqualTo(1);
        assertThat(testWork.getUserId()).isEqualTo(1);
        assertThat(testWork.getTitle()).isEqualTo("Inna opowieść");
        assertThat(testWork.getType()).isEqualTo(WorkType.OTHER);
    }

    @Test
    void findAllByTitleContainsIgnoreCaseAndStatusEqualsTest() {
        List<Work> works =
                workRepository.findAllByTitleContainsIgnoreCaseAndStatusEquals("OPOWIE", WorkStatus.ACCEPTED);
        Optional<Work> fantasyWork = works
                .stream()
                .filter(x -> x.getType().equals(WorkType.FANTASY))
                .findFirst();

        Optional<Work> emptyWork = works
                .stream()
                .filter(x -> x.getStatus().equals(WorkStatus.BLOCKED))
                .findAny();

        assertThat(works.size()).isEqualTo(2);
        assertThat(fantasyWork.isPresent()).isTrue();
        assertThat(fantasyWork.get().getTitle()).isEqualTo("Fantastyczna opowieść");
        assertThat(emptyWork.isPresent()).isFalse();
    }
}
