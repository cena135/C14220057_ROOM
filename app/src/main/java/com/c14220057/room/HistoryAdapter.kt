package com.c14220057.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class HistoryAdapter(
    private val historyList: MutableList<historyBarang>,
    private val onDeleteClick: (historyBarang) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvItem: TextView = itemView.findViewById(R.id.tvItem)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val btnDeleteHistory: ImageButton = itemView.findViewById(R.id.btnDeleteHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.tvTanggal.text = history.tanggal
        holder.tvItem.text = history.item
        holder.tvJumlah.text = history.jumlah

        holder.btnDeleteHistory.setOnClickListener {
            onDeleteClick(history)
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun removeItem(history: historyBarang) {
        val position = historyList.indexOf(history)
        if (position != -1) {
            historyList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

