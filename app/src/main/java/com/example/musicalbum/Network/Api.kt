package com.example.musicalbum.Network

import com.example.musicalbum.Model.Photo
import com.example.musicalbum.Model.Music
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("albums")
    fun getAllData(): Observable<List<Music>>

    @GET("photos")
    fun getAlbumPhotos(@Query("albumId") albumId: Int): Single<List<Photo>>

}
