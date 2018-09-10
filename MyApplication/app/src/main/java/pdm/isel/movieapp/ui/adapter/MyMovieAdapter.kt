package pdm.isel.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.R
import pdm.isel.movieapp.model.Movie

class MyMovieAdapter(context: Context, resource: Int, list: List<Movie>, application: MovieApplication) : ArrayAdapter<Movie>(context, resource, list) {

    val app : MovieApplication
    val vi: LayoutInflater
    val resource: Int
    val list: List<Movie>


    init {
        this.resource = resource
        this.list = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.app = application

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var holder: ViewHolder
        var retView: View

        if (convertView == null) {
            retView = vi.inflate(resource, parent, false)

            holder = ViewHolder(
                    retView.findViewById(R.id.movieImageList),
                    retView.findViewById(R.id.movieNameList),
                    retView.findViewById(R.id.movieDateList)
            )

            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        if(list.get(position).posterPath != null)
            holder.image.setImageUrl("http://image.tmdb.org/t/p/w185//"+list.get(position).posterPath, app.imageLoader)
        else holder.image.setImageResource(R.drawable.default_placeholder)
        holder.nameMovie.text = list.get(position).originalTitle
        holder.releaseDate.text = list.get(position).releaseDate


        return retView
    }

    class ViewHolder(
            val image: NetworkImageView,
            val nameMovie: TextView,
            val releaseDate: TextView
    )
}