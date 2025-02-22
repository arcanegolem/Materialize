package arcanegolem.materialize.viewer

/**
 * Represents additional option in page menu of PDFViewer
 *
 * @param [title] displayed title of action
 * @param [action] action to perform on click
 */
data class PdfViewerOption(
  val title : String,
  val action : () -> Unit
)
