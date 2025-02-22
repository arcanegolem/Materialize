package arcanegolem.materialize.viewer

data class PdfViewerOption(
  val title : String,
  val action : () -> Unit
)
