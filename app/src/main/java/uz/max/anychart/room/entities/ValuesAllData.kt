package uz.max.anychart.room.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ValuesAllData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val value: Float,
    val belong: Int
): Serializable {
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<ValuesAllData>() {
            override fun areItemsTheSame(oldItem: ValuesAllData, newItem: ValuesAllData) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ValuesAllData,
                newItem: ValuesAllData
            ) = oldItem.value == newItem.value && oldItem.belong == newItem.belong
        }
    }
}