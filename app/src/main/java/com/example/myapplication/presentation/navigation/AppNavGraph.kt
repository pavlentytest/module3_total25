package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.presentation.ui.screen.TodoAddScreen
import com.example.myapplication.presentation.ui.screen.TodoDetailScreen
import com.example.myapplication.presentation.ui.screen.TodoListScreen
import com.example.myapplication.presentation.viewmodel.TodoViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: TodoViewModel) {
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TodoListScreen(
                todos = viewModel.todos.collectAsState().value,
                onTodoClick = { id -> navController.navigate("detail/$id") },
                onToggle = viewModel::toggleTodo,
                onDelete = { item -> viewModel.deleteTodo(item.id) },
                useCompletedColor = viewModel.useCompletedColor.collectAsState().value
            )
        }

        composable(
            "detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
            val todo = viewModel.todos.collectAsState().value.find { it.id == todoId }
            todo?.let {
                TodoDetailScreen(todo = it, onBack = { navController.popBackStack() })
            }
        }

        composable("add") {
            TodoAddScreen(
                onSave = { title, description ->
                    viewModel.addTask(title, description)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}