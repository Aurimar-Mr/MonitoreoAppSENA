package com.sena.monitoreo.utils

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale


class TtsManager(
    private val context: Context,
    private val initCallback: TtsOnInitCallback
) : TextToSpeech.OnInitListener { // Implementa la interfaz de Android TTS

    private var tts: TextToSpeech? = null
    var playbackListener: TtsPlaybackListener? = null
    var isInitialized: Boolean = false
    var currentUtteranceId: String? = null

    companion object {
        private const val TAG = "TtsManager"
        const val UTTERANCE_ID_GENERAL = "general_speech"
    }

    init {
        // Inicializa el motor TTS de Android
        tts = TextToSpeech(context, this)
    }

    // Método de la interfaz TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Intenta establecer el idioma a Español
            val result = tts?.setLanguage(Locale("es", "ES"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "El idioma (Español) no es soportado o faltan datos.")
                isInitialized = false
            } else {
                Log.i(TAG, "Inicialización de TTS exitosa.")
                setupUtteranceListener()
                isInitialized = true
            }
        } else {
            Log.e(TAG, "Fallo en la inicialización de TTS. Estado: $status")
            isInitialized = false
        }
        initCallback.onTtsInitialized(isInitialized)
    }

    private fun setupUtteranceListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Uso de UtteranceProgressListener para versiones modernas
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                // Sobrescribe el método onStart de UtteranceProgressListener
                override fun onStart(utteranceId: String) {
                    currentUtteranceId = utteranceId
                    playbackListener?.onStartSpeaking()
                }

                // Sobrescribe el método onDone de UtteranceProgressListener
                override fun onDone(utteranceId: String) {
                    currentUtteranceId = null
                    playbackListener?.onDoneSpeaking()
                }

                override fun onError(utteranceId: String) {
                    currentUtteranceId = null
                    playbackListener?.onSpeechError(utteranceId)
                    Log.e(TAG, "TTS Error: $utteranceId")
                }

                // Método de UtteranceProgressListener para API 21+
                override fun onError(utteranceId: String, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                    currentUtteranceId = null
                    playbackListener?.onSpeechError(utteranceId)
                    Log.e(TAG, "TTS Error ($errorCode): $utteranceId")
                }
            })
        } else {
            // Uso de la interfaz obsoleta para versiones antiguas
            @Suppress("DEPRECATION")
            tts?.setOnUtteranceCompletedListener {
                playbackListener?.onDoneSpeaking()
                currentUtteranceId = null
            }
        }
    }

    fun speak(text: String, pitch: Float, speed: Float, utteranceId: String = UTTERANCE_ID_GENERAL) {
        if (!isInitialized) {
            Log.e(TAG, "TTS no inicializado. No se puede hablar.")
            return
        }

        tts?.setPitch(pitch)
        tts?.setSpeechRate(speed)

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)

        // Método de Android para hablar
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun shutdown() {
        // Métodos de Android para detener y apagar
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}