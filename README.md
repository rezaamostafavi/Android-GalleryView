# Android-GalleryView
A beautiful Gallery View (cover flow) for android platform , base on RecyclerView.

## Gif
<img src="https://github.com/rezaamostafavi/Android-GalleryView/blob/master/gif/help.gif?raw=true" width="40%" height="40%">

## Build
[![](https://jitpack.io/v/rezaamostafavi/Android-GalleryView.svg)](https://jitpack.io/#rezaamostafavi/Android-GalleryView)
##### Step 1. Add the JitPack repository to your build file
```build
allprojects {
	repositories {
			...
			maven { url "https://jitpack.io" }
	}
}
```

##### Step 2. Add the dependency
```build 
dependencies {
	compile 'com.github.rezaamostafavi:Android-GalleryView:v1.0.8'
}
```
	
## Layout

```layout
	<com.sharifin.galleryview.GalleryView
       android:id="@+id/galleryView"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:rotationY="0.25"
       app:scale="0.15" />
```

```java
 RecyclerView.Adapter mAdapter = ...
 
 GalleryView galleryView = new GalleryView(mContext);
 galleryView.setAdapter(mAdapter);
 
 galleryView.setRotationY(0.2f);
 galleryView.setScale(0.15f);
```	   

```java
 galleryView.setOnCenterViewClickListener(new GalleryView.OnCenterViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            }
        });
```

# Lincense
```lincense
The MIT License (MIT)

Copyright (c) 2017 Reza Mostafavi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

Status API Training Shop Blog About Pricing
```


