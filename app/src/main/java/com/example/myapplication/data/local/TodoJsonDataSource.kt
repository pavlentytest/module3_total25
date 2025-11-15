package com.example.myapplication.data.local

import android.content.Context
import com.example.myapplication.data.model.TodoItemDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class TodoJsonDataSource(private val context: Context?) {
    private val gson = Gson()

    open fun getTodos(): List<TodoItemDto> {
        if (context == null) return emptyList()

        val json = context.assets.open("todos.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<TodoItemDto>>() {}.type
        return gson.fromJson(json, type)
    }
}