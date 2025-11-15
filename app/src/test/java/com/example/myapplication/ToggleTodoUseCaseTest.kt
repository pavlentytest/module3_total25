package com.example.myapplication

import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.model.TodoItemDto
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.domain.repository.TodoRepository
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleTodoUseCaseTest {

    private lateinit var repository: TodoRepository
    private lateinit var useCase: ToggleTodoUseCase

    @Before
    fun setup() {
        val inMemoryDataSource = object : TodoJsonDataSource(null) {
            override fun getTodos(): List<TodoItemDto> = listOf(
                TodoItemDto(1, "Flip", "", false)
            )
        }
        repository = TodoRepositoryImpl(inMemoryDataSource)
        useCase = ToggleTodoUseCase(repository)
    }

    @Test
    fun `toggles isCompleted`() = runTest {
        GetTodosUseCase(repository)()
        useCase(1)
        val updated = GetTodosUseCase(repository)()[0]
        assertTrue(updated.isCompleted)
    }
}