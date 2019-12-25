# ImageHelper

Support to implement about ImagePicker and ImageViewer

This library is currently in Developer Preview. The API surface is not yet finalized, and please note that use this in production apps..

<br>

## Installation - Gradle
[![Release](https://jitpack.io/v/moka-a/mokaroid.svg)](https://jitpack.io/#moka-a/mokaroid)

```gradle
dependencies {
    implementation "com.github.moka-a.mokaroid:imagehelper:x.y.z"
}

```
<br>
<br>

## How to use

### ImagePicker

```kotlin
ImagePicker
    .with(this)
    .setConfig {
        // ... set your configuration
    }
    .showSingle { uri ->
        // uri targeted on Android 10 (api level 29)
    }
```


<br>

### ImageViewer

```kotlin
ImageViewer()
    .apply {
        // Add Header
        val header = OverlayView(activity!!, R.layout.layout_your_header)
        header.getTextView(R.id.title)!!.text = "Title"

        addHeader(header)

        // Add Listener
        addOnPageSelected { position ->
            header.getTextView(R.id.title)!!.text = "Title ${position}"
        }
    }
    .show(activity!!, imageList, selectedPosition)
```
