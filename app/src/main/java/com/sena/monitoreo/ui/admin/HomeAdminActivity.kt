package com.sena.monitoreo.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sena.monitoreo.databinding.HeaderLayoutAdminBinding
import com.sena.monitoreo.utils.TtsManager
import com.sena.monitoreo.utils.TtsOnInitCallback
import com.sena.monitoreo.utils.TtsPlaybackListener
import com.sena.monitoreo.utils.TtsManager.Companion.UTTERANCE_ID_GENERAL

class HomeAdminActivity : AppCompatActivity(), TtsOnInitCallback, TtsPlaybackListener {

    private lateinit var binding: HeaderLayoutAdminBinding
    private var ttsManager: TtsManager? = null
    private val TAG = "HeaderLayoutAdminActivity"

    private fun getAdminNameFromDatabase(): String {
        return "Javier Pérez" // ⚠️ Reemplazar con lógica real
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HeaderLayoutAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ttsManager = TtsManager(this, this)
        ttsManager?.playbackListener = this

        // 1. ANIMACIÓN: Usamos el nombre camelCase que debe funcionar.
        binding.voiceWaveAnimation.pauseAnimation()
    }

    override fun onDestroy() {
        ttsManager?.shutdown()
        super.onDestroy()
    }

    override fun onTtsInitialized(success: Boolean) {
        if (success) {
            val adminName = getAdminNameFromDatabase()
            val greeting = "Sistema B.M.I.S. en modo administrador. Hola, $adminName."

            ttsManager?.speak(
                text = greeting,
                pitch = 1.0f,
                speed = 1.0f,
                utteranceId = UTTERANCE_ID_GENERAL
            )
        } else {
            Log.e(TAG, "El saludo por voz no se pudo reproducir. TTS Fallido.")
        }
    }
    override fun onStartSpeaking() {
        binding.voiceWaveAnimation.playAnimation()
    }

    // Coincide con fun onDoneSpeaking() de la interfaz.
    override fun onDoneSpeaking() {
        binding.voiceWaveAnimation.pauseAnimation()
    }

    // Coincide con fun onSpeechError(utteranceId: String) de la interfaz.
    override fun onSpeechError(utteranceId: String) {
        binding.voiceWaveAnimation.pauseAnimation()
        Log.e(TAG, "Error en reproducción TTS para ID: $utteranceId")
    }
}