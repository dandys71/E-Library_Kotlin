package cz.uhk.fim.student.schmid.kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.uhk.fim.student.schmid.R
import cz.uhk.fim.student.schmid.kotlin.adapter.BookItemAdapter
import cz.uhk.fim.student.schmid.kotlin.viewmodel.MyLibraryViewModel

class MyLibraryFragment : Fragment() {

    private lateinit var myLibraryViewModel: MyLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myLibraryViewModel =
                ViewModelProvider(this).get(MyLibraryViewModel::class.java)
        myLibraryViewModel.init();
        val root = inflater.inflate(R.layout.fragment_my_library, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_my_library_book_items)

        recyclerView?.layoutManager = GridLayoutManager(context, 2)
        recyclerView?.setHasFixedSize(true)

        val noDataTv: TextView = root.findViewById(R.id.my_library_no_data)

        myLibraryViewModel.books?.observe(viewLifecycleOwner, Observer {
            noDataTv.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            recyclerView?.adapter = BookItemAdapter(
                context,
                it,
                BookDetailActivity.Companion.FRAGMENT.MY_LIBRARY
            )

        })

        return root
    }
}