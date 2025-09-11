package com.sena.monitoreo.data.api

import com.sena.monitoreo.data.model.LecturaResponse
import com.sena.monitoreo.data.model.Sensor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SensorApi {

    @GET("sensors")
    suspend fun getSensores(): Response<List<Sensor>>

    @GET("lecturas/{sensor_id}")
    suspend fun getLecturas(@Path("sensor_id") sensorId: Int): Response<List<LecturaResponse>>
}
