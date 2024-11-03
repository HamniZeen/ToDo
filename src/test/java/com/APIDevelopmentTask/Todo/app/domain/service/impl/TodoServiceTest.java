package com.APIDevelopmentTask.Todo.app.domain.service.impl;

import com.APIDevelopmentTask.Todo.app.application.exception.TodoNotFoundException;
import com.APIDevelopmentTask.Todo.app.application.exception.UnauthorizedAccessException;
import com.APIDevelopmentTask.Todo.app.domain.entity.Todo;
import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import com.APIDevelopmentTask.Todo.app.external.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    private User user;
    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        todo.setUser(user);
    }

    @Test
    void createTodoForUser_TodoCreated_ReturnsTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo createdTodo = todoService.createTodoForUser(todo, user);

        assertEquals(todo, createdTodo);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void getTodosForUser_TodosFetched_ReturnsTodos() {
        Page<Todo> todosPage = mock(Page.class);
        when(todosPage.getContent()).thenReturn(Collections.singletonList(todo));
        when(todoRepository.findByUserId(user.getId(), Pageable.unpaged())).thenReturn(todosPage);

        var todos = todoService.getTodosForUser(user);

        assertEquals(1, todos.size());
        assertEquals(todo, todos.get(0));
        verify(todoRepository, times(1)).findByUserId(user.getId(), Pageable.unpaged());
    }

    @Test
    void updateTodoForUser_TodoUpdated_ReturnsUpdatedTodo() {
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Todo");

        Todo result = todoService.updateTodoForUser(todo.getId(), updatedTodo, user);

        assertEquals(todo.getId(), result.getId());
        assertEquals(todo.getTitle(), result.getTitle());
        verify(todoRepository, times(1)).findById(todo.getId());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void updateTodoForUser_TodoNotFound_ThrowsException() {
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () ->
                todoService.updateTodoForUser(todo.getId(), todo, user)
        );
        verify(todoRepository, times(1)).findById(todo.getId());
    }

    @Test
    void updateTodoForUser_UnauthorizedAccess_ThrowsException() {
        User otherUser = new User();
        otherUser.setId(2L);
        todo.setUser(otherUser);
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));

        assertThrows(UnauthorizedAccessException.class, () ->
                todoService.updateTodoForUser(todo.getId(), todo, user)
        );
        verify(todoRepository, times(1)).findById(todo.getId());
    }

    @Test
    void deleteTodoForUser_TodoDeleted() {
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));

        todoService.deleteTodoForUser(todo.getId(), user);

        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    void deleteTodoForUser_TodoNotFound_ThrowsException() {
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () ->
                todoService.deleteTodoForUser(todo.getId(), user)
        );
        verify(todoRepository, times(1)).findById(todo.getId());
    }

    @Test
    void deleteTodoForUser_UnauthorizedAccess_ThrowsException() {
        User otherUser = new User();
        otherUser.setId(2L);
        todo.setUser(otherUser);
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));

        assertThrows(UnauthorizedAccessException.class, () ->
                todoService.deleteTodoForUser(todo.getId(), user)
        );
        verify(todoRepository, times(1)).findById(todo.getId());
    }
}
