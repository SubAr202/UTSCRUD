package com.example.jadwalfilm

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jadwalfilm.api.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuatJadwal : AppCompatActivity() {

    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button
    private lateinit var editJudul: EditText
    private lateinit var editTanggal: EditText
    private lateinit var editJam: EditText
    private lateinit var editGenre: EditText
    private lateinit var editKeterangan: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_jadwal)

        editJudul = findViewById(R.id.editTextJudul)
        editTanggal = findViewById(R.id.editTextTanggal)
        editJam = findViewById(R.id.editTextJam)
        editGenre = findViewById(R.id.editTextGenre)
        editKeterangan = findViewById(R.id.editTextKeterangan)

        btnSimpan = findViewById(R.id.buttonSimpan)
        btnBatal = findViewById(R.id.buttonBatal)

        btnSimpan.setOnClickListener {

            val judul = editJudul.text.toString().trim()
            val tanggal = editTanggal.text.toString().trim()
            val jam = editJam.text.toString().trim()
            val genre = editGenre.text.toString().trim()
            val keterangan = editKeterangan.text.toString().trim()

            if (judul.isEmpty() || tanggal.isEmpty() || jam.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.insertJadwal(
                judul,
                tanggal,
                jam,
                genre,
                keterangan
            ).enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@BuatJadwal,
                            "Jadwal film berhasil disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // ✅ cukup ini
                    } else {
                        Toast.makeText(
                            this@BuatJadwal,
                            "Gagal menyimpan data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@BuatJadwal,
                        "Tidak bisa terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        btnBatal.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
