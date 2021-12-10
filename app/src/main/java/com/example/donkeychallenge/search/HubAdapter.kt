package com.example.donkeychallenge.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donkeychallenge.R
import com.example.donkeychallenge.databinding.HubRowBinding
import com.example.donkeychallenge.model.SearchResult

class HubAdapter(private val onHubClick: (result: SearchResult) -> Unit) : RecyclerView.Adapter<HubAdapter.HubViewHolder>() {

    private var searchResults: List<SearchResult> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder =
        HubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hub_row, parent, false))

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        holder.bind(searchResults[position], onHubClick)
    }

    override fun getItemCount(): Int = searchResults.size

    fun updateList(newResult: List<SearchResult>) {
        searchResults = newResult
        notifyDataSetChanged()
    }

    class HubViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = HubRowBinding.bind(view)

        fun bind(result: SearchResult, onHubClick: (result: SearchResult) -> Unit) = with(binding) {
            root.setOnClickListener { onHubClick(result) }
            hubName.text = result.name
        }
    }
}
