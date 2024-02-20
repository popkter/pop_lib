## Android SharePreference工具类

> 简化sp的使用

### 使用方法

```kotlin
//初始化
val sharePreferenceHelper = SharePreferenceHelper(mContext: Context, name:String)
//api
sharePreferenceHelper.put(key:String,value:Any)
sharePreferenceHelper.get(key:String,defaultObject:T)
```

