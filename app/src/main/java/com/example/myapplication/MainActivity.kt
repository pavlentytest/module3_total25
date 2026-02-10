package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.TodoDatabase
import com.example.myapplication.data.local.TodoJsonDataSource
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.domain.usecase.AddTodoUseCase
import com.example.myapplication.domain.usecase.DeleteTodoUseCase
import com.example.myapplication.domain.usecase.GetTodosUseCase
import com.example.myapplication.domain.usecase.ToggleTodoUseCase
import com.example.myapplication.presentation.navigation.AppNavGraph
import com.example.myapplication.presentation.theme.MyApplicationTheme
import com.example.myapplication.presentation.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController //for test
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        val database = TodoDatabase.getDatabase(this)
        val todoDao = database.todoDao()

        val repository = TodoRepositoryImpl(
            jsonDataSource = TodoJsonDataSource(this),
            todoDao = todoDao
        )

        val getTodosUseCase = GetTodosUseCase(repository)
        val toggleTodoUseCase = ToggleTodoUseCase(repository)
        val addTodoUseCase = AddTodoUseCase(repository)
        val deleteTodoUseCase = DeleteTodoUseCase(repository)

        val viewModel = TodoViewModel(
            applicationContext,
            getTodosUseCase,
            toggleTodoUseCase,
            addTodoUseCase,
            deleteTodoUseCase
        )

        setContent {
            val navController = rememberNavController()
            this@MainActivity.navController = navController //for test
            MyApplicationTheme {
                val useCompletedColor by viewModel.useCompletedColor.collectAsStateWithLifecycle()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Todo List") },
                            actions = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Цвет завершённых", modifier = Modifier.padding(end = 8.dp))
                                    Switch(
                                        checked = useCompletedColor,
                                        onCheckedChange = { viewModel.toggleCompletedColor() }
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("add")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Добавить задачу"
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AppNavGraph(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

