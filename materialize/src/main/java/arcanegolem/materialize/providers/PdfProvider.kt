package arcanegolem.materialize.providers

import android.net.Uri
import androidx.annotation.RawRes

sealed class PdfProvider(
  val identifier: String,
  val bitmapScale: Int
) {
  class UriProvider(val uri: Uri, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)
  class UrlProvider(val url: String, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)
  class ResProvider(@RawRes val resId: Int, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)
}