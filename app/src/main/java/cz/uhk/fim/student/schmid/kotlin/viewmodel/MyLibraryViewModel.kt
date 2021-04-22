package cz.uhk.fim.student.schmid.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.repository.MyLibraryRepository

class MyLibraryViewModel : ViewModel() {

    var books: MutableLiveData<List<BookItem>>? = null
        private set

    private val repository = MyLibraryRepository.instance

    fun init(){
        books = repository?.getBooks()
    }

    fun extendBookDate(bookItemId: Int, daysToAdd: Int) = repository?.extendBookDate(bookItemId, daysToAdd)


    fun isBookInMyLibrary(bookItemId: Int) = repository?.isBookInMyLibrary(bookItemId)


    fun wasExtended(bookItemId: Int) = repository?.wasExtended(bookItemId)

}