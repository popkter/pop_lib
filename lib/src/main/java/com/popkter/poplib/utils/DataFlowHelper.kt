package com.popkter.poplib.utils

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class DataFlowHelper: CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {

    companion object {
        private const val TAG = "DataFlow"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DataFlowHelper() }
    }

    /**
     * SharedFlowMap, every Class has own sharedFlow,cache size default is one
     */
    private val mShareFlowDataMap = mutableMapOf<Class<*>, MutableSharedFlow<Any>>()

    /**
     * StateFlow,like SharedFlowMap,support deBounce
     */
    private val mStateFlowDataMap = mutableMapOf<Class<*>, MutableStateFlow<Any>>()

    /**
     * sharedFlow emit data
     * [T] customized object
     */
    fun <T : Any> postSharedFlow(t: T) {
        Log.i(TAG, "postSharedFlow: ${t.javaClass}")
        launch {
            if (!mShareFlowDataMap.containsKey(t.javaClass)) {
                mShareFlowDataMap[t.javaClass] = MutableSharedFlow(1)
            }
            mShareFlowDataMap[t.javaClass]?.emit(t)
            cancel()
        }
    }


    /**
     * sharedFlow collect data
     * [t] customized class
     * [block] the function to use result,support suspend
     */
    fun <T : Any> collectSharedFlow(t: Class<T>, block: suspend (T) -> Unit) {
        Log.i(TAG, "collectSharedFlow: $t")
        launch {
            if (!mShareFlowDataMap.containsKey(t)) {
                mShareFlowDataMap[t] = MutableSharedFlow(1)
            }
            mShareFlowDataMap[t]?.collect {
                block.invoke(it as T)
            }
            cancel()
        }
    }

    /**
     * stateFlow emit data
     * [T] customized object
     */
    fun <T : Any> postStateFlow(t: T) {
        Log.i(TAG, "postStateFlow: ${t.javaClass}")
        launch {
            if (!mStateFlowDataMap.containsKey(t.javaClass)) {
                mStateFlowDataMap[t.javaClass] = MutableStateFlow("")
            }
            mStateFlowDataMap[t.javaClass]?.emit(t)
            cancel()
        }
    }

    /**
     * stateFlow collect data
     * [t] customized class
     * [block] the function to use result,support suspend
     */
    fun <T : Any> collectStateFlow(t: Class<T>, block: suspend (T) -> Unit) {
        Log.i(TAG, "collectStateFlow: $t")
        launch {
            mStateFlowDataMap[t]?.collect {
                block.invoke(it as T)
            }
            cancel()
        }
    }

    /**
     * Check whether the current [clazz] Type exists in the Map
     */
    private fun <T> checkExistShareFlow(clazz: Class<T>) {
        if (!mShareFlowDataMap.containsKey(clazz)) {
            mShareFlowDataMap[clazz] = MutableSharedFlow(1)
            Log.w(TAG, "checkExistShareFlow: Create New One")
        } else {
            Log.w(TAG, "checkExistShareFlow: Exist")
        }
    }

    /**
     * Check whether the current [clazz] Type exists in the Map
     */
    private fun <T> checkExistStateFlow(clazz: Class<T>) {
        if (!mStateFlowDataMap.containsKey(clazz)) {
            mStateFlowDataMap[clazz] = MutableStateFlow("")
            Log.w(TAG, "checkExistStateFlow: Create New One")
        } else {
            Log.w(TAG, "checkExistStateFlow: Exist")
        }
    }
}