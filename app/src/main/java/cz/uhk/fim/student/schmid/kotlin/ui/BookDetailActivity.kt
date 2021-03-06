package cz.uhk.fim.student.schmid.kotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import cz.uhk.fim.student.schmid.R
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.model.Genre
import cz.uhk.fim.student.schmid.kotlin.model.LibraryBookItem
import cz.uhk.fim.student.schmid.kotlin.util.BookDetailActivityUtil
import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil.Companion.getDateFromNow
import cz.uhk.fim.student.schmid.kotlin.viewmodel.LibraryViewModel
import cz.uhk.fim.student.schmid.kotlin.viewmodel.MyLibraryViewModel
import cz.uhk.fim.student.schmid.kotlin.viewmodel.ReservedViewModel
import java.text.SimpleDateFormat
import java.util.*

class BookDetailActivity : AppCompatActivity() {

    private lateinit var tvInfo: TextView
    private lateinit var btn: Button

    private lateinit var type: FRAGMENT
    private var book: BookItem? = null
    private var libBook: LibraryBookItem? = null
    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var myLibraryViewModel: MyLibraryViewModel
    private lateinit var reservedViewModel: ReservedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        tvInfo = findViewById(R.id.d_tv_info)
        btn = findViewById(R.id.d_btn)

        type = intent.getSerializableExtra(BOOK_FRAGMENT_TYPE) as FRAGMENT

        libraryViewModel = ViewModelProvider(this).get(LibraryViewModel::class.java)
        myLibraryViewModel = ViewModelProvider(this).get(MyLibraryViewModel::class.java)
        reservedViewModel = ViewModelProvider(this).get(ReservedViewModel::class.java)

