package uz.max.anychart.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_users.view.*
import uz.max.anychart.R
import uz.max.anychart.room.entities.UsersData
import uz.max.anychart.utils.bindItem
import uz.max.anychart.utils.inflate

class UserAdapter:ListAdapter<UsersData,UserAdapter.VHolder>(
    UsersData.ITEM_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VHolder(parent.inflate(R.layout.item_users))

    override fun onBindViewHolder(holder: VHolder, position: Int)  = holder.bind()
    inner class VHolder(view:View):RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind() = bindItem {
            val d = getItem(adapterPosition)
            name.text = "${d.firstName} ${d.lastName}"
            age.text = d.age.toString()+" years old"
        }
    }
}