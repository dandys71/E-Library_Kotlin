package cz.uhk.fim.student.schmid.kotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.uhk.fim.student.schmid.R
import cz.uhk.fim.student.schmid.kotlin.model.LibraryBookItem
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity.Companion.BOOK_FRAGMENT_TYPE
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity.Companion.LIBRARY_BOOK_ITEM
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity.Companion.FRAGMENT.LIBRARY

/**
 * Adaptér sloužící pro Library Aktivitu implementující rozhraní Filterable pro možnost filtrovat knihy skrze vyhledávání
 * */
class LibraryBookItemAdapter(private val context: Context?, private val libraryBookItemList: List<LibraryBookItem>, private val noResultTv: TextView) : RecyclerView.Adapter<LibraryBookItemAdapter.LibraryBookItemViewHolder>(), Filterable {

    private var libraryBookListFiltered:MutableList<LibraryBookItem> = libraryBookItemList.toMutableList()

    class LibraryBookItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.iv_book)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthors: TextView = itemView.findViewById(R.id.tvAuthors)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val ivAvailability: ImageView = itemView.findViewById(R.id.iv_isAvailable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryBookItemViewHolder {
        val bookItem = LayoutInflater.from(parent.context).inflate(R.layout.library_book_item,
            parent, false)
        return LibraryBookItemViewHolder(bookItem)
    }

    override fun onBindViewHolder(holderLibrary: LibraryBookItemViewHolder, position: Int) {
        val item = libraryBookListFiltered[position]

        holderLibrary.itemView.setOnClickListener {
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra(LIBRARY_BOOK_ITEM, item)
                putExtra(BOOK_FRAGMENT_TYPE, LIBRARY)
            }
            context?.startActivity(intent)
        }

        Glide
                .with(holderLibrary.image.context)
                .load(item.imgUrl)
                .into(holderLibrary.image)

        holderLibrary.tvTitle.text = item.title
        holderLibrary.tvAuthors.text = item.authorsToString()
        holderLibrary.tvGenre.text = item.genre.getGenreName()
        holderLibrary.tvDescription.text = item.description
        holderLibrary.ivAvailability.setImageResource( if(item.isAvailable()) R.drawable.available else R.drawable.unavailable)
    }

    override fun getItemCount() = libraryBookListFiltered.size

    override fun getFilter() = filter

    private val filter:Filter = object:Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtered: MutableList<LibraryBookItem> = mutableListOf()
            if(constraint.toString().isEmpty()){
                filtered.addAll(libraryBookItemList)
            }else{
                for(bookItem: LibraryBookItem in libraryBookItemList){
                    if(bookItem.title.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtered.add(bookItem)
                    }
                    else if(bookItem.getAuthorsFullNameString().toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtered.add(bookItem)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filtered
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            libraryBookListFiltered.clear()
            libraryBookListFiltered.addAll(results?.values as Collection<LibraryBookItem>)
            noResultTv.visibility = if(libraryBookListFiltered.isEmpty()) View.VISIBLE else View.GONE
            notifyDataSetChanged()
        }

    }
}