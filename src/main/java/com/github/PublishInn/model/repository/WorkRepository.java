package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByTypeEquals(WorkType type);
    List<Work> findAllByTypeIsNotLike(WorkType type);
    List<Work> findAllByUserIdEquals(Long id);
}
