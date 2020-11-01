package uz.max.anychart.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_show.view.*
import uz.max.anychart.R
import uz.max.anychart.adapters.FloatAdapter
import uz.max.anychart.adapters.UserAdapter
import uz.max.anychart.data.ValueData
import uz.max.anychart.room.entities.UsersData
import uz.max.anychart.room.entities.ValuesAllData

class FloatDialog(context: Context, list: List<ValuesAllData>) : AlertDialog(context){
    private val contentView = LayoutInflater.from(context).inflate(R.layout.activity_show,null,false)
    private val floatAdapter = FloatAdapter()
    init {
        setView(contentView)

        contentView.recyclerView.layoutManager = LinearLayoutManager(context)
        contentView.recyclerView.adapter = floatAdapter

        floatAdapter.submitList(list)
        contentView.top_text.text = "Daily Money"
        var min = list[0].value
        var max = 0f
        var mid = 0f
        var d: Float
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
        contentView.middle.text = (mid / list.size).toString()

        contentView.btn_back.setOnClickListener { dismiss() }
    }


}