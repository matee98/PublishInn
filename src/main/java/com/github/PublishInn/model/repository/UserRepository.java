package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.AppUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository {
    Optional<AppUser> findByEmail(String email);
}
