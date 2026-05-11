package com.example.pasienapiapp.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
