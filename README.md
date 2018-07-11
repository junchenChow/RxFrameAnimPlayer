# RxFrameAnimPlayer
`Optimization of large frame animation without OutOfMemory`

`正常情况使用Imageview自带的AnimationDrawable一次加载大量的序列图片，很容易出现OutOfMemory。`

## Usage
```java
RxFrameAnimPlayer.with(imageView)
                 .frames(IMAGE_RESOURCES)
                 .listener(((run, position) -> {}))
                 .performInterval(130, TimeUnit.MILLISECONDS)
                 .start();
```

## Preview
![](preview.gif)

## About this project
`附送一个动画小demo`

# Licence

```
Copyright 2018, junchenChow

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

