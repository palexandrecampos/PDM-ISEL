package pdm.isel.movieapp.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

data class MovieCollection(
        val page: Int,
        @JsonProperty("total_pages") val totalPages: Int?,
        @JsonProperty("total_results") val totalResults: Int?,
        val dates: DatesInterval?,
        @JsonProperty("results") val movies: List<Movie>
) : Parcelable {

data class DatesInterval(
            val minimum: String,
            val maximum: String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(minimum)
        parcel.writeString(maximum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatesInterval> {
        override fun createFromParcel(parcel: Parcel): DatesInterval {
            return DatesInterval(parcel)
        }

        override fun newArray(size: Int): Array<DatesInterval?> {
            return arrayOfNulls(size)
        }
    }
}
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readParcelable(DatesInterval::class.java.classLoader),
            parcel.createTypedArrayList(Movie)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(page)
        parcel.writeValue(totalPages)
        parcel.writeValue(totalResults)
        parcel.writeParcelable(dates, flags)
        parcel.writeTypedList(movies)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieCollection> {
        override fun createFromParcel(parcel: Parcel): MovieCollection {
            return MovieCollection(parcel)
        }

        override fun newArray(size: Int): Array<MovieCollection?> {
            return arrayOfNulls(size)
        }
    }
}