        if(type != FRAGMENT.LIBRARY) {
            book = intent.getParcelableExtra(BOOK_ITEM)
            book?.let { initialize(it.title, it.authorsToString(), it.description, it.imgUrl)}
        }
        else {
            libBook = intent.getParcelableExtra(LIBRARY_BOOK_ITEM)
            libBook?.let { initialize(it.title, it.authorsToString(), it.description, it.imgUrl) }
        }

    }

    //init je k ni??emu, kdy?? chci na????tat data, kter?? jsou dostupn?? a?? po zavol??n?? onCreate... zm??nit jako nev??hodu, v onCreate zase nelze data nahr??t do val, max do var s p????vlastek lateinit....
    //lateinit nelze pou????t na nullable datov?? typy... co?? je probl??m, jeliko?? getExtras nemus?? b??t definov??no a tudi?? vrac?? nullable datov?? typ.... pon??kud zamotan?? kruh, tak??e je nutn?? odstranit lateinit a nastavit prom??nn?? na null, co?? nen?? moc esteticky p??kn??
    //jako ??e??en?? je mo??n?? ud??lat si vlastn?? funkci na z??sk??n?? StringExtra a pokud nebudou data definov??na vr??t?? pr??zdn?? string

    /**
     *Slou???? pro inicializaci aktivity (pro p??ehledn??j???? onCreate())
     **/
    private fun initialize(title: String, authors: String, description: String, img: String) {
        //Deklarace a inicializace View prvk??
        val ivImg: ImageView = findViewById(R.id.d_iv_book)
        val tvTitle: TextView = findViewById(R.id.d_tv_title)
        val tvAuthors: TextView = findViewById(R.id.d_tv_authors)
        val tvDescription: TextView = findViewById(R.id.d_tv_description)

        //Knihovna Glide slou???? pro na??t??n?? velk??ch obr??zk?? do ImageView (prevence proti StackOverFlowException)
        Glide
                .with(this)
                .load(img)
                .into(ivImg)

        //Nastaven?? dat do View
        tvTitle.text = title
        tvAuthors.text = authors
        tvDescription.text = description
        onFragmentChange(type)
    }

    private fun onFragmentChange(type: FRAGMENT) {
        if (type != FRAGMENT.LIBRARY) {
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            enableButton()

            if (type == FRAGMENT.MY_LIBRARY) {
                initializeMyLibraryUi(format);
            } else if (type == FRAGMENT.RESERVED) {
                initializeReservedUi(format)
            }

        } else {

            if (libBook?.total == 0) { //m????e b??t nula pokud p??ejdu z RESERVED FRAGMENTU do tohoto, je pot??eba zjistit na z??klad?? funkce z "DB"
                val lifecycleOwner: LifecycleOwner = this
                libBook?.id?.let {
                    libraryViewModel.getTotalOfBook(it)?.observe(lifecycleOwner, Observer { total ->
                        libBook?.total = total
                        libraryViewModel.getTotalOfBorrowed(it)?.observe(lifecycleOwner, Observer { borrowed ->
                            libBook?.borrowed = borrowed
                            initializeLibraryUi()
                        })
                    })

                }

            } else {
                initializeLibraryUi()
            }
        }
    }

    private fun initializeMyLibraryUi(format: SimpleDateFormat){
        btn.text = getText(R.string.extend)
        book?.id?.let { id ->
            myLibraryViewModel.wasExtended(id)?.observe(this, Observer {
                if(it){
                    setMyLibraryFragmentInfoText(format, false)
                    disableButton()
                }else{
                    setMyLibraryFragmentInfoText(format, true)
                }
            })
        }

        btn.setOnClickListener {
            val date = book?.id?.let { id ->
                myLibraryViewModel.extendBookDate(id, 30)?.observe(this, Observer {
                    if (it != null) {
                        Toast.makeText(this, getText(R.string.extend_success), Toast.LENGTH_LONG).show()
                        book?.dateTo = it
                        setMyLibraryFragmentInfoText(format, false)
                        disableButton()
                    }
                })
            }
        }
    }

    private fun initializeReservedUi(format: SimpleDateFormat){
        book?.dateFrom?.let { from ->
            book?.dateTo?.let { to->
                tvInfo.text = String.format(getText(R.string.reserved_book_info).toString(), format.format(from), format.format(to))
            } }

        btn.text = getText(R.string.cancel_reservation)
        btn.setOnClickListener {
            //pot??eba za id p??idat elvis oper??tor, pokud se neuvede je pot??eba v t??le let uv??st podm??nku na it != null
            book?.let { libBook = LibraryBookItem(it.id, it.title, it.authors, Genre.NONE, it.description, 0, 0, it.imgUrl) } //hodnoty nejsou zn??m?? nastav?? se na defaultn??, spr??vn?? se na??tou z DB
            book?.id?.let {
                reservedViewModel.removeBook(it)
                libraryViewModel.decreaseBorrowed(it)
            }

            Toast.makeText(this, getText(R.string.cancel_reservation_success), Toast.LENGTH_LONG).show()
            onFragmentChange(FRAGMENT.LIBRARY)
        }
    }

    private fun initializeLibraryUi(){
        if (libBook?.total != 0) {
            setLibraryFragmentInfoText()
            btn.text = getText(R.string.reserve)

            btn.setOnClickListener {
                libBook?.id?.let { id ->
                    val lifecycleOwner: LifecycleOwner = this
                    reservedViewModel.isReserved(id)?.observe(lifecycleOwner, Observer { isReserved ->
                        if(!isReserved){
                            myLibraryViewModel.isBookInMyLibrary(id)?.observe(lifecycleOwner, Observer { isInLibrary ->
                                if(!isInLibrary){
                                    libBook?.let { book = BookItem(it.id, it.title, it.authors, it.description, it.imgUrl, Date(), getDateFromNow(7)) }
                                    book?.let { reservedViewModel.addBook(it) }

                                    libraryViewModel.increaseBorrowed(id)
                                    libBook?.borrowed = libBook?.borrowed?.plus(1)!!
                                    setLibraryFragmentInfoText()
                                    Toast.makeText(this, getText(R.string.reserved_success), Toast.LENGTH_LONG).show()
                                    onFragmentChange(FRAGMENT.RESERVED)
                                }else{
                                    Toast.makeText(this, getText(R.string.reserved_failure_in_my_library), Toast.LENGTH_LONG).show()
                                }
                            })
                        }else{
                            Toast.makeText(this, getText(R.string.reserved_failure_in_reserved), Toast.LENGTH_LONG).show()

                        }
                    })
                }
            }
        }
    }

    private fun setMyLibraryFragmentInfoText(format: SimpleDateFormat, canBeExtended: Boolean) {
        book?.dateFrom.let {
            from -> if(from != null)
                book?.dateTo.let {
                    to -> if(to != null)
                        tvInfo.text = if(canBeExtended)
                            String.format(getText(R.string.my_library_book_info).toString(), format.format(from), format.format(to))
                        else
                            String.format(getText(R.string.my_library_book_info_can_not_extend).toString(), format.format(from), format.format(to))
                } }

    }

    private fun disableButton(){
        btn.isEnabled = false
        btn.alpha = .5f
    }

    private fun enableButton(){
        btn.isEnabled = true
        btn.alpha = 1f
    }

    private fun setLibraryFragmentInfoText() {
        val total = libBook?.total ?: 0
        val borrowed = libBook?.borrowed ?: 0
        tvInfo.text = if (total -borrowed > 0) {
            String.format(resources.getText(R.string.library_book_info_available).toString(), getPiecesLeftString(total - borrowed), total - borrowed, getPiecesString(total - borrowed), total)
        } else {
            String.format(resources.getText(R.string.library_book_info_unavailable).toString(), total, getPiecesString(total))
        }

        if (total - borrowed <= 0) {
            disableButton()
        }
    }

    private fun getPiecesString(num: Int) = BookDetailActivityUtil.getPiecesString(this, num)

    private fun getPiecesLeftString(num: Int) = BookDetailActivityUtil.getPiecesLeftString(this, num)

    companion object {
        /**
         * Konstanty slou????c?? pro p??ed??v??n?? ??daj?? skrze intent do t??to aktivity
         * */
        const val BOOK_FRAGMENT_TYPE = "fragment_type"
        const val BOOK_ITEM = "book_item"
        const val LIBRARY_BOOK_ITEM = "my_library_book_item"

        enum class FRAGMENT {
            LIBRARY,
            MY_LIBRARY,
            RESERVED
        }
    }

}

