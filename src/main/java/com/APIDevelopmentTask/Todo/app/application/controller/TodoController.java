package com.APIDevelopmentTask.Todo.app.application.controller;

import com.APIDevelopmentTask.Todo.app.domain.entity.Todo;
import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import com.APIDevelopmentTask.Todo.app.domain.service.impl.TodoService;
import com.APIDevelopmentTask.Todo.app.domain.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, @AuthenticationPrincipal User user) {
        logger.info("Creating todo for user: {}", user.getEmail());
        return ResponseEntity.ok(todoService.createTodoForUser(todo, user));
    }

    @GetMapping
    public ResponseEntity<?> getTodos(@AuthenticationPrincipal User user) {
        logger.info("Fetching todos for user: {}", user.getEmail());
        return ResponseEntity.ok(todoService.getTodosForUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo todo, @AuthenticationPrincipal User user) {
        logger.info("Updating todo with id: {} for user: {}", id, user.getEmail());
        return ResponseEntity.ok(todoService.updateTodoForUser(id, todo, user));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal User user) {
        logger.info("Deleting todo with id: {} for user: {}", id, user.getEmail());
        todoService.deleteTodoForUser(id, user);
        return ResponseEntity.noContent().build();
    }

}

