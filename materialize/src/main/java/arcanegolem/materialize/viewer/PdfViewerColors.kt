package arcanegolem.materialize.viewer

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.graphics.Color

data class PdfViewerColors(
  val fillerPageColor : Color = Color.Gray,
  val floatingElementsColor : Color = Color.LightGray,
  val loadingIndicatorColor : Color = Color.Black,
  val iconButtonColors: IconButtonColors = IconButtonColors(
    containerColor = Color.Unspecified,
    contentColor = Color.Unspecified,
    disabledContainerColor = Color.Unspecified,
    disabledContentColor = Color.Unspecified
  ),
  val textFieldColors: TextFieldColors = TextFieldColors(
    focusedTextColor = Color.Unspecified,
    unfocusedTextColor = Color.Unspecified,
    disabledTextColor = Color.Unspecified,
    errorTextColor = Color.Unspecified,
    focusedContainerColor = Color.Unspecified,
    unfocusedContainerColor = Color.Unspecified,
    disabledContainerColor = Color.Unspecified,
    errorContainerColor = Color.Unspecified,
    cursorColor = Color.Unspecified,
    errorCursorColor = Color.Unspecified,
    textSelectionColors = TextSelectionColors(
      handleColor = Color.Unspecified,
      backgroundColor = Color.Unspecified
    ),
    focusedIndicatorColor = Color.Unspecified,
    unfocusedIndicatorColor = Color.Unspecified,
    disabledIndicatorColor = Color.Unspecified,
    errorIndicatorColor = Color.Unspecified,
    focusedLeadingIconColor = Color.Unspecified,
    unfocusedLeadingIconColor = Color.Unspecified,
    disabledLeadingIconColor = Color.Unspecified,
    errorLeadingIconColor = Color.Unspecified,
    focusedTrailingIconColor = Color.Unspecified,
    unfocusedTrailingIconColor = Color.Unspecified,
    disabledTrailingIconColor = Color.Unspecified,
    errorTrailingIconColor = Color.Unspecified,
    focusedLabelColor = Color.Unspecified,
    unfocusedLabelColor = Color.Unspecified,
    disabledLabelColor = Color.Unspecified,
    errorLabelColor = Color.Unspecified,
    focusedPlaceholderColor = Color.Unspecified,
    unfocusedPlaceholderColor = Color.Unspecified,
    disabledPlaceholderColor = Color.Unspecified,
    errorPlaceholderColor = Color.Unspecified,
    focusedSupportingTextColor = Color.Unspecified,
    unfocusedSupportingTextColor = Color.Unspecified,
    disabledSupportingTextColor = Color.Unspecified,
    errorSupportingTextColor = Color.Unspecified,
    focusedPrefixColor = Color.Unspecified,
    unfocusedPrefixColor = Color.Unspecified,
    disabledPrefixColor = Color.Unspecified,
    errorPrefixColor = Color.Unspecified,
    focusedSuffixColor = Color.Unspecified,
    unfocusedSuffixColor = Color.Unspecified,
    disabledSuffixColor = Color.Unspecified,
    errorSuffixColor =Color.Unspecified,
  )
)
