package com.APIDevelopmentTask.Todo.app.domain.service.impl;

import com.APIDevelopmentTask.Todo.app.application.exception.TodoNotFoundException;
import com.APIDevelopmentTask.Todo.app.application.exception.UnauthorizedAccessException;
import com.APIDevelopmentTask.Todo.app.domain.entity.Todo;
import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import com.APIDevelopmentTask.Todo.app.external.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    public Todo createTodoForUser(Todo todo, User user) {
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        logger.info("Todo created: {} for user: {}", savedTodo.getId(), user.getEmail());
        return savedTodo;
    }

    public List<Todo> getTodosForUser(User user) {
        logger.info("Fetching todos for user: {}", user.getEmail());
        Page<Todo> todosPage = todoRepository.findByUserId(user.getId(), Pageable.unpaged());
        return todosPage.getContent();
    }

    public Todo updateTodoForUser(Long id, Todo todo, User user) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> {
            logger.error("Todo not found with id: {}", id);
            throw new TodoNotFoundException(id);
        });
        if (!existingTodo.getUser().equals(user)) {
            logger.error("Unauthorized access attempt by user: {} for todo id: {}", user.getEmail(), id);
            throw new UnauthorizedAccessException();
        }
        existingTodo.setTitle(todo.getTitle());
        Todo updatedTodo = todoRepository.save(existingTodo);
        logger.info("Todo updated: {} for user: {}", updatedTodo.getId(), user.getEmail());
        return updatedTodo;
    }

    public void deleteTodoForUser(Long id, User user) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> {
            logger.error("Todo not found with id: {}", id);
            throw new TodoNotFoundException(id);
        });
        if (!existingTodo.getUser().equals(user)) {
            logger.error("Unauthorized access attempt by user: {} for todo id: {}", user.getEmail(), id);
            throw new UnauthorizedAccessException();
        }
        todoRepository.delete(existingTodo);
        logger.info("Todo deleted: {} for user: {}", id, user.getEmail());
    }
}
