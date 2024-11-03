package com.APIDevelopmentTask.Todo.app.external.repository;

import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

