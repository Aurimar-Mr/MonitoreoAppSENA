package com.sena.monitoreo.data.repository

import com.sena.monitoreo.data.api.SensorApi
import com.sena.monitoreo.data.model.LecturaResponse
import com.sena.monitoreo.data.model.Sensor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SensorRepository {

    private val api: SensorApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/api/") // Cambia a tu IP/puerto del backend
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(SensorApi::class.java)
    }

    suspend fun getSensores(): List<Sensor> {
        val response = api.getSensores()
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    suspend fun getLecturas(sensorId: Int): List<LecturaResponse> {
        val response = api.getLecturas(sensorId)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
}
