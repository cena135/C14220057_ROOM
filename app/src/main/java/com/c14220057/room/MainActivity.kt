package com.c14220057.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.c14220057.room.database.daftarBelanja
import com.c14220057.room.database.daftarBelanjaDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

lateinit var DB: daftarBelanjaDB
lateinit var adpDaftar: adapterDaftar
private var arDaftar: MutableList<daftarBelanja> = mutableListOf()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi database
        DB = daftarBelanjaDB.getDatabase(this)

        // OnClickListener untuk FloatingActionButton
        val fabAdd =
            findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }

        // Inisialisasi adapter dan RecyclerView
        adpDaftar = adapterDaftar(arDaftar)

        val _rvDaftar = findViewById<RecyclerView>(R.id.rvNotes)
        _rvDaftar.layoutManager = LinearLayoutManager(this)
        _rvDaftar.adapter = adpDaftar

        val fabHistory: FloatingActionButton = findViewById(R.id.fabBtnHistory)
        fabHistory.setOnClickListener {
            // Navigasi ke halaman HistoryActivity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }


        adpDaftar.setOnItemClickCallback(
            object : adapterDaftar.OnItemClickCallback {
                override fun delData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).launch {
                        DB.fundaftarBelanjaDAO().delete(dtBelanja)
                        loadData()
                    }
                }
            }
        )

        val _searchView = findViewById<SearchView>(R.id.searchView)
        _searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchDatabase(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchDatabase(it) }
                return false
            }
        })
    }


    override fun onResume() {
        super.onResume()
        // Memuat ulang data setiap kali aktivitas kembali aktif
        CoroutineScope(Dispatchers.IO).launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        val daftarBelanja = DB.fundaftarBelanjaDAO().selectAll()
        withContext(Dispatchers.Main) {
            Log.d("data ROOM", daftarBelanja.toString())
            adpDaftar.isiData(daftarBelanja)
        }
    }

    private fun searchDatabase(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val daftarBelanja = DB.fundaftarBelanjaDAO()
                .selectAll()
                .filter {
                    it.item?.contains(query, ignoreCase = true) == true ||
                            it.jumlah?.contains(query, ignoreCase = true) == true
                }

            withContext(Dispatchers.Main) {
                adpDaftar.isiData(daftarBelanja)
            }
        }
    }
}
