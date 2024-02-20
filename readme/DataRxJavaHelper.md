> 基于RxJava的线程通讯工具

### 使用方法

- 传递数据

> 发送的数据必须继承UseCase类

```kotlin
//不会缓存的数据
DataRxJavaHelper.INSTANCE.send(UseCase())
//可以缓存的数据，如果没有被消费会保存
DataRxJavaHelper.INSTANCE.sendBehavior(UseCase)
```

- 接收数据

```kotlin
//订阅该类型的数据
DataRxJavaHelper.INSTANCE.observerUseCase(UseCase::class.java)
//订阅该类型的数据并在收到该数据后移除，类似有序广播的拦截
DataRxJavaHelper.INSTANCE.costUseCase(UseCase::class.java)
//订阅该类型的数据并在收到该数据后移除，类似有序广播的拦截
DataRxJavaHelper.INSTANCE.costBehaviorUseCase(UseCase::class.java)
```

