package cz.uhk.fim.student.schmid.kotlin.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.uhk.fim.student.schmid.R
import cz.uhk.fim.student.schmid.kotlin.adapter.LibraryBookItemAdapter
import cz.uhk.fim.student.schmid.kotlin.model.LibraryBookItem
import cz.uhk.fim.student.schmid.kotlin.viewmodel.LibraryViewModel


class LibraryFragment : Fragment() {

    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var recyclerView:RecyclerView
    private lateinit var noResultTv:TextView;


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        libraryViewModel =
                ViewModelProvider(this).get(LibraryViewModel::class.java)
        libraryViewModel.init()
        val root = inflater.inflate(R.layout.fragment_library, container, false)

        recyclerView = root.findViewById(R.id.rv_library_book_items)

        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.setHasFixedSize(true)

        noResultTv =  root.findViewById(R.id.library_no_result)

        setHasOptionsMenu(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        libraryViewModel.books?.observe(viewLifecycleOwner, Observer<List<LibraryBookItem>> {
            recyclerView.adapter = LibraryBookItemAdapter(context, it, noResultTv)
        })
    }

   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = resources.getText(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (recyclerView.adapter as LibraryBookItemAdapter).filter.filter(newText)
                return false
            }
        })
    }
}