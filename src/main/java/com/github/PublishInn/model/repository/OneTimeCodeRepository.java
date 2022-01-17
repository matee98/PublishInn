package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.OneTimeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneTimeCodeRepository extends JpaRepository<OneTimeCode, Long> {
    Optional<OneTimeCode> findByCode(String code);
}
