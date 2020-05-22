@[TOC](微信小程序开发〖四〗项目篇)

**最近花了几个月的时间,搞了一下项目,也是总结了一下之前学习的经验,和把学到的中间件用了一遍,写了个伪秒杀,微信小程序前端用的VantUI,本人不是写前端的,所以页面可能有些丑,先看看大概吧~(源码在最后)**


**由于gif格式太大,我放到图片服务器上你们点开链接看看gif**


[微信小程序整体gif](http://39.96.86.4/GIF.gif)

[微信小程序生成Excel文档gif](http://39.96.86.4/excel.gif)


### 一. 基础页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154030957.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154046765.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154100970.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154108852.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)

### 二. 活动详情页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154203673.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
### 三. 用户页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154319467.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052215434553.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052215441154.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154457657.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
### 四. 社团页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154544919.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154553207.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)

### 五. 社团管理者页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154633216.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154653771.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154708554.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
**点开后可以查看Excel格式**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154729781.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522154807512.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)

### 六. 项目托管

[微信小程序项目地址](https://git.weixin.qq.com/1550196694/community.git)

### 制作不易,转载请标注~