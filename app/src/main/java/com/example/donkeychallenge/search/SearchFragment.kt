package com.example.donkeychallenge.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donkeychallenge.databinding.FragmentSearchBinding
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.EventObserver
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

    private fun onHubClicked(hub: SearchResult) {
        viewModel.pickHub(hub)
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun search(query: String) {
        viewModel.search(query)
    }

    private fun initView() = with(binding) {
        textInput.addTextChangedListener(object : TextWatcher {
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
    private fun showError(throwable: Throwable) {
        Log.e("Donkey", "Coroutine error: ${throwable.message}")
        Toast.makeText(requireContext(), "Some error here!", Toast.LENGTH_SHORT).show()
    }

    private fun prepareLiveDataObserver() = with(viewModel) {
        searchResult.observe(viewLifecycleOwner, EventObserver { hubAdapter.updateList(it) })
        error.observe(viewLifecycleOwner, EventObserver {
            showError(it)
        })
    }

    companion object {

        private const val TEXT_WATCHER_DELAY = 500L

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
