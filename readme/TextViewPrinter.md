## TextView打字机效果

### 使用方法

1. 引用

```xml
<com.popkter.printer_textview.TextViewPrinter
            android:id="@+id/tv_set"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toTopOf="@id/btn_get"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
```

2. 设置

```kotlin
    /**
     * setText to TextView
     * [text] the Text you want to display
     * [interpolator] Interpolator, default is AccelerateDecelerateInterpolator
     * [singleCharDuration] single char load duration, default is 200L
     * [modifyPropOnLoad] apply modify text span When Load
     * [modifyPropOnEnd] apply modify text span after Load
     */
    fun updateText(
        text: CharSequence,
        interpolator: Interpolator = AccelerateDecelerateInterpolator(),
        singleCharDuration: Long = 200L,
        modifyPropOnEnd: Boolean = true,
        modifyPropOnLoad: Boolean = true
    ) {...}
```

### 效果预览

https://github.com/popkter/PopView/assets/74968459/28ac88cc-16b5-4715-8e55-294fd37d7629



