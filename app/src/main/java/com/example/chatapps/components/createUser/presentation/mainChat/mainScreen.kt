package com.example.chatapps.components.createUser.presentation.mainChat

import android.os.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainScreenChat() {
    ContentMainScreenChat()
}

@Composable
fun ContentMainScreenChat() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopLaout()
    }
}

@Composable
fun TopLaout() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "nombre",
            style = MaterialTheme.typography.body2.copy(
                fontSize = 20.sp
            ),
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(start = 20.dp)
        )
        Row() {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Buscar",
                    tint = MaterialTheme.colors.primary, modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones",
                    tint = MaterialTheme.colors.primary, modifier = Modifier.size(30.dp)
                )

            }
        }
    }
}