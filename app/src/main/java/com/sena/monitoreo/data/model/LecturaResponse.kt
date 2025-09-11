package com.sena.monitoreo.data.model

data class LecturaResponse(
    val fecha_hora: String,
    val sensor_id: Int,
    val valor: Float
)
