package pdm.isel.movieapp.data.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.support.annotation.MainThread

class MovieProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "pdm.isel.movieapp"
        const val UPCOMING_TABLE_PATH = "Upcoming"
        const val EXHIBITION_TABLE_PATH = "Exhibition"
        const val FOLLOW_TABLE_PATH = "Follow"

        val UPCOMING_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$UPCOMING_TABLE_PATH")
        val EXHIBITION_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$EXHIBITION_TABLE_PATH")
        val FOLLOW_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$FOLLOW_TABLE_PATH")

        val MOVIE_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/movies"
        val MOVIE_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/movie"


        const val COLUMN_ID = "_ID"
        const val COLUMN_POSTER = "POSTER"
        const val COLUMN_TITLE = "TITLE"
        const val COLUMN_OVERVIEW = "OVERVIEW"
        const val COLUMN_VOTE = "VOTE"
        const val COLUMN_DATE = "DATE"

        const val UPCOMING_TABLE_NAME = "Upcoming"
        const val EXHIBITION_TABLE_NAME = "Exhibition"
        const val FOLLOW_TABLE_NAME = "Follow"

        const val UPCOMING_LIST_CODE = 1010
        const val UPCOMING_ITEM_CODE = 1011
        const val EXHIBITION_LIST_CODE = 1020
        const val EXHIBITION_ITEM_CODE = 1021
        const val FOLLOW_LIST_CODE = 2000
        const val FOLLOW_ITEM_CODE = 2001
    }

    @Volatile private lateinit var dbHelper: MovieInfoDbHelper

    @Volatile private lateinit var uriMatcher: UriMatcher


    @MainThread
    override fun onCreate(): Boolean {
        dbHelper = MovieInfoDbHelper(this@MovieProvider.context)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with(uriMatcher) {
            addURI(AUTHORITY, UPCOMING_TABLE_PATH, UPCOMING_LIST_CODE)
            addURI(AUTHORITY, "$UPCOMING_TABLE_PATH/#", UPCOMING_ITEM_CODE)
            addURI(AUTHORITY, EXHIBITION_TABLE_PATH, EXHIBITION_LIST_CODE)
            addURI(AUTHORITY, "$EXHIBITION_TABLE_PATH/#", EXHIBITION_ITEM_CODE)
            addURI(AUTHORITY, FOLLOW_TABLE_PATH, FOLLOW_LIST_CODE)
            addURI(AUTHORITY, "$FOLLOW_TABLE_PATH/#", FOLLOW_ITEM_CODE)
        }
        return true
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE, EXHIBITION_LIST_CODE, FOLLOW_LIST_CODE -> MOVIE_LIST_CONTENT_TYPE
        UPCOMING_ITEM_CODE, EXHIBITION_ITEM_CODE, FOLLOW_ITEM_CODE -> MOVIE_ITEM_CONTENT_TYPE
        else -> throw IllegalArgumentException("Uri $uri not supported")
    }


    private fun resolveTableInfoFromUri(uri: Uri): Pair<String, String> = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE -> Pair(UPCOMING_TABLE_NAME, UPCOMING_TABLE_PATH)
        EXHIBITION_LIST_CODE -> Pair(EXHIBITION_TABLE_NAME, EXHIBITION_TABLE_PATH)
        FOLLOW_LIST_CODE -> Pair(FOLLOW_TABLE_NAME, FOLLOW_TABLE_PATH)
        else -> null
    } ?: throw IllegalArgumentException("Uri $uri not supported")


    private fun resolveTableAndSelectionInfoFromUri(uri: Uri, selection: String?, selectionArgs: Array<String>?)
            : Triple<String, String?, Array<String>?> {
        val itemSelection = "$COLUMN_ID = ${uri.pathSegments.last()}"
        return when (uriMatcher.match(uri)) {
            UPCOMING_ITEM_CODE -> Triple(UPCOMING_TABLE_NAME, itemSelection, null)
            EXHIBITION_ITEM_CODE -> Triple(EXHIBITION_TABLE_NAME, itemSelection, null)
            FOLLOW_ITEM_CODE -> Triple(FOLLOW_TABLE_NAME, itemSelection, null)
            else -> resolveTableInfoFromUri(uri).let { Triple(it.first, selection, selectionArgs) }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.writableDatabase
        db.use {
            val deletedCount = db.delete(params.first, params.second, params.third)
            if (deletedCount != 0)
                context.contentResolver.notifyChange(uri, null)
            return deletedCount
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val tableInfo = resolveTableInfoFromUri(uri)
        val db = dbHelper.writableDatabase
        return db.use {
            val id = db.insert(tableInfo.first, null, values)
            if (id < 0) null
            else {
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("content://$AUTHORITY/${tableInfo.second}/$id")
            }
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.readableDatabase
        return db.query(params.first, projection, params.second, params.third, null, null, sortOrder)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.writableDatabase
        db.use {
            val updatedCount = db.update(params.first, values, params.second, params.third)
            if (updatedCount != 0)
                context.contentResolver.notifyChange(uri, null)
            return updatedCount
        }
    }
}