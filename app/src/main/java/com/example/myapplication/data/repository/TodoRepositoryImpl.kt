package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.local.dao.TodoDao
import com.example.myapplication.data.local.entity.TodoEntity
import com.example.myapplication.data.model.TodoItemDto
import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface TodoRepository {
    fun getTodosFlow(): Flow<List<TodoItem>>
    suspend fun toggleTodo(id: Int)
    suspend fun addTask(task: TodoItem)
}

class TodoRepositoryImpl(
    private val jsonDataSource: TodoJsonDataSource,
    private val todoDao: TodoDao
) : TodoRepository {

    private val todoCache = mutableListOf<TodoItem>()

    override fun getTodosFlow(): Flow<List<TodoItem>> = todoDao.getAllTodos()
        .map { entities ->
            entities.map { it.toDomain() }
        }
        .onStart {
            // Миграция из JSON один раз при первом обращении
            if (todoDao.getCount() == 0) {
                val jsonTodos = jsonDataSource.getTodos().map { it.toDomain() }
                todoDao.insertAll(jsonTodos.map { it.toEntity() })
            }
        }

    override fun addTask(task: TodoItem): Flow<Unit> = flow {
        val entity = task.toEntity().copy(id = 0)
        todoDao.insert(entity)
        emit(Unit)
    }.flowOn(Dispatchers.IO)
        .catch { e ->
            throw e
        }

    override fun toggleTodo(id: Int): Flow<Unit> = flow {
        val entity = todoDao.getTodoById(id)
        todoDao.update(entity.copy(isCompleted = !entity.isCompleted))
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun deleteTodo(id: Int): Flow<Unit> = flow {
        todoDao.deleteById(id)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

}

// Маппер из DTO (JSON) в Domain
fun TodoItemDto.toDomain() = TodoItem(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

// Маппер из Entity (Room) в Domain
fun TodoEntity.toDomain() = TodoItem(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

// Маппер из Domain в Entity (для сохранения в Room)
fun TodoItem.toEntity() = TodoEntity(
    id = id,
    title = title,
    description = description ?: "",
    isCompleted = isCompleted
)