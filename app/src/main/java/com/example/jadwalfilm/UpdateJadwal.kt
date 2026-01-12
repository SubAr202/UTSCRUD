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

class UpdateJadwal : AppCompatActivity() {

    private lateinit var btnUpdate: Button
    private lateinit var btnKembali: Button

    private lateinit var editId: EditText
    private lateinit var editJudul: EditText
    private lateinit var editTanggal: EditText
    private lateinit var editJam: EditText
    private lateinit var editGenre: EditText
    private lateinit var editKeterangan: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_jadwal)

        editId = findViewById(R.id.editTextId)
        editJudul = findViewById(R.id.editTextJudul)
        editTanggal = findViewById(R.id.editTextTanggal)
        editJam = findViewById(R.id.editTextJam)
        editGenre = findViewById(R.id.editTextGenre)
        editKeterangan = findViewById(R.id.editTextKeterangan)

        btnUpdate = findViewById(R.id.buttonUpdate)
        btnKembali = findViewById(R.id.buttonKembali)

        // ====== ISI DATA DARI INTENT ======
        editId.setText(intent.getStringExtra("id"))
        editId.isEnabled = false // ✅ ID tidak boleh diubah

        editJudul.setText(intent.getStringExtra("judul"))
        editTanggal.setText(intent.getStringExtra("tanggal"))
        editJam.setText(intent.getStringExtra("jam"))
        editGenre.setText(intent.getStringExtra("genre"))
        editKeterangan.setText(intent.getStringExtra("keterangan"))

        btnUpdate.setOnClickListener {

            val id = editId.text.toString()
            val judul = editJudul.text.toString().trim()
            val tanggal = editTanggal.text.toString().trim()
            val jam = editJam.text.toString().trim()
            val genre = editGenre.text.toString().trim()
            val keterangan = editKeterangan.text.toString().trim()

            if (judul.isEmpty() || tanggal.isEmpty() || jam.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.updateJadwal(
                id, judul, tanggal, jam, genre, keterangan
            ).enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateJadwal,
                            "Data berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // ✅ cukup ini
                    } else {
                        Toast.makeText(
                            this@UpdateJadwal,
                            "Gagal memperbarui data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@UpdateJadwal,
                        "Tidak bisa terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
