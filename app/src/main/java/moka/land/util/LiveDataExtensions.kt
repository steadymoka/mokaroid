package moka.land.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class NotNullMutableLiveData<T : Any>(defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue() = super.getValue()!!

}

fun <T, K, R> combineWith(liveData1: LiveData<T>, liveData2: LiveData<K>, block: (T?, K?) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(liveData1) {
        result.value = block.invoke(liveData1.value, liveData2.value)
    }
    result.addSource(liveData2) {
        result.value = block.invoke(liveData1.value, liveData2.value)
    }
    return result
}

fun <T> MutableLiveData<ArrayList<T>>.addValues(values: ArrayList<T>) {
    this.value?.addAll(values)
    this.value = this.value
}
