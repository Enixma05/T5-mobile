package com.example.pasienapiapp.network

import com.example.pasienapiapp.model.ApiResponse
import com.example.pasienapiapp.model.LoginData
import com.example.pasienapiapp.model.LoginRequest
import com.example.pasienapiapp.model.Patient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<LoginData>>

    @GET("pasien")
    suspend fun getPatients(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Patient>>>
}