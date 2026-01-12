package com.example.jadwalfilm

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jadwalfilm.api.ApiClient
import com.example.jadwalfilm.model.JadwalFilm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LihatJadwal : AppCompatActivity() {

    private lateinit var btnKembali: Button
    private lateinit var textId: TextView
    private lateinit var textJudul: TextView
    private lateinit var textTanggal: TextView
    private lateinit var textGenre: TextView
    private lateinit var textSinopsis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_jadwal)

        // Hubungkan layout
        textId = findViewById(R.id.textViewId)
        textJudul = findViewById(R.id.textViewJudul)
        textTanggal = findViewById(R.id.textViewTanggal)
        textGenre = findViewById(R.id.textViewGenre)
        textSinopsis = findViewById(R.id.textViewSinopsis)
        btnKembali = findViewById(R.id.buttonKembali)

        val id = intent.getStringExtra("id")
        if (id.isNullOrEmpty()) {
            textJudul.text = "ID tidak ditemukan"
            return
        }

        // Panggil API
        ApiClient.instance.getJadwal()
            .enqueue(object : Callback<List<JadwalFilm>> {

                override fun onResponse(
                    call: Call<List<JadwalFilm>>,
                    response: Response<List<JadwalFilm>>
                ) {
                    val data = response.body() ?: return

                    val film = data.find { it.id == id }

                    if (film == null) {
                        textJudul.text = "Data tidak ditemukan"
                        return
                    }

                    textId.text = film.id
                    textJudul.text = film.judul
                    textTanggal.text = "${film.tanggal} | ${film.jam}"
                    textGenre.text = film.genre
                    textSinopsis.text = film.keterangan
                }

                override fun onFailure(call: Call<List<JadwalFilm>>, t: Throwable) {
                    textJudul.text = "Gagal mengambil data"
                }
            })

        btnKembali.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
