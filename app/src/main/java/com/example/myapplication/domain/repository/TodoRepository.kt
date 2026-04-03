package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodosFlow(): Flow<List<TodoItem>>
    fun toggleTodo(id: Int): Flow<Unit>
    fun deleteTodo(id: Int): Flow<Unit>
    fun addTask(task: TodoItem): Flow<Unit>
}