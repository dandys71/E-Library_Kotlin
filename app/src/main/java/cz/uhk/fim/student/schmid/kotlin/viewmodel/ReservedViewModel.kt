package cz.uhk.fim.student.schmid.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.repository.ReservedRepository

class ReservedViewModel : ViewModel() {

    var books: MutableLiveData<List<BookItem>>? = null
        private set;

    private val repository = ReservedRepository.instance

    fun init(){
        if(books != null){
            return
        }
        books = repository?.getReservedBooks();
    }

    fun addBook(bookItem: BookItem) = repository?.addBook(bookItem)

    fun removeBook(bookItemId: Int) =  repository?.removeBook(bookItemId)

    fun isReserved(bookItemId: Int) = repository?.isReserved(bookItemId)

}