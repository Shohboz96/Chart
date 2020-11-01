package uz.max.anychart.room.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class UsersData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var age:Int,
    val lastName:String,
    val firstName:String
):Serializable{
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<UsersData>() {
            override fun areItemsTheSame(oldItem: UsersData, newItem: UsersData) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: UsersData, newItem: UsersData) = oldItem.age == newItem.age &&
                oldItem.lastName == newItem.lastName && oldItem.firstName == newItem.firstName
        }
    }
}