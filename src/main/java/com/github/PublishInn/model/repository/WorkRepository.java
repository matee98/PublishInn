package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    public List<Work> findAllByTypeEquals(WorkType type);
    public List<Work> findAllByTypeIsNotLike(WorkType type);
}
