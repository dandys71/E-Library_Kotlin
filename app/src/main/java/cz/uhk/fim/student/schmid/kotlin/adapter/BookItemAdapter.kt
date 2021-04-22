package cz.uhk.fim.student.schmid.kotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.uhk.fim.student.schmid.R
import cz.uhk.fim.student.schmid.kotlin.model.BookItem
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity.Companion.BOOK_FRAGMENT_TYPE
import cz.uhk.fim.student.schmid.kotlin.ui.BookDetailActivity.Companion.BOOK_ITEM


class BookItemAdapter(private val context: Context?, private val libraryBookList: List<BookItem>, private val fragment: BookDetailActivity.Companion.FRAGMENT) : RecyclerView.Adapter<BookItemAdapter.BookItemViewHolder>() {

    class BookItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_book)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthors: TextView = itemView.findViewById(R.id.tvAuthors)
        val btnDetail: Button = itemView.findViewById(R.id.btnDetail)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        val bookItem = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookItemViewHolder(bookItem)
    }

    override fun onBindViewHolder(holderBookItem: BookItemViewHolder, position: Int) {
        val item = libraryBookList[position]

        Glide
                .with(holderBookItem.image.context)
                .load(item.imgUrl)
                .into(holderBookItem.image)

        holderBookItem.tvTitle.text = item.title
        holderBookItem.tvAuthors.text = item.authorsToString()
        holderBookItem.btnDetail.setOnClickListener {
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra(BOOK_ITEM, item)
                putExtra(BOOK_FRAGMENT_TYPE, fragment)
            }
            context?.startActivity(intent)
        }
    }

    override fun getItemCount() = libraryBookList.size

}