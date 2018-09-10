package pdm.isel.movieapp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * This class is used for representing The credits of a movie
 */
class MovieCredits(
        val movieId: Int?,
        val cast: List<Actor>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(Actor)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(movieId)
        parcel.writeTypedList(cast)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieCredits> {
        override fun createFromParcel(parcel: Parcel): MovieCredits {
            return MovieCredits(parcel)
        }

        override fun newArray(size: Int): Array<MovieCredits?> {
            return arrayOfNulls(size)
        }
    }
}