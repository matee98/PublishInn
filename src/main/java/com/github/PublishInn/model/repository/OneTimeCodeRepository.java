package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.OneTimeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimeCodeRepository extends JpaRepository<OneTimeCode, Long> {
}
