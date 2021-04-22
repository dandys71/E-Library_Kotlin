package cz.uhk.fim.student.schmid.kotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil

class ReservedRepository {
    companion object{
        var instance: ReservedRepository? = null
        get() {
            if(field == null)
                field = ReservedRepository()
            return field
        }
    }

    private var data: MutableList<BookItem> = ArrayList()

    init {
        createSampleData()
    }

    fun getReservedBooks(): MutableLiveData<List<BookItem>>{
        val sampleData: MutableLiveData<List<BookItem>> = MutableLiveData()
        sampleData.value = data
        return sampleData
    }

    private fun createSampleData(){
        data.add(BookItem(3, "Povídky Malostránské", listOf("Jan Neruda"), "Povídky malostranské je sbírka třinácti povídek, původně vydávaných časopisecky. Děj se vždy odehrává na Malé Straně v 1. ... Převažuje forma povídek či novel, ale je zde využita i forma dopisu, deníku a zápisníku (Figurky). Postavy mluví jazykem lidovým, spisovným i nespisovným." , "https://www.levneknihy.cz/Document/144/144687/144687.jpg", BookItemUtil.getDateFromNow(-1), BookItemUtil.getDateFromNow(6)))
    }

    fun addBook(bookItem: BookItem) {
        data.add(bookItem)
    }

    fun removeBook(bookItemId: Int) {
        data.remove(data.find { it.id == bookItemId })
    }

    fun isReserved(bookItemId: Int): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = data.find { it.id == bookItemId } != null
        return result
    }
}