## Android屏蔽快速点击工具

> 可以实现自定义的间隔时间或者统一的间隔时间

### 使用方法

```kotlin
//默认时长 500ms 的点击事件屏蔽
button.setOnSingleClickListener { }
//自定义时长的点击事件屏蔽
button.setOnSingleClickListener(1000L){ }
```

