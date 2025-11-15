package com.example.myapplication

import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.model.TodoItemDto
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import com.example.myapplication.presentation.viewmodel.TodoViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class TodoViewModelTest {

    private lateinit var viewModel: TodoViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val inMemoryDataSource = object : TodoJsonDataSource(null) {
            override fun getTodos(): List<TodoItemDto> = listOf(
                TodoItemDto(1, "Test", "", false)
            )
        }
        val repository = TodoRepositoryImpl(inMemoryDataSource)
        viewModel = TodoViewModel(GetTodosUseCase(repository), ToggleTodoUseCase(repository))
    }

    @Test
    fun `loads todos on init`() = runTest {
        advanceUntilIdle()
        val todos = viewModel.todos.value
        assertEquals(1, todos.size)
        assertEquals("Test", todos[0].title)
    }

    @Test
    fun `toggle updates state`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.todos.value[0].isCompleted)
        viewModel.toggleTodo(1)
        advanceUntilIdle()
        assertTrue(viewModel.todos.value[0].isCompleted)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}