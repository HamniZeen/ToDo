package com.APIDevelopmentTask.Todo.app.external.repository;

import com.APIDevelopmentTask.Todo.app.domain.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByUserId(Long userId, Pageable pageable);
    List<Todo> findByUserIdAndTitleContaining(Long userId, String keyword);
}
