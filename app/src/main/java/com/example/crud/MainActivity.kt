//nama file project
package com.example.aplikasicrudfilm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    // 1. Deklarasi Variabel
    // List Film sebagai sumber data utama
    private val movieList = ArrayList<Film>()
    // View
    private lateinit var listView: ListView
    private lateinit var fabAddFilm: FloatingActionButton
    // Adapter Kustom
    private lateinit var filmAdapter: FilmAdapter
    // Variabel untuk ID Film terakhir yang digunakan
    private var lastId = 2 // Dimulai dari 2 karena data dummy memiliki ID 1 dan 
    // 2. Launcher untuk Menambah Film (CREATE)
    private val addFilmLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                // Ambil data dari TambahFilmActivity
                val nama = data.getStringExtra("EXTRA_NAMA") ?: ""
                val tahun = data.getIntExtra("EXTRA_TAHUN", 0)
                val genre = data.getStringExtra("EXTRA_GENRE") ?: ""
                val rumahProduksi = data.getStringExtra("EXTRA_RUMAH_PRODUKSI") ?: ""
                // Tentukan ID baru
                lastId++
                val newFilm = Film(lastId, nama, tahun, genre, rumahProduksi)
                // Tambahkan data baru
                movieList.add(newFilm)
                // Refresh list view
                filmAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Film '$nama' berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 3. Launcher untuk Edit/Lihat Detail/Hapus Film (READ Detail, UPDATE, DELETE)
    private val editFilmLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val action = data?.getStringExtra("ACTION_TYPE")

            if (data != null) {
                val idToActOn = data.getIntExtra("RESULT_ID", -1)
                // Cari indeks film berdasarkan ID
                val index = movieList.indexOfFirst { it.id == idToActOn }

                if (index != -1) {
                    when (action) {
                        "UPDATE" -> {
                            // Logika UPDATE data
                            val filmToUpdate = movieList[index]
                            filmToUpdate.namaFilm = data.getStringExtra("RESULT_NAMA") ?: ""
                            filmToUpdate.tahunRilis = data.getIntExtra("RESULT_TAHUN", 0)
                            filmToUpdate.genre = data.getStringExtra("RESULT_GENRE") ?: ""
                            filmToUpdate.rumahProduksi = data.getStringExtra("RESULT_RUMAH_PRODUKSI") ?: ""
                            Toast.makeText(this, "Film '${filmToUpdate.namaFilm}' berhasil diubah!", Toast.LENGTH_SHORT).show()
                        }
                        "DELETE" -> {
                            // Logika DELETE data
                            movieList.removeAt(index)
                            Toast.makeText(this, "Film berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Refresh list view setelah update/delete
                    filmAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    // 4. Lifecycle onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hubungkan View
        listView = findViewById(R.id.movie_list_view)
        fabAddFilm = findViewById(R.id.fab_add_film)

        // Inisialisasi Data dan Adapter
        addInitialData()
        setupListViewAdapter()

        // 5. Listener Tambah Film (CREATE)
        fabAddFilm.setOnClickListener {
            val intent = Intent(this, TambahFilmActivity::class.java)
            addFilmLauncher.launch(intent)
        }

        // 6. Listener Klik Item (READ Detail / UPDATE / DELETE)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFilm = movieList[position]

            // Kirim data film yang dipilih ke DetailEditFilmActivity
            val intent = Intent(this, DetailEditFilmActivity::class.java).apply {
                // Semua properti film dikirim
                putExtra("EXTRA_ID", selectedFilm.id)
                putExtra("EXTRA_NAMA", selectedFilm.namaFilm)
                putExtra("EXTRA_TAHUN", selectedFilm.tahunRilis)
                putExtra("EXTRA_GENRE", selectedFilm.genre)
                putExtra("EXTRA_RUMAH_PRODUKSI", selectedFilm.rumahProduksi)
            }
            editFilmLauncher.launch(intent)
        }
    }
    // 7. Fungsi untuk Data Dummy Awal
    private fun addInitialData() {
        movieList.add(Film(1, "The Avenger", 2012, "Action", "Marvel"))
        movieList.add(Film(2, "Inception", 2010, "Sci-Fi", "Warner Bros"))
        // lastId sudah diset ke 2 di deklarasi
    }
    // 8. Fungsi untuk Menyiapkan Adapter
    private fun setupListViewAdapter() {
        // Menggunakan Adapter Kustom yang dibuat sebelumnya (FilmAdapter.kt)
        filmAdapter = FilmAdapter(this, movieList)
        listView.adapter = filmAdapter
    }
}
