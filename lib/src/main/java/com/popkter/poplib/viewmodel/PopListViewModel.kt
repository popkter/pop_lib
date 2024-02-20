package com.popkter.poplib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@Suppress("UNCHECKED_CAST")
open class PopListViewModel : ViewModel() {
    val dataSet: LiveData<List<Nothing>>
        get() = _dataSet as LiveData<List<Nothing>>
    protected val _dataSet: MutableLiveData<List<*>> = MutableLiveData()

    val loadMoreDataSet: LiveData<List<Nothing>>
        get() = _loadMoreDataSet as LiveData<List<Nothing>>
    protected val _loadMoreDataSet: MutableLiveData<List<*>> = MutableLiveData()

    open suspend fun loadMore() {}

    open suspend fun onRefresh() {}
}