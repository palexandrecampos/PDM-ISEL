package pdm.isel.movieapp.data.services

import com.android.volley.Request
import com.android.volley.VolleyError
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection
import pdm.isel.movieapp.model.MovieCredits


class MovieService {


    fun getUpcomingMovies(url: String,
                          dtoType: Class<MovieCollection>,
                          success: (MovieCollection) -> Unit,
                          error: (VolleyError) -> Unit): Request<MovieCollection> {

        return GetRequest(url, dtoType, success, error)
    }

    fun getNowPlayingMovies(url: String,
                            dtoType: Class<MovieCollection>,
                            success: (MovieCollection) -> Unit,
                            error: (VolleyError) -> Unit): Request<MovieCollection> {


        return GetRequest(url, dtoType, success, error)
    }

    fun getSearchMovies(url: String,
                        dtoType: Class<MovieCollection>,
                        success: (MovieCollection) -> Unit,
                        error: (VolleyError) -> Unit): Request<MovieCollection> {


        return GetRequest(url, dtoType, success, error)
    }

    fun getMostPopularMovies(url: String,
                             dtoType: Class<MovieCollection>,
                             success: (MovieCollection) -> Unit,
                             error: (VolleyError) -> Unit): Request<MovieCollection> {


        return GetRequest(url, dtoType, success, error)
    }

    fun getMovieCredits(url: String,
                         dtoType: Class<MovieCredits>,
                         success: (MovieCredits) -> Unit,
                         error: (VolleyError) -> Unit): Request<MovieCredits> {

        return GetRequest(url, dtoType, success, error)
    }

    fun getMovieDetails(url: String,
                        dtoType: Class<Movie>,
                        success: (Movie) -> Unit,
                        error: (VolleyError) -> Unit): Request<Movie> {

        return GetRequest(url, dtoType, success, error)
    }
}