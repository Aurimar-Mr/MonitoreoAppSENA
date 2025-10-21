package com.sena.monitoreo.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sena.monitoreo.databinding.HeaderLayoutUserBinding
import com.sena.monitoreo.utils.TtsManager
import com.sena.monitoreo.utils.TtsOnInitCallback
import com.sena.monitoreo.utils.TtsPlaybackListener
import com.sena.monitoreo.utils.TtsManager.Companion.UTTERANCE_ID_GENERAL

class HomeUserActivity : AppCompatActivity(), TtsOnInitCallback, TtsPlaybackListener {

    private lateinit var binding: HeaderLayoutUserBinding
    private var ttsManager: TtsManager? = null
    private val TAG = "HomeUserActivity"
    private fun getUserNameFromDatabase(): String {
        return "Andrea López"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HeaderLayoutUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ttsManager = TtsManager(this, this)
        ttsManager?.playbackListener = this
        binding.voiceWaveAnimation.pauseAnimation()
    }

    override fun onTtsInitialized(success: Boolean) {
        if (success) {
            val userName = getUserNameFromDatabase()
            val welcomeMessage = "Bienvenido $userName. Su sistema de monitoreo está listo."
            ttsManager?.speak(welcomeMessage, 1.0f, 1.0f)
        } else {
            Log.e(TAG, "TTS no se pudo inicializar.")
        }
    }

    override fun onStartSpeaking() {
        Log.i(TAG, "TTS ha empezado a hablar.")
        binding.voiceWaveAnimation.resumeAnimation()
    }

    override fun onDoneSpeaking() {
        Log.i(TAG, "TTS ha terminado de hablar.")
        binding.voiceWaveAnimation.pauseAnimation()
    }

    override fun onSpeechError(utteranceId: String) {
        Log.e(TAG, "Error de voz para ID: $utteranceId")
        binding.voiceWaveAnimation.pauseAnimation()
    }

    override fun onDestroy() {
        ttsManager?.shutdown()
        super.onDestroy()
    }
}