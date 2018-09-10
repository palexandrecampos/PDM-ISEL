package pdm.isel.movieapp.data.provider

import android.content.ContentValues
import android.database.Cursor
import pdm.isel.movieapp.model.Movie
import pdm.isel.movieapp.model.MovieCollection

fun Movie.toContentValues(): ContentValues {
    val result = ContentValues()
    with(MovieProvider) {
        result.put(COLUMN_ID, id)
        result.put(COLUMN_POSTER, posterPath)
        result.put(COLUMN_TITLE, originalTitle)
        result.put(COLUMN_DATE, releaseDate)
        result.put(COLUMN_OVERVIEW, overview)
        result.put(COLUMN_VOTE, rating)

    }
    return result
}


fun MovieCollection.toContentValues(): Array<ContentValues> =
        movies.map { it.toContentValues() }.toTypedArray()


fun Cursor.toMovieItem(): Movie {
    with(MovieProvider) {
        return Movie(
                id = getInt(getColumnIndex(COLUMN_ID)),
                posterPath = getString(getColumnIndex(COLUMN_POSTER)),
                overview = getString(getColumnIndex(COLUMN_OVERVIEW)),
                releaseDate = getString(getColumnIndex(COLUMN_DATE)),
                originalTitle = getString(getColumnIndex(COLUMN_TITLE)),
                rating = getDouble(getColumnIndex(COLUMN_VOTE))
        )
    }
}


fun Cursor.toMovieList(page: Int): MovieCollection {

    val cursorIterator = object : AbstractIterator<Movie>() {
        override fun computeNext() {
            moveToNext()
            when (isAfterLast) {
                true -> done()
                false -> setNext(toMovieItem())
            }
        }
    }

    val result = mutableListOf<Movie>().let {
        it.addAll(Iterable { cursorIterator }); it
    }

    return MovieCollection(
            page,
            null,
            null,
            null,
            result)
}