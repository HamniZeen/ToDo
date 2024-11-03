package com.APIDevelopmentTask.Todo.app.application.controller;

import com.APIDevelopmentTask.Todo.app.domain.entity.Todo;
import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import com.APIDevelopmentTask.Todo.app.domain.service.impl.TodoService;
import com.APIDevelopmentTask.Todo.app.domain.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
    }

    @Test
    void createTodo_TodoCreated_ReturnsTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        when(todoService.createTodoForUser(any(Todo.class), eq(user))).thenReturn(todo);

        ResponseEntity<?> response = todoController.createTodo(todo, user);

        assertEquals(ResponseEntity.ok(todo), response);
        verify(todoService, times(1)).createTodoForUser(any(Todo.class), eq(user));
    }

    @Test
    void getTodos_TodosFetched_ReturnsTodos() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        when(todoService.getTodosForUser(user)).thenReturn(Collections.singletonList(todo));

        ResponseEntity<?> response = todoController.getTodos(user);

        assertEquals(ResponseEntity.ok(Collections.singletonList(todo)), response);
        verify(todoService, times(1)).getTodosForUser(user);
    }

    @Test
    void updateTodo_TodoUpdated_ReturnsUpdatedTodo() {
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTitle("Updated Todo");

        when(todoService.updateTodoForUser(eq(todoId), any(Todo.class), eq(user))).thenReturn(todo);

        ResponseEntity<?> response = todoController.updateTodo(todoId, todo, user);

        assertEquals(ResponseEntity.ok(todo), response);
        verify(todoService, times(1)).updateTodoForUser(eq(todoId), any(Todo.class), eq(user));
    }

    @Test
    void deleteTodo_TodoDeleted_ReturnsNoContent() {
        Long todoId = 1L;

        ResponseEntity<?> response = todoController.deleteTodo(todoId, user);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(todoService, times(1)).deleteTodoForUser(todoId, user);
    }
}
