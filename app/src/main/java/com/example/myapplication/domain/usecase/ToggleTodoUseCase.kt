package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class ToggleTodoUseCase(private val repository: TodoRepository) {
    operator fun invoke(id: Int): Flow<Unit> {
        return repository.toggleTodo(id)
    }
}