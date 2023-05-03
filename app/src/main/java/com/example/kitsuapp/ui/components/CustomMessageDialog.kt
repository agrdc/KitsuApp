package com.example.kitsuapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomMessageDialog(
    title: String? = null,
    message: String,
    outlinedButtonText: String? = null,
    filledButtonText: String? = null,
    outlinedButtonAction: () -> Unit = {},
    filledButtonAction: () -> Unit = {}
) {
    Dialog(

        onDismissRequest = { outlinedButtonAction() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                title?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 2.dp),
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = message,
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                )

                Row(Modifier.padding(top = 6.dp)) {
                    outlinedButtonText?.let {
                        OutlinedButton(
                            shape = RoundedCornerShape(8.dp),
                            onClick = { outlinedButtonAction() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 3.dp)
                                .weight(1F),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                        )
                        { Text(text = outlinedButtonText, fontSize = 12.sp) }
                    }

                    filledButtonText?.let {
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            onClick = { filledButtonAction() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 3.dp)
                                .weight(1F)
                        ) {
                            Text(text = filledButtonText, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomMessageDialogPreview() {
    CustomMessageDialog(
        outlinedButtonText = "Simulate error",
        filledButtonText = "Proceed",
        title = "Information",
        message = "This \"login\" is only symbolic and no data is being sent or stored anywhere. I created this just to show how field validations would fit in this app's architecture.",
    )
}