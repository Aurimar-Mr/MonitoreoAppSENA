package com.sena.monitoreo.utils

// Este archivo solo define las interfaces para el manejo de los estados del motor TTS.

/**
 * Interface para manejar el resultado de la inicialización de TtsManager.
 */
interface TtsOnInitCallback {
    /**
     * Llamado cuando el motor TTS se ha inicializado.
     * @param success Verdadero si la inicialización fue exitosa, falso en caso contrario.
     */
    fun onTtsInitialized(success: Boolean)
}

/**
 * Interface para manejar el ciclo de vida de la reproducción de voz.
 */
interface TtsPlaybackListener {
    /**
     * Llamado justo antes de que el TTS comience a hablar.
     */
    fun onStartSpeaking()

    /**
     * Llamado cuando el TTS ha terminado de hablar un texto completo.
     */
    fun onDoneSpeaking()
}