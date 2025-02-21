package arcanegolem.materialize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import arcanegolem.materialize.providers.PdfProvider
import arcanegolem.materialize.ui.theme.MaterializeTheme
import arcanegolem.materialize.viewer.ColumnPdfViewer

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MaterializeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Column(
            modifier = Modifier.padding(innerPadding)
          ) {
            ColumnPdfViewer(
              modifier = Modifier.fillMaxSize(),
              pdfProvider = PdfProvider.ResProvider(
                resId = R.raw.sample_multipage,
                bitmapScale = 3,
                identifier = "sample_multipage"
              )
            )
          }
        }
      }
    }
  }
}