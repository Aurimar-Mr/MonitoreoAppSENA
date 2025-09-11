package com.sena.monitoreo.data.api

import com.sena.monitoreo.data.model.LoginRequest
import com.sena.monitoreo.data.model.LoginResponse
import com.sena.monitoreo.data.model.RegisterRequest
import com.sena.monitoreo.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
