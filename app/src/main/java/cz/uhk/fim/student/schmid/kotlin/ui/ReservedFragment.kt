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
import cz.uhk.fim.student.schmid.kotlin.viewmodel.ReservedViewModel

class ReservedFragment : Fragment() {

    private lateinit var reservedViewModel: ReservedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataTv:TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        reservedViewModel =
                ViewModelProvider(this).get(ReservedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reserved, container, false)

        recyclerView = root.findViewById(R.id.rv_reserved_book_items)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

        reservedViewModel.init()

        noDataTv= root.findViewById(R.id.reserved_no_data)

        return root
    }

    override fun onResume() {

        reservedViewModel.books?.observe(viewLifecycleOwner, Observer{
            noDataTv.visibility = if(it.isNotEmpty()) View.GONE else View.VISIBLE
            recyclerView.adapter =
                BookItemAdapter(context, it, BookDetailActivity.Companion.FRAGMENT.RESERVED)
        })

        super.onResume()
    }
}