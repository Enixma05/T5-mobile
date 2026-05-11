package com.example.pasienapiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pasienapiapp.model.ApiResponse
import com.example.pasienapiapp.model.LoginData
import com.example.pasienapiapp.model.LoginRequest
import com.example.pasienapiapp.network.RetrofitClient
import com.example.pasienapiapp.utils.SessionManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        val savedToken = sessionManager.getToken()
        if (!savedToken.isNullOrBlank()) {
            startActivity(Intent(this, PasienActivity::class.java))
            finish()
            return
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val password = etPassword.text?.toString()?.trim().orEmpty()

        if (email.isBlank()) {
            tvError.text = "Email tidak boleh kosong"
            return
        }

        if (password.isBlank()) {
            tvError.text = "Password tidak boleh kosong"
            return
        }

        tvError.text = ""
        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(email = email, password = password)
                )

                if (response.isSuccessful) {
                    val body: ApiResponse<LoginData>? = response.body()
                    val token = body?.data?.token
                    val userName = body?.data?.user?.name

                    if (!token.isNullOrBlank()) {
                        sessionManager.saveToken(token)
                        sessionManager.saveUserName(userName ?: "User")

                        startActivity(Intent(this@LoginActivity, PasienActivity::class.java))
                        finish()
                    } else {
                        tvError.text = body?.message ?: "Token tidak ditemukan"
                    }
                } else {
                    tvError.text = "Login gagal: ${response.code()}"
                }
            } catch (e: Exception) {
                tvError.text = "Gagal terhubung ke server: ${e.message}"
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
        etEmail.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
    }
}