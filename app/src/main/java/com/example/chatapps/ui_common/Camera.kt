package com.example.chatapps.ui_common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapps.R


@Composable
fun PreviewImage(
    painter: Painter,
    screenHeight: Dp,
    screenWidth: Dp,
    onClean: () -> Unit,
    onSaveImage: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .rotate(
                    90f
                )
                .size(
                    width = screenWidth,
                    height = screenHeight * 0.9f
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                          onClean()
                    //viewModel.onCleanPreviewImage()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(screenHeight * 0.1f)
                    .padding(start = 10.dp, end = 5.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onSurface
                )
            ) {
                Text(
                    text = "Volver a tomar foto",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.background
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    //viewModel.onSaveImage(context, navController)
                          onSaveImage()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(screenHeight * 0.1f)
                    .padding(start = 5.dp, end = 10.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onSurface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.continueButton),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.background
                )
            }
        }
    }
}