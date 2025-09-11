package com.sena.monitoreo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sena.monitoreo.data.model.LoginRequest
import com.sena.monitoreo.data.repository.AuthRepository
import com.sena.monitoreo.databinding.ActivityLoginBinding
import com.sena.monitoreo.ui.user.HomeUserActivity
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val repository = AuthRepository() // sin pasar Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.containerLogin) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // SharedPreferences para recordar sesión
        val prefs = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val remember = prefs.getBoolean("REMEMBER", false)
        if (remember) {
            binding.inputPhone.setText(prefs.getString("PHONE", ""))
            binding.inputPassword.setText(prefs.getString("PASSWORD", ""))
            binding.checkboxRememberMe.isChecked = true
            binding.loginButton.visibility = View.VISIBLE
        }

        // Función para mostrar u ocultar botón según inputs
        fun checkInputs() {
            val phone = binding.inputPhone.text?.toString()?.trim()
            val password = binding.inputPassword.text?.toString()?.trim()
            binding.loginButton.visibility =
                if (!phone.isNullOrEmpty() && !password.isNullOrEmpty()) View.VISIBLE
                else View.GONE

            // Ocultar error cuando el usuario escribe
            binding.tvError.visibility = View.GONE
        }

        val watcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { checkInputs() }
            override fun afterTextChanged(s: android.text.Editable?) {}
        }

        binding.inputPhone.addTextChangedListener(watcher)
        binding.inputPassword.addTextChangedListener(watcher)

        // Login
        binding.loginButton.setOnClickListener {
            val phone = binding.inputPhone.text.toString()
            val password = binding.inputPassword.text.toString()

            // Guardar en SharedPreferences si se seleccionó "Recordarme"
            if (binding.checkboxRememberMe.isChecked) {
                with(prefs.edit()) {
                    putString("PHONE", phone)
                    putString("PASSWORD", password)
                    putBoolean("REMEMBER", true)
                    apply()
                }
            } else {
                with(prefs.edit()) {
                    clear()
                    apply()
                }
            }

            val request = LoginRequest(phone, password)
            lifecycleScope.launch {
                try {
                    Log.d("LoginActivity", "Iniciando login con: phone=$phone, password=$password")
                    val response = repository.login(request)
                    Log.d("LoginActivity", "Response recibido: $response")

                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        Log.d("LoginActivity", "Login exitoso: usuario=${user.usuario}")
                        Snackbar.make(binding.containerLogin, "Bienvenido ${user.usuario}", Snackbar.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeUserActivity::class.java))
                        finish()
                    } else {
                        val msg = response.errorBody()?.string()
                        val errorMessage = try {
                            JSONObject(msg!!).getString("error")
                        } catch (e: Exception) {
                            "Credenciales inválidas"
                        }
                        // Mostrar error visual
                        binding.tvError.text = errorMessage
                        binding.tvError.visibility = View.VISIBLE
                        Log.e("LoginActivity", "Login fallido: $errorMessage")
                    }
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error de conexión", e)
                    binding.tvError.text = "Error de conexión: ${e.message}"
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }

        // Crear cuenta
        binding.createAccountText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
