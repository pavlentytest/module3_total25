package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class AddTodoUseCase(private val repository: TodoRepository) {

    operator fun invoke(task: TodoItem): Flow<Unit> {
        return repository.addTask(task)
    }
}