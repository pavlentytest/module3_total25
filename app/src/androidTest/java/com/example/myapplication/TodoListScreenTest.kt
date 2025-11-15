package com.example.myapplication

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import com.example.myapplication.presentation.theme.MyApplicationTheme
import com.example.myapplication.presentation.ui.screen.TodoListScreen
import com.example.myapplication.presentation.viewmodel.TodoViewModel
import org.junit.Rule
import org.junit.Test


class TodoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun displaysThreeTodosFromJson() {
        composeTestRule.setContent {
            MyApplicationTheme() {
                val repository = TodoRepositoryImpl(TodoJsonDataSource(context))
                val viewModel =
                    TodoViewModel(GetTodosUseCase(repository), ToggleTodoUseCase(repository))
                TodoListScreen(
                    todos = viewModel.todos,
                    onTodoClick = {},
                    onToggle = viewModel::toggleTodo
                )
            }
        }

        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()
        composeTestRule.onNodeWithText("Позвонить маме").assertIsDisplayed()
        composeTestRule.onNodeWithText("Сделать ДЗ по Android").assertIsDisplayed()
    }


}