package com.alphadevelopmentsolutions.frcscout.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.adapter.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchListBinding
import com.alphadevelopmentsolutions.frcscout.table.Match
import com.alphadevelopmentsolutions.frcscout.ui.MasterFragment

class MatchListFragment : MasterFragment()
{
    private val viewModel: MatchListViewModel by viewModels()

    private var matchList: MutableList<Match> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    private val recyclerViewAdapter by lazy {
        MatchListRecyclerViewAdapter(
            matchList,
            context
        )
    }

    private val layoutManager by lazy {
        LinearLayoutManager(
            context
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val binding = FragmentMatchListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.matchList.observe(context, matchObserver)

        binding.MatchListRecyclerView.layoutManager = layoutManager
        binding.MatchListRecyclerView.adapter = recyclerViewAdapter

        return binding.root
    }

    private val matchObserver: Observer<MutableList<Match>> =
        Observer<MutableList<Match>> {
            matchList = it
        }
}
