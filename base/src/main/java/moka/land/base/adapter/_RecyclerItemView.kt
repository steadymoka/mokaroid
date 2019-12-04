package moka.land.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class _RecyclerItemView<DATA : Any>(parent: ViewGroup, resId: Int)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false)) {

    var index: Int = 0

    lateinit var data: DATA
    var preData: DATA? = null
    var afterData: DATA? = null

    open fun refreshView() {

    }

    open fun onRecycled() {
    }

    open fun onClickItem() {
    }

}
