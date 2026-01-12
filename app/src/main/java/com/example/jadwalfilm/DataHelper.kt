package com.example.jadwalfilm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Membuat tabel jadwal film
        val sql = """
            CREATE TABLE IF NOT EXISTS jadwalfilm (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                judul TEXT NOT NULL,
                tanggal TEXT,
                jam TEXT,
                genre TEXT,
                keterangan TEXT
            );
        """.trimIndent()

        Log.d("DataHelper", "onCreate: $sql")
        db.execSQL(sql)

        // Masukkan data awal (opsional)
        val insertSql = """
            INSERT INTO jadwalfilm (judul, tanggal, jam, genre, keterangan)
            VALUES 
                ('Avengers: Endgame', '2025-11-10', '19:30', 'Action', 'Pertarungan epik melawan Thanos'),
                ('Inside Out 2', '2025-11-11', '14:00', 'Animation', 'Petualangan emosi baru di dalam pikiran'),
                ('Dune: Part Two', '2025-11-12', '21:00', 'Sci-Fi', 'Kelanjutan kisah Paul Atreides di Arrakis');
        """.trimIndent()

        db.execSQL(insertSql)
        Log.d("DataHelper", "Data awal berhasil dimasukkan")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Hapus tabel lama dan buat ulang
        db?.execSQL("DROP TABLE IF EXISTS jadwalfilm")
        onCreate(db!!)
    }

    companion object {
        private const val DATABASE_NAME = "jadwalfilm.db"
        private const val DATABASE_VERSION = 4 // pastikan versi dinaikkan agar update tabel
    }
}
