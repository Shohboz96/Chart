package uz.max.anychart.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_show.view.*
import uz.max.anychart.R
import uz.max.anychart.adapters.ManufacAdapter
import uz.max.anychart.room.entities.ProductsData

class ProductDialog(context: Context, list: List<ProductsData>) : AlertDialog(context){
    private val contentView = LayoutInflater.from(context).inflate(R.layout.activity_show,null,false)
    private val manuAdapter = ManufacAdapter()
    init {
        setView(contentView)


        contentView.recyclerView.layoutManager = LinearLayoutManager(context)
        contentView.recyclerView.adapter = manuAdapter
            manuAdapter.submitList(list)

        contentView.top_text.text = "Products"
        contentView.min_max_card.visibility = View.GONE

        contentView.btn_back.setOnClickListener { dismiss() }
        }


    }
