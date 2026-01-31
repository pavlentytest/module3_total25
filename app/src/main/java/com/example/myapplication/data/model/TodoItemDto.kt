package com.example.myapplication.data.model

import com.example.myapplication.domain.model.TodoItem

data class TodoItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)