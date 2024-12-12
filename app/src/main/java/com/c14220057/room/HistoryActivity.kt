package com.c14220057.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var rvHistory: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_barang_layout)

        rvHistory = findViewById(R.id.rvHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            val historyList = DB.funHistoryBarangDAO().getAll().toMutableList()
            withContext(Dispatchers.Main) {
                historyAdapter = HistoryAdapter(historyList) { history ->
                    deleteHistory(history)
                }
                rvHistory.adapter = historyAdapter
            }
        }
    }

    private fun deleteHistory(history: historyBarang) {
        CoroutineScope(Dispatchers.IO).launch {
            DB.funHistoryBarangDAO().delete(history) // Tambahkan fungsi delete di DAO jika belum ada
            withContext(Dispatchers.Main) {
                historyAdapter.removeItem(history)
            }
        }
    }
}
