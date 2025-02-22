# Materialize [![](https://jitpack.io/v/arcanegolem/Materialize.svg)](https://jitpack.io/#arcanegolem/Materialize)

Simple PDF viewer made with and for Jetpack Compose

# Requirements
- Add `INTERNET` permission to your Android Manifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

- [OPTIONAL] Set `android:usesCleartextTraffic="true"` in your Android Manifest `<application>` tag to enable downloading of PDF documents from unsecure `http://` URLs
```xml
<application>
  ...
  android:usesCleartextTraffic="true"
  ...
</application>
```

# Example usage
`ColumnPdfViewer` composable function provides a simple scrollable column PDF viewer, fillers and "more" menu can be customized using `PdfViewerColors` and `options` parameters. PDF document can be provided via URL, Uri or Raw resource using corresponding `PdfProvider`. 

For example, following code (see more in MainActivity.kt) will result in something like this:
<img src="https://github.com/arcanegolem/Materialize/blob/master/files/materialize_demo.gif" width="380" />

```kotlin
ColumnPdfViewer(
  modifier = Modifier.fillMaxSize(),
  pdfProvider = PdfProvider.ResProvider(
    resId = R.raw.sample_multipage,
    bitmapScale = 3,
    identifier = "sample_multipage"
  ),
  colors = PdfViewerColors(
    fillerPageColor = Color.Black,
    floatingElementsColor = Color.Blue,
    iconButtonColors = IconButtonDefaults.iconButtonColors(),
    textFieldColors = TextFieldDefaults.colors()
  ),
  options = listOf(
    PdfViewerOption(
      title = "SampleOption",
      action = {
        Toast.makeText(context, "Sample text", Toast.LENGTH_SHORT).show()
      }
    ),
    PdfViewerOption(
      title = "SampleOption2",
      action = {
        Toast.makeText(context, "Another sample text", Toast.LENGTH_SHORT).show()
      }
    )
  ),
  fillerAspectRatio = 1f / sqrt(2f)
)
```
