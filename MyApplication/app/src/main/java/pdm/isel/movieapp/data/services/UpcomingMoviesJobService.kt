package pdm.isel.movieapp.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import pdm.isel.movieapp.MovieApplication
import pdm.isel.movieapp.R
import pdm.isel.movieapp.data.exception.MovieRepoException
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection
import java.util.*


class UpcomingMoviesJobService : JobService() {

    companion object {
        const val Id = 1000
    }


    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        (application as MovieApplication).movieRepo.deleteTable(
                "Upcoming",
                {
                    jobFinished(p0, true)
                }
        )
        putUpcomingMoviesInLocalRepo(p0)
        return true
    }

    private fun putUpcomingMoviesInLocalRepo(p0: JobParameters?) {
        getFollowedMovies()
        (application as MovieApplication).requestQueue.add((application as MovieApplication).service.getUpcomingMovies(
                "https://api.themoviedb.org/3/movie/upcoming?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${(application as MovieApplication).language}",
                MovieCollection::class.java,
                { res ->
                    res.movies.forEach { movie ->
                        (application as MovieApplication).requestQueue.add((application as MovieApplication).service.getMovieDetails(
                                "https://api.themoviedb.org/3/movie/${movie.id}?api_key=${ConfigurationInfo.API_KEY_VALUE}&language=${(application as MovieApplication).language}",
                                Movie::class.java,
                                { movieDetails ->
                                    (application as MovieApplication).movieRepo.insertMovie(
                                            movieDetails,
                                            "Upcoming",
                                            { MovieRepoException("Error To Insert in Local Repository") })
                                }, { (jobFinished(p0, true)) }
                        ))
                    }
                }, { (jobFinished(p0, true)) }
        ))
        jobFinished(p0, false)
    }

    private fun getFollowedMovies() {
        (application as MovieApplication).movieRepo.getFollowedMovies(
                1,
                { res ->
                    res.movies.forEach {
                        movie ->
                            emitNotificationUpcomingMovies(movie)
                    }
                }, { MovieRepoException("Error To Send Notification") }
        )
    }

    private fun emitNotificationUpcomingMovies(movie : Movie){
        if(movie.releaseDate!! < getCurrentDate() && (application as MovieApplication).getNotifications ){
            sendNotifications(movie.originalTitle)
            (application as MovieApplication).movieRepo.unfollowMovie(movie)
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

    private fun sendNotifications(movieName : String?) {
        val id = "main_channel"
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val name = "Channel Name"
            val description = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(id, name, importance)
            notificationChannel.description = description
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, id)
                .setContentTitle("YAMDA Notification")
                .setContentText("$movieName is Now Playing")
                .setSmallIcon(R.drawable.ic_stat_movie_filter)
                .setLights(Color.WHITE, 500, 500)
                .setColor(Color.BLACK)
                .setDefaults(Notification.DEFAULT_SOUND)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(1000, notificationBuilder.build())

    }
}