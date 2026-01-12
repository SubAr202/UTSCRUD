package com.example.jadwalfilm.api

import com.example.jadwalfilm.model.JadwalFilm
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("film.php")
    fun getJadwal(
        @Query("proc") proc: String = "get"
    ): Call<List<JadwalFilm>>

    @FormUrlEncoded
    @POST("film.php?proc=insert")
    fun insertJadwal(
        @Field("judul") judul: String,
        @Field("tanggal") tanggal: String,
        @Field("jam") jam: String,
        @Field("genre") genre: String,
        @Field("keterangan") keterangan: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("film.php?proc=update")
    fun updateJadwal(
        @Field("id") id: String,
        @Field("judul") judul: String,
        @Field("tanggal") tanggal: String,
        @Field("jam") jam: String,
        @Field("genre") genre: String,
        @Field("keterangan") keterangan: String
    ): Call<ResponseBody>

    // ✅ INI YANG DIPERBAIKI
    @GET("film.php")
    fun deleteJadwal(
        @Query("proc") proc: String = "delete",
        @Query("id") id: String
    ): Call<ResponseBody>
}
