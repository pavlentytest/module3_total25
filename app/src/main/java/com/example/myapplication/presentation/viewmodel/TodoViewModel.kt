package com.example.myapplication.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase
) : ViewModel() {

    private val _todos = mutableStateOf<List<TodoItem>>(emptyList())
    val todos: State<List<TodoItem>> = _todos


    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            _todos.value = getTodosUseCase()
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
            _todos.value = getTodosUseCase()
        }
    }
}