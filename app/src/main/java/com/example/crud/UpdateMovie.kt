package com.example.crud

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud.databinding.ActivityUpdateMovieBinding

class UpdateMovie : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateMovieBinding
    private lateinit var db: DatabaseHelper
    private var movieId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Ambil ID dari intent
        movieId = intent.getStringExtra("movieId").toString()

        // Ambil data movie berdasarkan ID
        val movie = db.getMovieById(movieId)

        // Tampilkan datanya di form
        if (movie != null) {
            binding.editIdEditText.setText(movie.id)
            binding.editJudulEditText.setText(movie.judul)
            binding.editGenreEditText.setText(movie.genre)
            binding.editTahunRilisText.setText(movie.tahunRilis)
            binding.editRumahProduksi.setText(movie.rumahProduksi)
        }

        // Tombol simpan perubahan
        binding.editSaveButton.setOnClickListener {
            val newId = binding.editIdEditText.text.toString()
            val newJudul = binding.editJudulEditText.text.toString()
            val newGenre = binding.editGenreEditText.text.toString()
            val newTahunRilis = binding.editTahunRilisText.text.toString()
            val newRumahProduksi = binding.editRumahProduksi.text.toString()

            val updatedMovie = Movie(newId, newJudul, newGenre, newTahunRilis, newRumahProduksi)
            db.updateMovie(movieId, updatedMovie)

            Toast.makeText(this, "Data film berhasil diupdate!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Biar layout tetap rapi saat ada sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
