package pdm.isel.movieapp.data.repository

import android.content.Context
import android.net.Uri
import pdm.isel.movieapp.data.handlers.MyQueryHandler
import pdm.isel.movieapp.data.provider.MovieProvider
import pdm.isel.movieapp.data.provider.toContentValues
import pdm.isel.movieapp.data.provider.toMovieItem
import pdm.isel.movieapp.data.provider.toMovieList
import pdm.isel.movieapp.data.exception.MovieRepoException
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection

class MovieRepo(private val ctx: Context) {

    private fun getLimitAndOffset(page: Int): Pair<Int, Int> {
        val offset = (page - 1) * 20
        val limit = offset + 20
        return Pair(limit, offset)
    }

    fun getUpcomingMovies(page: Int, success: (MovieCollection) -> Unit, error: (MovieRepoException) -> Unit) {
        val pair = getLimitAndOffset(page)
        val cursor = ctx.contentResolver.query(
                MovieProvider.UPCOMING_CONTENT_URI,
                null,
                null,
                null,
                "${MovieProvider.COLUMN_ID} limit ${pair.first} offset ${pair.second}"
        ) ?: return error(MovieRepoException())


        val resultQuery = cursor.toMovieList(page)
        cursor.close()
        success(resultQuery)

    }

    fun getNowPlayingMovies(page: Int, success: (MovieCollection) -> Unit, error: (MovieRepoException) -> Unit) {
        val pair = getLimitAndOffset(page)
        val cursor = ctx.contentResolver.query(
                MovieProvider.EXHIBITION_CONTENT_URI,
                null,
                null,
                null,
                "${MovieProvider.COLUMN_ID} limit ${pair.first} offset ${pair.second}"
        ) ?: return error(MovieRepoException())


        val resultQuery = cursor.toMovieList(page)
        cursor.close()
        success(resultQuery)

    }

    fun getMovie(id: Int, table: String, success: (Movie) -> Unit, error: (MovieRepoException) -> Unit) {
        val tableToGetMovie: Uri = when (table) {
            "Upcoming" -> MovieProvider.UPCOMING_CONTENT_URI
            "Exhibition" -> MovieProvider.EXHIBITION_CONTENT_URI
            else -> return error(MovieRepoException("Table Not Exist"))
        }

        val cursor = ctx.contentResolver.query(
                tableToGetMovie,
                null,
                "${MovieProvider.COLUMN_ID}= " + id,
                null,
                MovieProvider.COLUMN_ID
        ) ?: return error(MovieRepoException())


        val resultQuery = cursor.toMovieItem()
        cursor.close()
        success(resultQuery)
    }

    fun insertMovie(movie: Movie, table: String, error: (MovieRepoException) -> Unit) {
        val tableToGetMovie: Uri = when (table) {
            "Upcoming" -> MovieProvider.UPCOMING_CONTENT_URI
            "Exhibition" -> MovieProvider.EXHIBITION_CONTENT_URI
            else -> return error(MovieRepoException("Table Not Exist"))
        }

        MyQueryHandler(
                ctx.contentResolver
        ).startInsert(
                0,
                null,
                tableToGetMovie,
                movie.toContentValues()
        )
    }

    fun deleteTable(table: String, error: (MovieRepoException) -> Unit) {
        val tableToDelete: Uri = when (table) {
            "Upcoming" -> MovieProvider.UPCOMING_CONTENT_URI
            "Exhibition" -> MovieProvider.EXHIBITION_CONTENT_URI
            else -> return error(MovieRepoException("Table Not Exist"))
        }

        MyQueryHandler(ctx.contentResolver).startDelete(
                0,
                null,
                tableToDelete,
                null,
                null)
    }

    fun followMovie(movie: Movie) {
        val uri = MovieProvider.FOLLOW_CONTENT_URI

        MyQueryHandler(ctx.contentResolver).startInsert(
                0,
                null,
                uri,
                movie.toContentValues()
        )

    }

    fun unfollowMovie(movie: Movie) {
        val uri = MovieProvider.FOLLOW_CONTENT_URI

        val myArray = arrayOf(movie.id.toString())
        MyQueryHandler(ctx.contentResolver).startDelete(
                0,
                null,
                uri,
                " _ID = ? ",
                myArray

        )
    }

    fun getFollowedMovies(page: Int, success: (MovieCollection) -> Unit, error: (MovieRepoException) -> Unit) {
        val pair = getLimitAndOffset(page)
        val cursor = ctx.contentResolver.query(
                MovieProvider.FOLLOW_CONTENT_URI,
                null,
                null,
                null,
                "${MovieProvider.COLUMN_ID} limit ${pair.first} offset ${pair.second}"
        ) ?: return error(MovieRepoException())


        val resultQuery = cursor.toMovieList(page)
        cursor.close()
        success(resultQuery)
    }
}