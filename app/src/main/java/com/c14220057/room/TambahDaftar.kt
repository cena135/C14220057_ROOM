package com.c14220057.room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.c14220057.room.database.daftarBelanja
import com.c14220057.room.database.daftarBelanjaDB

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uts.c14220057.room.helper.DateHelper.getCurrentDate

class TambahDaftar : AppCompatActivity() {

    private lateinit var _etItem: EditText
    private lateinit var _etJumlah: EditText
    private lateinit var _btnTambah: Button
    private lateinit var _btnUpdate: Button

    var tanggal = getCurrentDate()
    var DB = daftarBelanjaDB.getDatabase(this)
    var iID: Int = 0
    var iAddEdit: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)

        _etItem = findViewById(R.id.etItem)
        _etJumlah = findViewById(R.id.etJumlah)
        _btnTambah = findViewById(R.id.btnTambah)
        _btnUpdate = findViewById(R.id.btnUpdate)

        iID = intent.getIntExtra("id", 0)
        iAddEdit = intent.getIntExtra("addEdit", 0)

        if (iAddEdit == 0) {
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etItem.isEnabled = true
        } else {
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etItem.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                val item = DB.fundaftarBelanjaDAO().getItem(iID)
                withContext(Dispatchers.Main) {
                    _etItem.setText(item.item)
                    _etJumlah.setText(item.jumlah)
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.actTambahDaftar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _btnTambah.setOnClickListener {
            val itemText = _etItem.text.toString()
            val jumlahText = _etJumlah.text.toString()

            if (itemText.isEmpty() || jumlahText.isEmpty()) {
                Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                DB.fundaftarBelanjaDAO().insert(
                    daftarBelanja(
                        tanggal = tanggal,
                        item = itemText,
                        jumlah = jumlahText
                    )
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TambahDaftar, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TambahDaftar, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                DB.fundaftarBelanjaDAO().update(
                    isi_tanggal = tanggal,
                    isi_item = _etItem.text.toString(),
                    isi_jumlah = _etJumlah.text.toString(),
                    pilihid = iID
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TambahDaftar, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
