package com.example.donkeychallenge.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donkeychallenge.databinding.FragmentSearchBinding
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.model.SearchResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.schedule

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by sharedViewModel<MainViewModel>()
    private val hubAdapter = HubAdapter(::onHubClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        prepareLiveDataObserver()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearSearchResults()
    }

    private fun onHubClicked(hub: SearchResult) {
        Log.e("klop", "On Hub Clicked: ${hub.name}")
        viewModel.pickHub(hub)
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun search(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.search(query)
        }
    }

    private fun initView() = with(binding) {
        textInput.addTextChangedListener(object : TextWatcher {
            //TODO add as extension?
            var timer: Timer? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} //not used
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(text: Editable?) {
                timer = Timer()
                timer?.schedule(TEXT_WATCHER_DELAY) {
                    search(text.toString())
                }
            }
        })

        searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hubAdapter
        }
    }

    private fun prepareLiveDataObserver() {
        viewModel.searchResult.observe(viewLifecycleOwner, { hubAdapter.updateList(it) })
    }

    companion object {

        private const val TEXT_WATCHER_DELAY = 500L

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}