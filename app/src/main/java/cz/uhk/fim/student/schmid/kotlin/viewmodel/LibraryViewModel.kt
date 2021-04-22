package cz.uhk.fim.student.schmid.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.uhk.fim.student.schmid.kotlin.model.LibraryBookItem
import cz.uhk.fim.student.schmid.kotlin.repository.LibraryRepository

class LibraryViewModel : ViewModel() {
    var books: MutableLiveData<List<LibraryBookItem>>? = null
    private set

    private val repository = LibraryRepository.instance

    fun init(){
        if(books != null){
            return
        }
        books = repository?.getBooks()
    }

    fun increaseBorrowed(bookItemId: Int) = repository?.increaseBorrowed(bookItemId)

    fun decreaseBorrowed(bookItemId: Int) = repository?.decreaseBorrowed(bookItemId)

    fun getTotalOfBook(bookItemId: Int) = repository?.getTotalOfBook(bookItemId)

    fun getTotalOfBorrowed(bookItemId: Int) = repository?.getTotalOfBorrowed(bookItemId)

}