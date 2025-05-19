package arcanegolem.materialize

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import arcanegolem.materialize.providers.PdfProvider
import arcanegolem.materialize.ui.theme.MaterializeTheme
import arcanegolem.materialize.viewer.ColumnPdfViewer
import arcanegolem.materialize.viewer.PdfViewerColors
import arcanegolem.materialize.viewer.PdfViewerOption
import kotlin.math.sqrt

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
                    Toast.makeText(this@MainActivity, "Sample text", Toast.LENGTH_SHORT).show()
                  }
                ),
                PdfViewerOption(
                  title = "SampleOption2",
                  action = {
                    Toast.makeText(this@MainActivity, "Another sample text", Toast.LENGTH_SHORT).show()
                  }
                )
              ),
              fillerAspectRatio = 1f / sqrt(2f),
              pageSpacing = 4.dp
            )
          }
        }
      }
    }
  }
}