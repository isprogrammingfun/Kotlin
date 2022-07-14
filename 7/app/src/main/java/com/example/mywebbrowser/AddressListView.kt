package com.example.mywebbrowser

import ListViewAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.mywebbrowser.databinding.ActivityItemBinding

class AddressListView : AppCompatActivity() {
    var list = arrayListOf<ListViewItem>(
        ListViewItem("J", "1234"),
        ListViewItem("S", "2345"),
        ListViewItem("K", "3456"),
        ListViewItem("L", "4567")
    )


    private val binding by lazy {
        ActivityItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listAdapter = ListViewAdapter(this, list)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = listAdapter
    }
}