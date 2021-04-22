package cz.uhk.fim.student.schmid.kotlin.model

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil
import java.net.URL



data class LibraryBookItem(override val id: Int, override val title: String, override val authors: List<String>, val genre: Genre, override val description: String, var total: Int, var borrowed: Int, override val imgUrl: String)
    : BookItemBase(id, title, authors, description, imgUrl), BookItemInterface, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: listOf(),
            parcel.readSerializable() as Genre,
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    fun getAuthorsFullNameString():CharSequence?{
        return BookItemUtil.authorsToString(authors, false)
    }

    fun isAvailable() = (total - borrowed) > 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeStringList(authors)
        parcel.writeSerializable(genre)
        parcel.writeString(description)
        parcel.writeInt(total)
        parcel.writeInt(borrowed)
        parcel.writeString(imgUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    //pouze jeden companion object
    companion object CREATOR : Parcelable.Creator<LibraryBookItem> {

        override fun createFromParcel(parcel: Parcel): LibraryBookItem {
            return LibraryBookItem(parcel)
        }

        override fun newArray(size: Int): Array<LibraryBookItem?> {
            return arrayOfNulls(size)
        }
    }
}