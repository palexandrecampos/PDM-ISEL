package pdm.isel.movieapp


import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.preference.PreferenceManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.movieapp.data.repository.MovieRepo
import pdm.isel.movieapp.data.services.MovieService
import pdm.isel.movieapp.data.services.ImageCache
import pdm.isel.movieapp.data.services.NowPlayingMoviesJobService
import pdm.isel.movieapp.data.services.UpcomingMoviesJobService
import java.util.concurrent.TimeUnit


class MovieApplication : Application() {

    @Volatile lateinit var requestQueue: RequestQueue
    @Volatile lateinit var imageLoader: ImageLoader
    @Volatile lateinit var service: MovieService
    @Volatile lateinit var language: String
    @Volatile lateinit var movieRepo: MovieRepo
    private lateinit var sharedPreferences: SharedPreferences
    private var batteryLimit: Int = 15
    private lateinit var updatePeriodicity: String
    var getNotifications: Boolean = false
    private var dataTypePreference: Boolean = false
    private var updateTime: Long = 7
    private var dataType: Int = JobInfo.NETWORK_TYPE_UNMETERED


    override fun onCreate() {
        super.onCreate()

        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, ImageCache())
        service = MovieService()
        language = resources.getString(R.string.language)
        movieRepo = MovieRepo(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        saveSharedPreferences()
        if(getBattery() >= batteryLimit)
            schedulingJob()
    }

    private fun schedulingJob() {


        val upcomingJob = JobInfo.Builder(
                UpcomingMoviesJobService.Id,
                ComponentName(this, UpcomingMoviesJobService::class.java))

        val upcomingJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        upcomingJobScheduler.schedule(upcomingJob
                .setPeriodic(TimeUnit.DAYS.toMillis(updateTime))
                .setRequiredNetworkType(dataType)
                .build())


        val nowPlayingJob = JobInfo.Builder(
                NowPlayingMoviesJobService.Id,
                ComponentName(this, NowPlayingMoviesJobService::class.java))

        val nowPlayingJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        nowPlayingJobScheduler.schedule(nowPlayingJob
                .setPeriodic(TimeUnit.DAYS.toMillis(updateTime))
                .setRequiredNetworkType(dataType)
                .build())
    }


    private fun saveSharedPreferences() {

        batteryLimit = (sharedPreferences.getString("battery_preference", "15")).toInt()
        updatePeriodicity = (sharedPreferences.getString("update_preference", "7"))
        getNotifications = sharedPreferences.getBoolean("notification_preference", false)
        dataTypePreference = sharedPreferences.getBoolean("only_wifi", false)

        updateTime = when (updatePeriodicity) {
            "Once a Day" -> 1
            "Twice a Week" -> 3
            "Once a Week" -> 7
            else -> {
                7
            }
        }

        dataType = when (dataTypePreference) {
            false -> JobInfo.NETWORK_TYPE_ANY
            true -> JobInfo.NETWORK_TYPE_UNMETERED
        }
    }

    private fun getBattery(): Int {
        val bm = applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}