package com.popkter.poplib.utils

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DataRxJavaHelper {

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DataRxJavaHelper() }
    }


    private val transientData: ConcurrentMap<Class<*>, UseCase?> = ConcurrentHashMap()
    private val publishSubject: PublishSubject<Class<*>> = PublishSubject.create()
    private val behaviorSubject: BehaviorSubject<Class<*>> = BehaviorSubject.create()

    /**
     * 发消息
     * @param useCase 消息被封装的类，需要继承 UseCase类
     */
    @Synchronized
    fun send(useCase: UseCase) {
        val useCaseClass: Class<out UseCase> = useCase.javaClass
        transientData[useCaseClass] = useCase
        publishSubject.onNext(useCaseClass)
    }

    /**
     * BehaviorSubject
     */
    fun sendBehavior(useCase: UseCase) {
        val useCaseClass: Class<out UseCase> = useCase.javaClass
        transientData[useCaseClass] = useCase
        behaviorSubject.onNext(useCaseClass)
    }

    /**
     * 主动获取消息
     * @param useCaseClass 消息被封装的类
     * @return 消息被封装的类
     */
    operator fun <T : UseCase?> get(useCaseClass: Class<T>): T? {
        val removedUseCase = transientData.remove(useCaseClass)
        return useCaseClass.cast(removedUseCase)
    }

    /**
     * 订阅消息，订阅后，可以接受到发送方的数据，但不会从 transientData 移除消息数据，适用1对多
     * @param useCaseClass 订阅的对象类
     * @return 订阅的对象类
     */
    fun <T : UseCase?> observerUseCase(useCaseClass: Class<T>): Observable<T> {
        return observeUseCase(useCaseClass).map { transientData[useCaseClass]!! }
            .cast(useCaseClass)
    }

    /**
     * 消费Behavior消息，订阅后，可以接受到发送方的数据，会从 transientData 移除消息数据，适用1对1。
     * @param useCaseClass
     * @param <T>
     * @return
    </T> */
    fun <T : UseCase?> costBehaviorUseCase(useCaseClass: Class<T>): Observable<T> {
        return observeBehaviorUseCase(useCaseClass).map { transientData.remove(useCaseClass)!! }
            .cast(useCaseClass)
    }

    /**
     * 消费普通消息，订阅后，可以接受到发送方的数据，会从 transientData 移除消息数据，适用1对1。
     * @param useCaseClass
     * @param <T>
     * @return
    </T> */
    fun <T : UseCase?> costUseCase(useCaseClass: Class<T>): Observable<T> {
        return observeUseCase(useCaseClass).map { transientData.remove(useCaseClass)!! }
            .cast(useCaseClass)
    }

    /**
     * 判断数据是否存在与 缓存中
     * @param useCaseClass
     * @param <T>
     * @return
    </T> */
    fun <T : UseCase?> containsUseCase(useCaseClass: Class<T>): Boolean {
        return transientData.containsKey(useCaseClass)
    }

    /**
     * 过滤对象
     * @param useCaseClass 目标usecase
     * @return true: 存在， false： 不存在
     */
    private fun observeUseCase(useCaseClass: Class<*>): Observable<Class<*>> {
        return publishSubject.filter { clazz ->
            clazz == useCaseClass && transientData.containsKey(useCaseClass)
                    && transientData[useCaseClass] != null
        }
    }

    /**
     * 过滤对象
     */
    private fun observeBehaviorUseCase(useCaseClass: Class<*>): Observable<Class<*>> {
        return behaviorSubject.filter { clazz ->
            clazz == useCaseClass && transientData.containsKey(useCaseClass)
                    && transientData[useCaseClass] != null
        }
    }
}

/*
object DataTransferHelper {
    private val myDispatcher by lazy { Executors.newSingleThreadExecutor().asCoroutineDispatcher() }
    private val flow by lazy { flow<UseCase> {} }
    private val data by lazy { ConcurrentHashMap<Class<*>, Any>() }

    fun <T : UseCase> emit(value: T) {
        flow.apply {
            println("emit ${value.javaClass} $value")
            emit(value)
            data[value.javaClass] = value
        }
    }

    fun <T : UseCase> handle(clazz: Class<T>, func: (value: T) -> Unit) {
        CoroutineScope(myDispatcher).launch {
            flow.collect {
                data.filter { dataKey ->
                    dataKey.key == clazz
                }.map { dataValue ->
                    println("handle ${dataValue.javaClass} ${dataValue.value}")
                    func.invoke(dataValue.value as T)
                }
            }
        }
    }
}*/

open class UseCase()
