package uz.max.anychart.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.max.anychart.room.entities.ValuesAllData
import java.io.Serializable

data class ValueData(
    var value: Int
){
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<ValueData>() {
            override fun areItemsTheSame(oldItem: ValueData, newItem: ValueData) = oldItem.value == newItem.value

            override fun areContentsTheSame(oldItem: ValueData, newItem: ValueData) = oldItem.value == newItem.value
        }
    }
}