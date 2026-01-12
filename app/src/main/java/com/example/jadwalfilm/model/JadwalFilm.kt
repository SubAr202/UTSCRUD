package com.example.jadwalfilm.model

data class JadwalFilm(
    val id: String,
    val judul: String,
    val tanggal: String,
    val jam: String,
    val genre: String,
    val keterangan: String
)
