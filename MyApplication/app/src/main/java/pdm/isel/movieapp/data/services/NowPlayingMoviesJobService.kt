package pdm.isel.movieapp.data.services

import android.app.job.JobParameters
import android.app.job.JobService
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.data.exception.MovieRepoException
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection

class NowPlayingMoviesJobService : JobService() {

    companion object {
        const val Id = 2000
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        (application as MovieApplication).movieRepo.deleteTable(
                "Exhibition",
                {
                    jobFinished(p0, true)
                }
        )
        putNowPlayingMoviesInLocalRepo(p0)
        return true
    }

    private fun putNowPlayingMoviesInLocalRepo(p0: JobParameters?) {
        (application as MovieApplication).requestQueue.add((application as MovieApplication).service.getNowPlayingMovies(
                "https://api.themoviedb.org/3/movie/now_playing?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${(application as MovieApplication).language}",
                MovieCollection::class.java,
                { res ->
                    res.movies.forEach { movie ->
                        (application as MovieApplication).requestQueue.add((application as MovieApplication).service.getMovieDetails(
                                "https://api.themoviedb.org/3/movie/${movie.id}?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${(application as MovieApplication).language}",
                                Movie::class.java,
                                { movieDetails ->
                                    (application as MovieApplication).movieRepo.insertMovie(
                                            movieDetails,
                                            "Exhibition",
                                            { MovieRepoException("Error To Insert in Local Repository") })

                                }, { (jobFinished(p0, true)) }
                        ))
                    }
                }, { (jobFinished(p0, true)) }
        ))
        jobFinished(p0, false)
    }
}