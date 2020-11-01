package uz.max.anychart.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_manufac.view.*
import uz.max.anychart.R
import uz.max.anychart.room.entities.ProductsData
import uz.max.anychart.utils.bindItem
import uz.max.anychart.utils.inflate

class ManufacAdapter: ListAdapter<ProductsData, ManufacAdapter.VHolder>(
    ProductsData.ITEM_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VHolder(parent.inflate(R.layout.item_manufac))

    override fun onBindViewHolder(holder: VHolder, position: Int)  = holder.bind()
    inner class VHolder(view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind() = bindItem {
            val d = getItem(adapterPosition)
           manufac.text = d.manufacturer
           serial.text = d.serial
           model_name.text = d.modelName
        }
    }
}