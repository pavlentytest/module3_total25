package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.presentation.ui.screen.TodoDetailScreen
import com.example.myapplication.presentation.ui.screen.TodoListScreen
import com.example.myapplication.presentation.viewmodel.TodoViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: TodoViewModel) {
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TodoListScreen(
                todos = viewModel.todos,
                onTodoClick = { id -> navController.navigate("detail/$id") },
                onToggle = viewModel::toggleTodo
            )
        }
        composable(
            "detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
            val todo = viewModel.todos.value.find { it.id == todoId }
            todo?.let {
                TodoDetailScreen(todo = it, onBack = { navController.popBackStack() })
            }
        }
    }
}