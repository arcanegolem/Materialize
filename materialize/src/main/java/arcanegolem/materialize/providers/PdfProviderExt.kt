package arcanegolem.materialize.providers

import android.content.Context
import android.os.ParcelFileDescriptor
import androidx.core.net.toFile
import arcanegolem.materialize.file_loading.DownloadInterface
import java.io.File
import java.io.FileOutputStream

internal suspend fun Context.getParcelFileDescriptorForPdfProvider(provider: PdfProvider) : ParcelFileDescriptor? {
  return when (provider) {
    is PdfProvider.UriProvider -> ParcelFileDescriptor.open(provider.uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)

    is PdfProvider.ResProvider -> {
      val temp = File(cacheDir, provider.identifier)

      resources.openRawResource(provider.resId).use { inputStream ->
        FileOutputStream(temp).use { outputStream ->
          inputStream.copyTo(outputStream)
        }
      }

      ParcelFileDescriptor.open(temp, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    is PdfProvider.UrlProvider -> {
      val temp = File(cacheDir, provider.identifier)

      DownloadInterface().downloadFile(provider.url).byteStream().use { inputStream ->
        FileOutputStream(temp).use { outputStream ->
          inputStream.copyTo(outputStream)
        }
      }

      ParcelFileDescriptor.open(temp, ParcelFileDescriptor.MODE_READ_ONLY)
    }
  }
}