package com.example.musicalbum.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalbum.Adapter.PhotoAdapter
import com.example.musicalbum.Model.Photo
import com.example.musicalbum.Network.Api
import com.example.musicalbum.Network.RetrofitInstance
import com.example.musicalbum.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoActivity : AppCompatActivity() {

    private lateinit var photoList: ArrayList<Photo>
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()
    private val api: Api = RetrofitInstance.api


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_photos)

        /*val jsonPhotos = intent.getStringExtra("photos")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Photo>>() {}.type
        photoList = gson.fromJson(jsonPhotos, type)*/

        recyclerView = findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this@PhotoActivity)
        photoAdapter = PhotoAdapter(photoList)
        recyclerView.adapter = photoAdapter

        val albumId = intent.getIntExtra("albumId", -1)
        if (albumId != -1) {
            loadPhotos(albumId)
        } else {
            Toast.makeText(this, "Invalid album id", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPhotos(albumId: Int) {
        disposable.add(
                api.getAlbumPhotos(albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { photos ->
                        photoList.clear()
                        photoList.addAll(photos)
                        photoAdapter.notifyDataSetChanged()
                    },
                    { error ->
                        Toast.makeText(
                            this,
                            "Failed to load photos: ${error.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
