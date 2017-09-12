# TraceDemo
运动类画轨迹等实现demo，包括锁屏，画轨迹，分享截图和记录等功能，基本满足单一类型App的功能了，同时我也写了后台服务，方便大家调试，后台使用tomcat服务，依赖fastjson和sqlite数据库。

运动类轨迹记录Demo（Android和Tomcat源码）

因为一些原因，这个我很早就写好了，知道公司的App不用这个功能了，大概连续3个版本后我才公开的，里面有一些问题欢迎指正，

QQ:1264957104

[个人主页：http://lujianchao.com](http://lujianchao.com)
 
[个人主页：http://itgowo.com](http://itgowo.com)


声明：文件下载使用的是我自己写的文件服务器，所有文件都是一个id表示，支持断点续传，同名（同路径）文件会被替换，从C盘移动到d盘再次上传会被认为是两个文件，同一个路径视为同一个文件进行更新，空间有限，只能这样了


[App和Server源码 下载地址1（非Github）](http://file.itgowo.com/download/project/TraceDemoSrcAll/TraceDemo%28App%2BServer%29.zip)

[App和Server源码 下载地址2（非Github）](http://lujianchao.com:8081/Server/file/download?fileid=58)

[Apk高速下载地址（非Github）](http://file.itgowo.com/download/project/TraceDemoSrcAll/TraceDemoApp.apk)

简单介绍

1:Android demo最开始页面

![image](https://github.com/hnsugar/TraceDemo/blob/master/0.png)

地图使用的是百度地图，单独申请的key，放到了源码中

默认进来就会获取总运动里程和总时间，切换运动类型可以更新数据

右上角一个是排行，因为demo没有用户区分处理，所以无效，另一个是记录，根据首页选择运动类型显示相应类型数据记录



![image](https://github.com/hnsugar/TraceDemo/blob/master/1.jpg)

因为在楼间定位不准确，只能这么看了

配速是单位路程所使用的时间，这里是千米每秒，显示格式为分秒，如果超过1小时会显示小时


![image](https://github.com/hnsugar/TraceDemo/blob/master/2.jpg)

按住锁屏按钮有动画并计时，计时到了全屏加一层遮罩；滑动解锁。


![image](https://github.com/hnsugar/TraceDemo/blob/master/3.jpg)

运动中可以点击下方距离时间区域，自动收回和展开，有动画


![image](https://github.com/hnsugar/TraceDemo/blob/master/4.jpg)

根据日期分割

列表支持刷新和加载更多，增加自定义方法，空数据有单独显示，默认加了上次刷新时间提示。


![image](https://github.com/hnsugar/TraceDemo/blob/master/5.jpg)

上面右侧是分享记录的路程,左上是用户名和备注信息,用画笔画的bitmap。


1:Tomcat 源码

使用了fastjson和sqlite-jdbc，不用担心数据库有密码泄露了(ˇˍˇ) 想～

Servlet是用IntelliJ IDEA开发的，只有一个Servlet做了上传记录获取记录和列表等多个功能，个人感觉一个接口就够用了

非专业开发，勿喷，有用中文做参数的

QQ：1264957104

个人主页:http://lujianchao.com

个人主页:http://itgowo.com
