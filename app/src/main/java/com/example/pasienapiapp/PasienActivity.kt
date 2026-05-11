package com.example.pasienapiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapiapp.adapter.PatientAdapter
import com.example.pasienapiapp.model.ApiResponse
import com.example.pasienapiapp.model.Patient
import com.example.pasienapiapp.network.RetrofitClient
import com.example.pasienapiapp.utils.SessionManager
import kotlinx.coroutines.launch

class PasienActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvErrorPasien: TextView
    private lateinit var progressBarPasien: ProgressBar
    private lateinit var rvPatients: RecyclerView
    private lateinit var btnLogout: Button

    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: PatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien)

        sessionManager = SessionManager(this)

        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            redirectToLogin()
            return
        }

        initViews()
        setupRecyclerView()
        setupUserInfo()
        setupLogoutButton()

        loadPatients()
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvErrorPasien = findViewById(R.id.tvErrorPasien)
        progressBarPasien = findViewById(R.id.progressBarPasien)
        rvPatients = findViewById(R.id.rvPatients)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupRecyclerView() {
        adapter = PatientAdapter()

        rvPatients.layoutManager = LinearLayoutManager(this)
        rvPatients.adapter = adapter
    }

    private fun setupUserInfo() {
        val userName = sessionManager.getUserName() ?: "User"
        tvUserName.text = "Halo, $userName"
    }

    private fun setupLogoutButton() {

        btnLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->

                    sessionManager.clear()

                    redirectToLogin()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun loadPatients() {

        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            tvErrorPasien.text = "Token tidak tersedia"
            return
        }

        setLoading(true)
        tvErrorPasien.text = ""

        lifecycleScope.launch {

            try {

                val response = RetrofitClient.apiService.getPatients(
                    "Bearer $token"
                )

                if (response.isSuccessful) {

                    val body: ApiResponse<List<Patient>>? = response.body()

                    val patients = body?.data ?: emptyList()

                    adapter.submitList(patients)

                    if (patients.isEmpty()) {
                        tvErrorPasien.text = "Data pasien kosong"
                    }

                } else {

                    tvErrorPasien.text =
                        "Gagal mengambil data pasien (${response.code()})"
                }

            } catch (e: Exception) {

                tvErrorPasien.text =
                    "Terjadi kesalahan: ${e.message}"

            } finally {

                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {

        progressBarPasien.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun redirectToLogin() {

        val intent = Intent(this, LoginActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
}