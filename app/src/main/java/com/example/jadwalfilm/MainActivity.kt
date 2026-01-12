package com.example.jadwalfilm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jadwalfilm.api.ApiClient
import com.example.jadwalfilm.model.JadwalFilm
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var dataFilm: List<JadwalFilm> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        val btnTambah = findViewById<Button>(R.id.buttonAddFilm)

        btnTambah.setOnClickListener {
            startActivity(Intent(this, BuatJadwal::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList() // ✅ cukup di sini
    }

    private fun refreshList() {
        ApiClient.instance.getJadwal()
            .enqueue(object : Callback<List<JadwalFilm>> {

                override fun onResponse(
                    call: Call<List<JadwalFilm>>,
                    response: Response<List<JadwalFilm>>
                ) {
                    if (!response.isSuccessful) {
                        Log.e("API", "Response error: ${response.code()}")
                        return
                    }

                    dataFilm = response.body() ?: emptyList()

                    if (dataFilm.isEmpty()) {
                        listView.adapter = null
                        return
                    }

                    val daftarJudul = dataFilm.map { it.judul }

                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        daftarJudul
                    )

                    listView.adapter = adapter

                    listView.setOnItemClickListener { _, _, position, _ ->
                        showActionDialog(dataFilm[position])
                    }
                }

                override fun onFailure(call: Call<List<JadwalFilm>>, t: Throwable) {
                    Log.e("API", "Gagal mengambil data", t)
                }
            })
    }

    private fun showActionDialog(film: JadwalFilm) {
        val dialogItems = arrayOf("Lihat Jadwal", "Update Jadwal", "Hapus Jadwal")

        AlertDialog.Builder(this)
            .setTitle("Pilih Aksi")
            .setItems(dialogItems) { _, which ->
                when (which) {
                    0 -> {
                        val i = Intent(this, LihatJadwal::class.java)
                        i.putExtra("id", film.id)
                        startActivity(i)
                    }
                    1 -> {
                        val i = Intent(this, UpdateJadwal::class.java)
                        i.putExtra("id", film.id)
                        i.putExtra("judul", film.judul)
                        i.putExtra("tanggal", film.tanggal)
                        i.putExtra("jam", film.jam)
                        i.putExtra("genre", film.genre)
                        i.putExtra("keterangan", film.keterangan)
                        startActivity(i)
                    }
                    2 -> {
                        konfirmasiHapus(film.id)
                    }
                }
            }
            .show()
    }

    private fun konfirmasiHapus(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data")
            .setMessage("Yakin ingin menghapus jadwal ini?")
            .setPositiveButton("Ya") { _, _ ->
                hapusData(id)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun hapusData(id: String) {
        ApiClient.instance.deleteJadwal(
            id = id // proc otomatis "delete"
        ).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    refreshList()
                } else {
                    Log.e("API", "Delete gagal: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", "Gagal menghapus data", t)
            }
        })
    }

}
