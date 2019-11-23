package com.mvd.drunkgames.scores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvd.drunkgames.R
import com.mvd.drunkgames.entity.GameSession
import java.text.SimpleDateFormat
import java.util.*

class ScoresAdapter : RecyclerView.Adapter<ScoresAdapter.Holder>() {
    private var itemsList: ArrayList<GameSession>? = ArrayList()
    var formatter = SimpleDateFormat("dd MMMM yyyy")
    private var context: Context? = null

    var listData: MutableList<GameSession>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
            itemsList!!.addAll(this.listData!!)
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ScoresAdapter.Holder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.scores_item, viewGroup, false)
        context = viewGroup.context
        return Holder(view)
    }

    override fun getItemCount(): Int = listData!!.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScoresAdapter.Holder, position: Int) {
        val gameSession = listData!![position]
        holder.dateTextView.text = formatDate(gameSession.date)
        holder.roundsTextView.text =
            context?.resources!!.getString(R.string.rounds) + gameSession.rounds.toString()
    }

    inner class Holder : RecyclerView.ViewHolder {
        var dateTextView: TextView
        var roundsTextView: TextView

        constructor(view: View) : super(view) {
            dateTextView = view.findViewById(R.id.date_tv)
            roundsTextView = view.findViewById(R.id.rounds_tv)
        }

    }

    private fun formatDate(date: Long): String {
        return formatter.format(Date(date))
    }

}