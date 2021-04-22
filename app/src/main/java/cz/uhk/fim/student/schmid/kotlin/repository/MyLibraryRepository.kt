package cz.uhk.fim.student.schmid.kotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil.Companion.addDaysToDate
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil.Companion.getDateFromNow
import java.util.*
import kotlin.collections.ArrayList

class MyLibraryRepository {
    companion object{
        var instance: MyLibraryRepository? = null
            get() {
                if(field == null)
                    field = MyLibraryRepository()
                return field
            }
    }

    private var data: MutableList<BookItem> = ArrayList()

    init{
        createSampleData()
    }

    fun getBooks() : MutableLiveData<List<BookItem>> {
        val sampleData: MutableLiveData<List<BookItem>> = MutableLiveData()
        sampleData.value = data
        return sampleData
    }

    private fun createSampleData(){
        data.add(BookItem(1, "Kytice", listOf("Karel Jaromír Erben"), "Kytice je básnická sbírka 13 lyrickoepických skladeb - balad. Všechny mají pochmurný děj, rychlý a dramatický spád a většinou špatný konec. Základním motivem je nějaké provinění, po kterém vždy přichází tvrdý trest, někdy lze vinu odčinit nebo zmírnit pokáním." , "https://www.levneknihy.cz/Document/140/140837/140837.jpg", getDateFromNow(-7), getDateFromNow(24)))
        data.add(BookItem(4, "Válka světů", listOf("Herbert George Wells"), "Válka světů je vědeckofantastický román Herberta George Wellse z roku 1898, jenž popisuje invazi mimozemšťanů z Marsu do Anglie. Je to jedna z prvních knih popisujících mimozemšťanskou invazi na Zemi. Několikrát byl podle ní natočen film.",  "https://knihydobrovsky.cz/thumbs/book-detail/mod_eshop/produkty/v/valka-svetu-9788075532220.jpg", getDateFromNow(-14), getDateFromNow(17)))
    }

    fun extendBookDate(bookItemId: Int, daysToAdd: Int): LiveData<Date> {
        val result = MutableLiveData<Date>()
        val bookItem = data.find { it.id == bookItemId }
        bookItem?.dateTo = addDaysToDate(bookItem?.dateTo ?: Date(), daysToAdd)
        bookItem?.setWasExtended()
        result.value = bookItem?.dateTo
        return result
    }

    fun isBookInMyLibrary(bookItemId: Int):LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        result.value = data.find{ it.id == bookItemId } != null
        return result
    }

    fun wasExtended(bookItemId: Int): LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        val bookItem = data.find { it.id == bookItemId }
        result.value = bookItem?.wasExtended() ?: false
        return result
    }
}