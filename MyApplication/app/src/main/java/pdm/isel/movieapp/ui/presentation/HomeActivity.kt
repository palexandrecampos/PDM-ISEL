package pdm.isel.movieapp.ui.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_home.*
import pdm.isel.movieapp.data.services.ConfigurationInfo
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.R

import pdm.isel.movieapp.model.MovieCollection


class HomeActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        var app = application as MovieApplication

        fun Context.toast(message: CharSequence) =
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        searchButton.setOnClickListener {
            if (isNetworkAvailable(app)) {
                var name: String = movieTitleForSearch.text.toString()
                (app).requestQueue.add((app).service.getSearchMovies
                ("https://api.themoviedb.org/3/search/movie?query=$name&api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${app.language}",
                        MovieCollection::class.java,
                        { success ->
                            val intent = Intent(this, MostPopularActivity::class.java)
                            intent.putExtra("MovieList", success)
                            startActivity(intent)
                        }, { (VolleyError()) }))
            } else {
                toast("No Network Connection!!!!")
            }
        }

        mostPopular.setOnClickListener {
            if (isNetworkAvailable(app)) {
                (app).requestQueue.add((app).service.getMostPopularMovies
                ("https://api.themoviedb.org/3/movie/popular?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${app.language}",
                        MovieCollection::class.java,
                        { success ->
                            val intent = Intent(this, MostPopularActivity::class.java)
                            intent.putExtra("MovieList", success)
                            startActivity(intent)
                        }, { (VolleyError()) }))
            } else {
                toast("No Network Connection!!!!")
            }
        }


        nowPlaying.setOnClickListener {
            app.movieRepo.getNowPlayingMovies(
                    1,
                    { success ->
                        val intent = Intent(this, NowPlayingActivity::class.java)
                        intent.putExtra("MovieList", success)
                        startActivity(intent)
                    },
                    { (VolleyError()) })
        }


        upcomingMovies.setOnClickListener {
            app.movieRepo.getUpcomingMovies(
                    1,
                    { success ->
                        val intent = Intent(this, UpcomingMoviesActivity::class.java)
                        intent.putExtra("MovieList", success)
                        startActivity(intent)
                    },
                    { (VolleyError()) })
        }
    }
}
