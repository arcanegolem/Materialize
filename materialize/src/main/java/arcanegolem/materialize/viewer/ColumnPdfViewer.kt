package arcanegolem.materialize.viewer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import arcanegolem.materialize.providers.PdfProvider
import arcanegolem.materialize.providers.getParcelFileDescriptorForPdfProvider
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import android.graphics.Color as LegacyColor

@Composable
fun ColumnPdfViewer(
  modifier: Modifier,
  pdfProvider: PdfProvider,
  colors: PdfViewerColors = PdfViewerColors(),
  options: List<PdfViewerOption> = emptyList(),
  fillerAspectRatio : Float
) {
  val coroutineScope = rememberCoroutineScope()
  val mutex = remember { Mutex() }
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  val pdfRenderer by produceState<PdfRenderer?>(null, pdfProvider) {
    coroutineScope.launch(Dispatchers.IO) {
      val parcelFileDescriptor = context.getParcelFileDescriptorForPdfProvider(pdfProvider)
      value = parcelFileDescriptor?.let { PdfRenderer(it) }
    }
    awaitDispose {
      val renderer = value
      coroutineScope.launch(Dispatchers.IO) {
        mutex.withLock { renderer?.close() }
      }
    }
  }

  val pageCount by remember(pdfRenderer) { derivedStateOf { pdfRenderer?.pageCount ?: 0 } }

  var scale by remember { mutableFloatStateOf(1f) }
  var offsetX by remember { mutableFloatStateOf(0f) }
  var offsetY by remember { mutableFloatStateOf(0f) }
  var viewportSize by remember { mutableStateOf(Size.Zero) }

  val animatedOffsetX by animateFloatAsState(
    if (scale <= 1f) {offsetX = 0f; 0f}  else offsetX
  )
  val animatedOffsetY by animateFloatAsState(
    if (scale <= 1f) {offsetY = 0f; 0f}  else offsetY
  )

  val lazyListState = rememberLazyListState()

  var optionsExpanded by remember { mutableStateOf(false) }

  Box(
    modifier = modifier.then(
      Modifier
        .pointerInput(Unit) {
          awaitEachGesture {
            awaitFirstDown()
            do {
              val event = awaitPointerEvent()
              scale = (scale * event.calculateZoom()).coerceAtLeast(1f)
              val offset = event.calculatePan()
              offsetX += offset.x
              offsetY += offset.y
            } while (event.changes.any { it.pressed })
          }
        }
    )
  ) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier
        .fillMaxSize()
        .then(
          Modifier
            .onGloballyPositioned { layoutCoordinates ->
              viewportSize = layoutCoordinates.size.toSize()
            }
            .graphicsLayer(
              scaleX = scale,
              scaleY = scale,
              translationX = animatedOffsetX.coerceIn(
                -(viewportSize.width * (scale - 1)) / 2f,
                (viewportSize.width * (scale - 1)) / 2f
              ),
              translationY = animatedOffsetY.coerceIn(
                -(viewportSize.height * (scale - 1)) / 2f,
                (viewportSize.height * (scale - 1)) / 2f
              )
            )
        )
    ) {
      items(pageCount, key = { page -> "${pdfProvider.identifier}$page" }) { page ->
        val cacheKey = MemoryCache.Key("${pdfProvider.identifier}$page")
        val cachedBitmap = context.imageLoader.memoryCache?.get(cacheKey)?.bitmap
        var bitmap by remember { mutableStateOf(cachedBitmap) }

        if (bitmap == null) {
          DisposableEffect(pdfProvider.identifier, page) {
            val renderingJob = coroutineScope.launch(Dispatchers.IO) {
              mutex.withLock {
                if (!coroutineContext.isActive) return@launch

                try {
                  pdfRenderer?.let {
                    it.openPage(page).use { pageInRenderer ->
                      bitmap = Bitmap.createBitmap(
                        pageInRenderer.width * pdfProvider.bitmapScale,
                        pageInRenderer.height * pdfProvider.bitmapScale,
                        Bitmap.Config.ARGB_8888
                      )

                      bitmap?.let { bmp ->
                        Canvas(bmp).apply {
                          drawColor(LegacyColor.WHITE)
                          drawBitmap(bitmap!!, 0f, 0f, null)
                        }
                      }

                      pageInRenderer.render(
                        bitmap!!,
                        null,
                        null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                      )
                    }
                  }
                } catch (e: Exception) {
                  e.printStackTrace()
                  return@launch
                }
              }
            }
            onDispose { renderingJob.cancel() }
          }

          Box(
            modifier = Modifier
              .background(color = colors.fillerPageColor)
              .aspectRatio(fillerAspectRatio)
              .fillMaxWidth()
          )
        } else {
          val densityInt = LocalDensity.current.density.toInt()
          val pageWidth = bitmap!!.getScaledWidth(densityInt)
          val pageHeight = bitmap!!.getScaledHeight(densityInt)

          val imageRequest = ImageRequest.Builder(context)
            .size(pageWidth, pageHeight)
            .memoryCacheKey(cacheKey)
            .data(bitmap)
            .build()

          Image(
            modifier = Modifier
              .background(color = colors.fillerPageColor)
              .aspectRatio(pageWidth.toFloat() / pageHeight.toFloat()),
            painter = rememberAsyncImagePainter(imageRequest),
            contentDescription = null
          )
        }
      }
    }

    var textFieldVal by remember { mutableStateOf("") }

    Row(
      modifier = Modifier
        .offset((-8).dp, (8).dp)
        .align(Alignment.TopEnd)
        .animateContentSize()
        .clip(RoundedCornerShape(8.dp))
        .background(color = colors.floatingElementsColor),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        IconButton(
          onClick = { optionsExpanded = !optionsExpanded },
          colors = IconButtonDefaults.iconButtonColors(contentColor = colors.iconButtonColors.contentColor)
        ) {
          Icon(
            imageVector = if (optionsExpanded) Icons.Rounded.Close else Icons.Rounded.MoreVert,
            contentDescription = "menu"
          )
        }
        if (optionsExpanded) {
          Column {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                modifier = Modifier
                  .padding(4.dp)
                  .width(120.dp),
                value = textFieldVal,
                onValueChange = { textFieldVal = it },
                colors = colors.textFieldColors,
                singleLine = true,
                placeholder = { Text("1-$pageCount") }
              )
              FilledIconButton(
                modifier = Modifier.padding(4.dp),
                onClick = {
                  focusManager.clearFocus()
                  coroutineScope.launch {
                    lazyListState.animateScrollToItem(
                      ((textFieldVal.toIntOrNull() ?: 0) - 1).coerceIn(0, pageCount)
                    )
                  }
                },
                colors = colors.iconButtonColors
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                  contentDescription = null
                )
              }
            }
            options.forEach { pdfOption ->
              Row(
                modifier = Modifier.clickable { pdfOption.action() }
              ) {
                Text(
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                  text = pdfOption.title
                )
              }
            }
          }
        }
      }
    }
  }
}