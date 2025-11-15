package com.example.myapplication.data.repository

import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.model.TodoItemDto
import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.repository.TodoRepository

class TodoRepositoryImpl(
    private val jsonDataSource: TodoJsonDataSource
) : TodoRepository {

    private val todoCache = mutableListOf<TodoItem>()

    override suspend fun getTodos(): List<TodoItem> {
        if (todoCache.isEmpty()) {
            todoCache.addAll(jsonDataSource.getTodos().map { it.toDomain() })
        }
        return todoCache.toList()
    }

    override suspend fun toggleTodo(id: Int) {
        val index = todoCache.indexOfFirst { it.id == id }
        if (index != -1) {
            todoCache[index] = todoCache[index].copy(isCompleted = !todoCache[index].isCompleted)
        }
    }
}
private fun TodoItemDto.toDomain() = TodoItem(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)