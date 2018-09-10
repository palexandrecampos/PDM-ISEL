package pdm.isel.movieapp.data.services

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class ImageCache : ImageLoader.ImageCache{

    private val mMemoryCache = LruCache<String, Bitmap>(20)

    override fun getBitmap(url: String?): Bitmap? = mMemoryCache.get(url)
    override fun putBitmap(url: String?, bitmap: Bitmap?){
        mMemoryCache.put(url,bitmap)
    }

}