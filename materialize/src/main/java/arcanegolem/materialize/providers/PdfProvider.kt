package arcanegolem.materialize.providers

import android.net.Uri
import androidx.annotation.RawRes

/**
 * Class for defining a way to provide PDF document to PDFViewer
 *
 * @param [identifier] identifier for a document, it is mandatory and strongly advised to be unique, because it is used for caching
 * @param [bitmapScale] represents quality of the displayed document, higher value equals higher resolution
 */
sealed class PdfProvider(
  val identifier: String,
  val bitmapScale: Int
) {
  /**
   * Provides PDF document via Uri
   *
   * @param [uri] [Uri] of the desired document on the device
   * @param [identifier] identifier for a document, it is mandatory and strongly advised to be unique, because it is used for caching
   * @param [bitmapScale] represents quality of the displayed document, higher value equals higher resolution
   */
  class UriProvider(val uri: Uri, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)

  /**
   * Provides PDF document via URL
   *
   * NOTE: enable clearTextTraffic in your manifest to receive documents from unsecure HTTP URLs
   *
   * @param [url] URL of the desired document on the device
   * @param [identifier] identifier for a document, it is mandatory and strongly advised to be unique, because it is used for caching
   * @param [bitmapScale] represents quality of the displayed document, higher value equals higher resolution
   */
  class UrlProvider(val url: String, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)

  /**
   * Provides PDF document via Raw resource
   *
   * @param [resId] PDF resource ID
   * @param [identifier] identifier for a document, it is mandatory and strongly advised to be unique, because it is used for caching
   * @param [bitmapScale] represents quality of the displayed document, higher value equals higher resolution
   */
  class ResProvider(@RawRes val resId: Int, bitmapScale: Int, identifier : String) : PdfProvider(identifier, bitmapScale)
}