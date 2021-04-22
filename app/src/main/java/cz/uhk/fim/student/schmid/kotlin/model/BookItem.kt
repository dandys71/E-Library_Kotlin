package cz.uhk.fim.student.schmid.kotlin.model

import android.os.Parcel
import android.os.Parcelable
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil
import java.util.*

//data class si nerozumí s děděním (proč nelze jednoduše předat parametry rodičovi???)
data class BookItem(override val id: Int, override val title: String, override val authors: List<String>, override val description: String, override val imgUrl: String, var dateFrom: Date, var dateTo: Date)
    : BookItemBase(id, title, authors, description, imgUrl), Parcelable {
    private var wasExtended = false
    
    fun setWasExtended(){
        this.wasExtended = true
    }

    fun wasExtended() = wasExtended

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "" ,
            parcel.createStringArrayList().let { it?.toList() as List<String> },
            parcel.readString() ?: "",
            parcel.readString() ?: "" ,
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date)
             {
        wasExtended = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeStringList(authors)
        parcel.writeString(description)
        parcel.writeString(imgUrl)
        parcel.writeSerializable(dateFrom)
        parcel.writeSerializable(dateTo)
        parcel.writeByte(if (wasExtended) 1 else 0)
    }

    override fun describeContents(): Int {
        return hashCode()
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }

}