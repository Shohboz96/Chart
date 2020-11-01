package uz.max.anychart.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_show.view.*
import uz.max.anychart.R
import uz.max.anychart.adapters.ManufacAdapter
import uz.max.anychart.adapters.UserAdapter
import uz.max.anychart.room.entities.ProductsData
import uz.max.anychart.room.entities.UsersData

class UserDialog(context: Context, list: List<UsersData>) : AlertDialog(context){
    private val contentView = LayoutInflater.from(context).inflate(R.layout.activity_show,null,false)
    val userAdapter = UserAdapter()
    init {
        setView(contentView)


        contentView.recyclerView.layoutManager = LinearLayoutManager(context)
        contentView.recyclerView.adapter = userAdapter
       // window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.Animation_Design_BottomSheetDialog

            userAdapter.submitList(list)
            contentView.top_text.text = "Users"
            var min = list[0].age
            var max = list[0].age
            var mid = 0
            var d : Any
            for (i in list.indices){
                d = list[i].age
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