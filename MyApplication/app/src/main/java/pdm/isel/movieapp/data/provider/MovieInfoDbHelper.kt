package pdm.isel.movieapp.data.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieInfoDbHelper(
        context: Context,
        version: Int = 1,
        dbName: String = "MOVIE_DB.db"
) : SQLiteOpenHelper(context, dbName, null, version) {

    private fun createTable(db: SQLiteDatabase?, tableName: String) {
        val CREATE_CMD = "CREATE TABLE $tableName ( " +
                "${MovieProvider.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${MovieProvider.COLUMN_POSTER} TEXT, " +
                "${MovieProvider.COLUMN_VOTE} REAL, " +
                "${MovieProvider.COLUMN_TITLE} TEXT NOT NULL, " +
                "${MovieProvider.COLUMN_DATE} REAL, " +
                "${MovieProvider.COLUMN_OVERVIEW} TEXT )"
        db?.execSQL(CREATE_CMD)
    }

    private fun dropTable(db: SQLiteDatabase?, tableName: String) {
        val DROP_CMD = "DROP TABLE IF EXISTS $tableName"
        db?.execSQL(DROP_CMD)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db, MovieProvider.UPCOMING_TABLE_NAME)
        createTable(db, MovieProvider.EXHIBITION_TABLE_NAME)
        createTable(db, MovieProvider.FOLLOW_TABLE_NAME)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        dropTable(db, MovieProvider.UPCOMING_TABLE_NAME)
        dropTable(db, MovieProvider.EXHIBITION_TABLE_NAME)
        createTable(db, MovieProvider.UPCOMING_TABLE_NAME)
        createTable(db, MovieProvider.EXHIBITION_TABLE_NAME)
    }
}