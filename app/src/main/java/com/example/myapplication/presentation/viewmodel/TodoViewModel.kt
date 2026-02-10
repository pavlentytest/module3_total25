package com.example.myapplication.presentation.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.TodoApplication
import com.example.myapplication.data.preferences.PreferencesKeys
import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.launch
import com.example.myapplication.domain.usecase.AddTodoUseCase
import com.example.myapplication.domain.usecase.DeleteTodoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.prefs.Preferences

class TodoViewModel(
    private val context: Context,
    private val getTodosUseCase: GetTodosUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val dataStore = (context.applicationContext as TodoApplication).dataStore

    private val _useCompletedColor = MutableStateFlow(false)
    val useCompletedColor: StateFlow<Boolean> = _useCompletedColor.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                _useCompletedColor.value = preferences[PreferencesKeys.TASK_COMPLETED_COLOR] ?: false
            }
        }
    }

    val todos: StateFlow<List<TodoItem>> = getTodosUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun toggleCompletedColor() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.TASK_COMPLETED_COLOR] = !(_useCompletedColor.value)
            }
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
        }
    }

    fun addTask(title: String, description: String?) {
        viewModelScope.launch {
            val newTask = TodoItem(
                id = 0,  // id сгенерируется в Room
                title = title,
                description = description,
                isCompleted = false
            )
            addTodoUseCase(newTask)
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            deleteTodoUseCase(id)
        }
    }
}