# **Tiny**
an image compression framework.

----

[ ![Download](https://api.bintray.com/packages/sunzxyong/maven/Tiny/images/download.svg) ](https://bintray.com/sunzxyong/maven/Tiny/_latestVersion)[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)]() [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)]() ![](https://img.shields.io/badge/architecture-clean-yellow.svg)

[Blog entry with introduction](http://zhengxiaoyong.me/2017/04/23/Android%E5%9B%BE%E7%89%87%E5%8E%8B%E7%BC%A9%E6%A1%86%E6%9E%B6-Tiny/)
or
[Introduction of compression](http://zhengxiaoyong.me/2017/04/23/%E4%B9%9F%E8%B0%88%E5%9B%BE%E7%89%87%E5%8E%8B%E7%BC%A9/)


## **Effect of compression**

| ImageInfo | Tiny | Wechat |
| :-: | :-: | :-: |
6.66MB (3500x2156) | 151KB (1280x788) | 135KB (1280x788)|
4.28MB (4160x3120) | 219KB (1280x960)| 195KB (1280x960)|
2.60MB (4032x3024) | 193KB (1280x960)| 173KB (1280x960)|
372KB (500x500) | 38.67KB (500x500) | 34.05KB (500x500)|
236KB (960x1280) | 127KB (960x1280) | 118KB (960x1280)|

## **Introduce**
`Tiny` does not depend on any library , it keeps the code clean on architecture . `Tiny` also uses an asynchronous thread pool to compress images , and will hand out the result in the main thread when compression is completed.

## **Usage**
### **Installation**

```
compile 'com.zxy.android:tiny:0.0.6'
```

### **Choose an abi**
**Tiny** provide abi：`armeabi`、`armeabi-v7a`、`arm64-v8a`、`x86`.

Choose what you need **"abi"** version：

```
android {
    defaultConfig {
        ndk {
            abiFilters 'armeabi','x86'//or armeabi-v7a、arm64-v8a、x86
        }
    }
}
```

### **Initialization**

```
        Tiny.getInstance().init(this);
```
### **Compression**

#### **AsBitmap**

```
        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
        //options.height = xxx;//some compression configuration.
        Tiny.getInstance().source("").asBitmap().withOptions(options).compress(new BitmapCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap bitmap) {
                //return the compressed bitmap object
            }
        });
```

#### **AsFile**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //return the compressed file path
            }
        });
```
#### **AsFileWithReturnBitmap**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").asFile().withOptions(options).compress(new FileWithBitmapCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, String outfile) {
                //return the compressed file path and bitmap object
            }
        });
```

#### **BatchAsBitmap**

```
        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
        Tiny.getInstance().source("").batchAsBitmap().withOptions(options).batchCompress(new BitmapBatchCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap[] bitmaps) {
                //return the batch compressed bitmap object
            }
        });
```
#### **BatchAsFile**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile) {
                //return the batch compressed file path
            }
        });
```
#### **BatchAsFileWithReturnBitmap**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompress(new FileWithBitmapBatchCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile) {
                //return the batch compressed file path and bitmap object
            }
        });
```

## **Version**

* **v0.0.1**：The first version.
* **v0.0.2**：Optimize the compression strategy,and handle with the orientation of bitmap.
* **v0.0.3**：Unified as `libtiny.so`
* **v0.0.4**：Add cover source file configuration—see`{@link FileCompressOptions#overrideSource}`, and setting up the batch compressed file paths—see  `{@link BatchFileCompressOptions#outfiles}`
* **v0.0.5**：Fix google store reject.
* **v0.0.6**：Initialization is not must.Add clear compression directory method,see`{@link Tiny#clearCompressDirectory}`

## **License**

>
>     Apache License
>
>     Version 2.0, January 2004
>     http://www.apache.org/licenses/
>
>     Copyright 2017 郑晓勇
>
>  Licensed under the Apache License, Version 2.0 (the "License");
>  you may not use this file except in compliance with the License.
>  You may obtain a copy of the License at
>
>      http://www.apache.org/licenses/LICENSE-2.0
>
>  Unless required by applicable law or agreed to in writing, software
>  distributed under the License is distributed on an "AS IS" BASIS,
>  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>  See the License for the specific language governing permissions and
>  limitations under the License.


