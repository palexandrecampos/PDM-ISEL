package pdm.isel.movieapp.ui.presentation

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ListView
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_upcoming_movies.*
import pdm.isel.movieapp.data.services.ConfigurationInfo
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.ui.adapter.MyMovieAdapter
import pdm.isel.movieapp.R
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection
import pdm.isel.movieapp.model.MovieCredits

class UpcomingMoviesActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)


        var app = application as MovieApplication

        var intent = intent
        var movieCollection = intent.getParcelableExtra<MovieCollection>("MovieList")

        var adapter = MyMovieAdapter(
                this,
                R.layout.activity_movie_list_item,
                movieCollection.movies,
                application as MovieApplication
        )

        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            var movie: Movie = adapter.getItem(position)
            if (isNetworkAvailable(app)) {
                (app).requestQueue.add((app).service.getMovieCredits
                ("https://api.themoviedb.org/3/movie/${movie.id}/credits?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${app.language}",
                        MovieCredits::class.java,
                        { success ->
                            val intent = Intent(this, MovieActivity::class.java)
                            intent.putExtra("MovieDetails", movie)
                            intent.putExtra("MovieCredits", success)
                            startActivity(intent)
                        }, { (VolleyError()) })
                )
            } else {
                val intent = Intent(this, MovieActivity::class.java)
                intent.putExtra("MovieDetails", movie)
                startActivity(intent)
            }

        }
    }
}
