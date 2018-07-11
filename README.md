# RxFrameAnimPlayer
`Optimization of large frame animation without OutOfMemory`

##### 使用了RxJava2，用很少的内存，加载大量的图片帧动画。帮助你避免出现内存错误。<br>正常情况使用Imageview自带的AnimationDrawable一次加载大量的序列图片，很容易出现OutOfMemory。

## Preview
![](preview.gif)

## Usage
```
RxFrameAnimPlayer.with(imageView)
                 .frames(IMAGE_RESOURCES)
                 .listener(((run, position) -> {}))
                 .performInterval(130, TimeUnit.MILLISECONDS)
                 .start();
```
