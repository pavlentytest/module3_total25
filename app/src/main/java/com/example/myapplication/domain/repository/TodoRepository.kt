package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodosFlow(): Flow<List<TodoItem>>
    suspend fun toggleTodo(id: Int)
    suspend fun addTask(task: TodoItem)
    suspend fun deleteTodo(id: Int)
}