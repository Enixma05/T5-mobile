package com.example.pasienapiapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapiapp.R
import com.example.pasienapiapp.model.Patient

class PatientAdapter : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    private val patients = mutableListOf<Patient>()

    fun submitList(newItems: List<Patient>) {
        patients.clear()
        patients.addAll(newItems)
        notifyDataSetChanged()
    }

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTglLahir: TextView = itemView.findViewById(R.id.tvTglLahir)
        val tvJenisKelamin: TextView = itemView.findViewById(R.id.tvJenisKelamin)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoTelepon: TextView = itemView.findViewById(R.id.tvNoTelepon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val item = patients[position]

        holder.tvNama.text = "Nama: ${item.nama ?: "-"}"
        holder.tvTglLahir.text = "Tanggal Lahir: ${item.tanggal_lahir ?: "-"}"
        holder.tvJenisKelamin.text = "Jenis Kelamin: ${item.jenis_kelamin ?: "-"}"
        holder.tvAlamat.text = "Alamat: ${item.alamat ?: "-"}"
        holder.tvNoTelepon.text = "No Telepon: ${item.no_telepon ?: "-"}"
    }

    override fun getItemCount(): Int = patients.size
}