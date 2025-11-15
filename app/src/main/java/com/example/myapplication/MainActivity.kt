package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import com.example.myapplication.presentation.navigation.AppNavGraph
import com.example.myapplication.presentation.theme.MyApplicationTheme
import com.example.myapplication.presentation.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController //for test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val jsonDataSource = TodoJsonDataSource(this)
        val repository = TodoRepositoryImpl(jsonDataSource)
        val getTodosUseCase = GetTodosUseCase(repository)
        val toggleTodoUseCase = ToggleTodoUseCase(repository)
        val viewModel = TodoViewModel(getTodosUseCase, toggleTodoUseCase)

        setContent {
            val navController = rememberNavController()
            this@MainActivity.navController = navController //for test
            MyApplicationTheme() {
                Scaffold { paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                    ) {
                        AppNavGraph(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

