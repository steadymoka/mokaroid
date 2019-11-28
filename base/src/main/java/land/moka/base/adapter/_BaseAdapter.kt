package land.moka.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

abstract class _BaseAdapter<DATA : Any, in VIEW : _RecyclerItemView<DATA>> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<DATA> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder as VIEW
        if (0 < position) {
            itemView.preData = items[position - 1]
        }
        else {
            itemView.preData = null
        }

        if (items.size > position + 1) {
            itemView.afterData = items[position + 1]
        }
        else {
            itemView.afterData = null
        }

        val data = items[position]
        itemView.index = position
        itemView.data = data
        itemView.refreshView(data)
    }

}
