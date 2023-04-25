import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalbum.Activity.PhotoActivity
import com.example.musicalbum.Model.Music
import com.example.musicalbum.Network.Api
import com.example.musicalbum.R
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MusicAdapter(
    private val context: Context,
    private var musicList: ArrayList<Music>,
    private val api: Api
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MusicAdapter.MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.each_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.albumTitle.text = music.title

        holder.itemView.setOnClickListener {
            api.getAlbumPhotos(music.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photos ->
                    val gson = Gson()
                    val jsonPhotos = gson.toJson(photos)
                    val intent = Intent(context, PhotoActivity::class.java)
                    intent.putExtra("photos", jsonPhotos)
                    context.startActivity(intent)
                }, { t ->
                    Toast.makeText(context, "Error loading photos", Toast.LENGTH_SHORT).show()
                    Log.e("MusicAdapter", "Error loading photos", t)
                })
        }
    }

    override fun getItemCount(): Int = musicList.size

    fun setData(musicList: List<Music>) {
        this.musicList = ArrayList(musicList)
        notifyDataSetChanged()
    }


    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumTitle: TextView = itemView.findViewById(R.id.album_title)
    }
}
