package com.sena.monitoreo.data.repository

import com.sena.monitoreo.data.api.RetrofitClient
import com.sena.monitoreo.data.model.LoginRequest
import com.sena.monitoreo.data.model.LoginResponse
import com.sena.monitoreo.data.model.RegisterRequest
import com.sena.monitoreo.data.model.RegisterResponse
import retrofit2.Response

class AuthRepository {

    suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return RetrofitClient.api.register(request)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return RetrofitClient.api.login(request)
    }
}
