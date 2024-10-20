import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class MovieInfo(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val show_time: Date?,
    val duration: Int,
    val rating: Double,
    val release_date: Date?,
    val cast: String,
    val image: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readSerializable() as? Date, // Sử dụng readSerializable thay vì getTime
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readSerializable() as? Date,
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeSerializable(show_time) // Sử dụng writeSerializable
        parcel.writeInt(duration)
        parcel.writeDouble(rating)
        parcel.writeSerializable(release_date) // Sử dụng writeSerializable
        parcel.writeString(cast)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieInfo> {
        override fun createFromParcel(parcel: Parcel): MovieInfo {
            return MovieInfo(parcel)
        }

        override fun newArray(size: Int): Array<MovieInfo?> {
            return arrayOfNulls(size)
        }
    }
}
