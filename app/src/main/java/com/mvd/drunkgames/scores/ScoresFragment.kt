package com.mvd.drunkgames.scores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvd.drunkgames.R
import kotlinx.android.synthetic.main.fragment_scores.*

class ScoresFragment : Fragment() {

    private lateinit var viewModel: ScoresViewModel

    companion object {
        private const val ID_EXTRA = "ID_EXTRA"
        fun getInstance(id: String): ScoresFragment {
            val fragment = ScoresFragment()
            val bundle = Bundle()
            bundle.putString(ID_EXTRA, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this)[ScoresViewModel::class.java]
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString(ID_EXTRA)
        if (id != null) {
            viewModel.setUserId(id)
        } else {

        }

        viewModel.scoresLoaded.observe(this, Observer<ScoresAdapter> {
            if (it != null) {
                scores_rv.adapter = viewModel.adapter
                val layoutManager = LinearLayoutManager(context)
                scores_rv.layoutManager = layoutManager
            }
        })
    }
}