## Android 自定义View实现的取色盘

### 使用方法

1. 属性设置

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="color_picker">
        <!-- 背景圆的半径 -->
        <attr name="circle_radius" format="dimension" />
        <!-- 可滑动小球的半径 -->
        <attr name="center_radius" format="dimension" />
        <!-- 可滑动小球的颜色 -->
        <attr name="center_color" format="color" />
        <!-- 圆环还是原型取色盘 -->
        <attr name="is_ring" format="boolean" />
        <!-- 圆环的半径 -->
        <attr name="ring_radius" format="dimension" />
    </declare-styleable>
</resources>
```

2. 布局设置

```xml
    <com.popkter.colorpickview.ColorPickerView
    android:id="@+id/color_picker_view"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:layout_gravity="center"
    app:center_color="#ffffff"
    app:center_radius="13dp"
    app:circle_radius="100dp"
    app:is_ring="false"
    app:ring_radius="20dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    android:layout_marginBottom="20dp"/>
```

3. 状态监听

```java
//监听颜色变化，返回一个hsb的值，可通过hsbToRgb(float[] hsb)方法转为rgb值
mColorPickerView.setOnColorChangedListener(hsb -> {});
```

