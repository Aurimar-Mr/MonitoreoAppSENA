package com.sena.monitoreo.utils
interface TtsOnInitCallback {
    fun onTtsInitialized(success: Boolean)
}
interface TtsPlaybackListener {
    fun onStartSpeaking()
    fun onDoneSpeaking()
    fun onSpeechError(utteranceId: String)
}