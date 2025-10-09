package com.sena.monitoreo.ui.user

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sena.monitoreo.data.model.LecturaResponse
import com.sena.monitoreo.data.model.Sensor
import com.sena.monitoreo.data.repository.SensorRepository
import com.sena.monitoreo.databinding.ActivitySensorDataBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SensorDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySensorDataBinding
    private val repository = SensorRepository()
    private val TAG = "SensorDataActivity"
    private val maxDatos = 7
    private val refreshTime = 5 * 60 * 1000L // 5 minutos en milisegundos

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySensorDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val visualizer = findViewById<LineBarVisualizer>(R.id.voiceVisualizer)
        visualizer.setColor(ContextCompat.getColor(this, R.color.black_500))
        visualizer.setDensity(70)
        visualizer.setPlayer(0) // 0 = salida de audio global del sistema

        override fun onPause() {
            super.onPause()
            val visualizer = findViewById<LineBarVisualizer>(R.id.voiceVisualizer)
            visualizer.release()
        }

        // Lanza la actualización periódica
        lifecycleScope.launch {
            while (true) {
                cargarSensoresYGraficas()
                delay(refreshTime)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun cargarSensoresYGraficas() {
        try {
            Log.d(TAG, "Iniciando carga de sensores...")
            val sensores: List<Sensor> = repository.getSensores()
            Log.d(TAG, "Sensores recibidos: $sensores")

            if (sensores.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this@SensorDataActivity, "No hay sensores disponibles", Toast.LENGTH_SHORT).show()
                }
                return
            }

            sensores.forEach { sensor ->
                Log.d(TAG, "Cargando lecturas para sensor: ${sensor.nombre} (id=${sensor.id})")
                val lecturas: List<LecturaResponse> = repository.getLecturas(sensor.id)
                Log.d(TAG, "Lecturas recibidas para sensor ${sensor.id}: $lecturas")

                if (lecturas.isEmpty()) {
                    Log.d(TAG, "No hay lecturas para sensor ${sensor.id}")
                    return@forEach
                }

                // Convertir las lecturas a Entries y limitar a maxDatos
                val entries = lecturas.map { lectura ->
                    try {
                        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        val time = LocalDateTime.parse(lectura.fecha_hora, formatter).hour.toFloat()
                        Entry(time, lectura.valor)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parseando fecha: ${lectura.fecha_hora}", e)
                        Entry(0f, lectura.valor)
                    }
                }.takeLast(maxDatos) // <-- Limita a los últimos 7 datos

                when (sensor.nombre.trim().lowercase()) {
                    "temperatura" -> setupChart(binding.cardTemperatura.cardLineChart, "Temperatura", Color.BLUE, entries)
                    "presion" -> setupChart(binding.cardPresion.cardLineChart, "Presión", Color.GREEN, entries)
                    "volumen" -> setupChart(binding.cardMq4.cardLineChart, "Gas", Color.RED, entries)
                    else -> Log.d(TAG, "Sensor desconocido: '${sensor.nombre}'")
                }

            }

        } catch (e: Exception) {
            Log.e(TAG, "Error cargando datos", e)
            runOnUiThread {
                Toast.makeText(this@SensorDataActivity, "Error al cargar datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupChart(chart: LineChart, label: String, color: Int, entries: List<Entry>) {
        val dataSet = LineDataSet(entries, label)
        dataSet.color = color
        dataSet.valueTextColor = Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 3f
        dataSet.setCircleColor(color)

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.description.isEnabled = false
        chart.legend.isEnabled = true
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.invalidate()
    }

}
