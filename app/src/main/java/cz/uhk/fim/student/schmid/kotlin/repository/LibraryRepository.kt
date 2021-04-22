package cz.uhk.fim.student.schmid.kotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.uhk.fim.student.schmid.kotlin.model.LibraryBookItem
import cz.uhk.fim.student.schmid.kotlin.model.Genre

class LibraryRepository {
    companion object{
        var instance: LibraryRepository? = null
            get() {
            if(field == null)
               field = LibraryRepository()
            return field
        }
    }

    private var data: MutableList<LibraryBookItem> = ArrayList()

    init {
        createSampleData()
    }

    fun getBooks() : MutableLiveData<List<LibraryBookItem>>{
        val sampleData: MutableLiveData<List<LibraryBookItem>> = MutableLiveData()
        sampleData.value = data
        return sampleData
    }

    private fun createSampleData(){
        data.add(LibraryBookItem(1, "Kytice", listOf("Karel Jaromír Erben"), Genre.POEZIE, "Kytice je básnická sbírka 13 lyrickoepických skladeb - balad. Všechny mají pochmurný děj, rychlý a dramatický spád a většinou špatný konec. Základním motivem je nějaké provinění, po kterém vždy přichází tvrdý trest, někdy lze vinu odčinit nebo zmírnit pokáním." ,10, 6, "https://www.levneknihy.cz/Document/140/140837/140837.jpg"))
        data.add(LibraryBookItem(2, "Babička", listOf("Božena Němcová"), Genre.ROMAN, "Babička Magdaléna Novotná – Moudrá, prostá, pracovitá, obětavá a laskavá žena, která okolí rozdává lásku. Je dobrou katoličkou a ostatní si jí váží. Dodržuje tradice. ... Kvůli nešťastné lásce se zblázní.", 7, 6, "https://www.levneknihy.cz/Document/106/106812/106812.jpg"))
        data.add(LibraryBookItem(3, "Povídky Malostránské", listOf("Jan Neruda"), Genre.POVIDKA, "Povídky malostranské je sbírka třinácti povídek, původně vydávaných časopisecky. Děj se vždy odehrává na Malé Straně v 1. ... Převažuje forma povídek či novel, ale je zde využita i forma dopisu, deníku a zápisníku (Figurky). Postavy mluví jazykem lidovým, spisovným i nespisovným.", 4, 2, "https://www.levneknihy.cz/Document/144/144687/144687.jpg"))
        data.add(LibraryBookItem(4, "Válka světů", listOf("Herbert George Wells"), Genre.SCI_FI, "Válka světů je vědeckofantastický román Herberta George Wellse z roku 1898, jenž popisuje invazi mimozemšťanů z Marsu do Anglie. Je to jedna z prvních knih popisujících mimozemšťanskou invazi na Zemi. Několikrát byl podle ní natočen film.", 3, 3, "https://knihydobrovsky.cz/thumbs/book-detail/mod_eshop/produkty/v/valka-svetu-9788075532220.jpg"))
    }

    fun increaseBorrowed(bookItemId: Int):Boolean{
        val item = data.find { it.id == bookItemId }
        if(item != null) {
            if ((item.borrowed.plus(1)) <= item.total) {
                item.borrowed = item.borrowed.plus(1)
                return true
            }
        }
        return false
    }

    fun decreaseBorrowed(bookItemId: Int):Boolean{
        val item = data.find { it.id == bookItemId }
        if(item != null) {
            if ((item.borrowed.minus(1)) >= 0) {
                item.borrowed = item.borrowed.minus(1)
                return true
            }
        }
        return false
    }

    fun getTotalOfBook(bookItemId:  Int): LiveData<Int> {
        val result = MutableLiveData<Int>()
        val item = data.find { it.id == bookItemId }
        result.value = item?.total ?: 0
        return result
    }

    fun getTotalOfBorrowed(bookItemId: Int): LiveData<Int> {
        val result = MutableLiveData<Int>()
        val item = data.find { it.id == bookItemId }
        result.value = item?.borrowed ?: 0
        return result;
    }


}