package uz.max.anychart.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_show.view.*
import uz.max.anychart.R
import uz.max.anychart.adapters.FloatAdapter
import uz.max.anychart.adapters.ValueAdapter
import uz.max.anychart.data.ValueData
import uz.max.anychart.room.entities.ValuesAllData

class ValueDialog(context: Context, list: List<ValueData>,status:String) : AlertDialog(context){
    private val contentView = LayoutInflater.from(context).inflate(R.layout.activity_show,null,false)
    val valuAdapter = ValueAdapter()
    init {
        setView(contentView)

        contentView.recyclerView.layoutManager = LinearLayoutManager(context)
        contentView.recyclerView.adapter = valuAdapter
        valuAdapter.submitList(list)
        contentView.top_text.text = status
        var min = list[0].value
        var max = list[0].value
        var mid = 0.0
        var d : Any
        for (i in list.indices){
            d = list[i].value
            mid += d
            if(min > d){
                min = d
            }
            if(max < d){
                max = d
            }
        }
        contentView.minimum.text = min.toString()
        contentView.maximum.text = max.toString()
        contentView.middle.text = (mid / list.size).toInt().toString()

        contentView.btn_back.setOnClickListener { dismiss() }
    }


}