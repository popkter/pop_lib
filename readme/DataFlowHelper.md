
## 基于`SharedFlow`的线程通讯工具

### 使用方法

- 传递数据

> 发送的数据可以是任意类型，但需要注意，接收处也需要是相同类型

```kotlin
//SharedFlow[replay = 1]
DataFlowHelper.instance.postSharedFlow(1)
//StateFlow
DataFlowHelper.instance.postStateFlow(1)
```

- 接收数据

```kotlin
//SharedFlow[replay = 1],接收一个lambda处理数据
DataFlowHelper.instance.collectSharedFlow(Integer::class.java){it->}
//StateFlow
DataFlowHelper.instance.collectStateFlow(Integer::class.java){it->}
```

