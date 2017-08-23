# RadarView
RaderView 雷达图

## 演示
![RadarView](https://github.com/MiraclesHed/RadarView/blob/master/gif/device-2017-08-23-163603.mp4_1503477877.gif)

## 属性

- `borderColor`:网格线颜色
- `branchColor`:由中心发出的分支线的颜色
- `contentColor`:数值区域颜色
- `pointColor`:数值点颜色
- `isShowBorder`:是否显示网格线
- `isShowBranch`:是否显示分支线
- `isShowPoint`:是否显示数值点
- `borderCount`:网格线数量
- `max`:数值最大值
- `shapeType`:雷达图形状,如`HEXAGON`
> `shapeType`目前雷达图只支持正六边形(HEXAGON)


## 方法
- `setValues(float[] values)`:设置各个属性的值

## XML

```
    <com.miracleshed.raderview.RadarView
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:background="@color/colorPrimary"
        app:borderColor="@android:color/white"
        app:branchColor="@android:color/white"
        app:contentColor="@android:color/white"
        app:pointColor="@android:color/holo_red_dark"
        app:isShowBorder="true"
        app:isShowBranch="true"
        app:isShowPoint="true"
        app:borderCount="4"
        app:max="100"
        app:shapeType="HEXAGON"
        />
```


