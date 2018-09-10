package pdm.isel.movieapp.ui.presentation

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_movie.*
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.ui.adapter.MyActorAdapter
import pdm.isel.movieapp.R
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCredits
import java.util.*

class MovieActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        var app = application as MovieApplication

        val currentDate = getCurrentDate()

        var intent = intent
        var movieDetails = intent.getParcelableExtra<Movie>("MovieDetails")

        var releaseDate = movieDetails.releaseDate

        if (releaseDate!! > currentDate) {
            followButton.visibility = View.VISIBLE
        }


        if (movieDetails.posterPath != null) {
            movieImage.setImageUrl("http://image.tmdb.org/t/p/w185//" + movieDetails.posterPath, (application as MovieApplication).imageLoader)
        } else movieImage.setDefaultImageResId(R.drawable.default_placeholder)
        movieVote.text = movieDetails.rating.toString()
        movieDate.text = movieDetails.releaseDate
        movieName.text = movieDetails.originalTitle
        movieOverview.text = movieDetails.overview

        if (isNetworkAvailable(applicationContext)) {
            var movieCredits = intent.getParcelableExtra<MovieCredits>("MovieCredits")

            var adapter = MyActorAdapter(
                    this,
                    R.layout.activity_movie_list_item,
                    movieCredits.cast,
                    app
            )

            listCredits.adapter = adapter
        }

        followButton.setOnClickListener {
            if (followButton.isChecked)
                app.movieRepo.followMovie(movieDetails)
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if(month < 10)
            return "$year-0$month-$day"
        return "$year-$month-$day"
    }
}
