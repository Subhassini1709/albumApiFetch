package com.example.musicalbum

import MusicAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalbum.Network.RetrofitInstance
import com.example.musicalbum.Model.Music
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter
    private val api = RetrofitInstance.api // initialize api here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicAdapter = MusicAdapter(this, ArrayList<Music>(), api) // pass api instance here
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply{
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this@MainActivity)
            adapter=musicAdapter

        }
        val compositeDisposable= CompositeDisposable()
        compositeDisposable.add(getObservable().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response->getObserver(response as ArrayList<Music>)},
                {t->onFailure(t)}
            ))
    }

    private fun getObserver(musicList: ArrayList<Music>) {
        if(musicList!= null && musicList.size!=0)
        {
            musicAdapter.setData(musicList)
        }
    }

    private fun onFailure(t: Throwable) {
        Log.d("main", "onFailure: "+t.message)
    }

    private fun getObservable(): Observable<List<Music>> {
        return RetrofitInstance.api.getAllData()

    }
}
