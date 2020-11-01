package uz.max.anychart.room.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProductsData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val modelName: String,
    val serial: String,
    val manufacturer: String
): Serializable {
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<ProductsData>() {
            override fun areItemsTheSame(oldItem: ProductsData, newItem: ProductsData)= oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductsData, newItem: ProductsData)= oldItem.modelName == newItem.modelName &&
                    oldItem.serial == newItem.serial && oldItem.manufacturer == newItem.manufacturer
        }
    }
}