package pdm.isel.movieapp.data.handlers

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.net.Uri
import pdm.isel.movieapp.data.exception.MovieRepoException


internal class MyQueryHandler(resolver: ContentResolver) : AsyncQueryHandler(resolver) {

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
        if(uri == null) MovieRepoException("Insert Failed")
        return
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
        if(result <= 0) MovieRepoException("Update Failed")

        val res = cookie as? ((Int) -> Unit)
        res?.invoke(result)
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        if(result <= 0) MovieRepoException("Delete Failed")

        val res = cookie as? ((Int) -> Unit)
        res?.invoke(result)
    }
}