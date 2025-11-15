package com.example.myapplication.presentation.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.myapplication.domain.model.TodoItem
import com.example.myapplication.presentation.ui.component.TodoItemCard
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue


@Composable
fun TodoListScreen(
    todos: State<List<TodoItem>>,
    onTodoClick: (Int) -> Unit,
    onToggle: (Int) -> Unit
) {
    val todoList by todos

    LazyColumn {
        items(
            items = todoList,
            key = { todo -> todo.id }
        ) { todo ->
            TodoItemCard(
                todo = todo,
                onClick = { onTodoClick(todo.id) },
                onCheckedChange = { onToggle(todo.id) }
            )
        }
    }
}