package com.example.chatapps.createUser.presentation.createProfile

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapps.R
import com.example.chatapps.createUser.presentation.ButtonScreenMain
import com.example.chatapps.createUser.presentation.CreateUserStates

@Composable
fun ProfileScreen() {
    ProfileContent()
}

@Composable
fun ProfileContent() {
    Column(
        modifier = Modifier
           .fillMaxSize()
           .padding(horizontal = 30.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.infoProfile),
            style = MaterialTheme.typography.h1.copy(
                fontSize = 25.sp
            ),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(id = R.string.infoProfileDesc),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
        )
        Spacer(modifier = Modifier.height(60.dp))
        Box(
            modifier = Modifier
                .size(150.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.profilephoto),
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                   .fillMaxSize()
                   .padding(5.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                       .clip(CircleShape)
                       .size(30.dp)
                       .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera, contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
       Spacer(modifier = Modifier.height(25.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
           placeholder = {
              Text(text = stringResource(id = R.string.infoProfilePlaceholder),
                 style = MaterialTheme.typography.body1.copy(
                    fontSize = 16.sp
                 ),
                 color = MaterialTheme.colors.onPrimary)
           },
           colors = TextFieldDefaults.outlinedTextFieldColors(
              focusedBorderColor = MaterialTheme.colors.background,
              backgroundColor = MaterialTheme.colors.background,
              unfocusedBorderColor = MaterialTheme.colors.background
           ),
           modifier = Modifier.fillMaxWidth()
        )
       Box(modifier = Modifier.fillMaxSize().padding(10.dp),
       contentAlignment = Alignment.BottomEnd){
          Button(
             onClick = {

             },
             colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onSecondary,
                disabledBackgroundColor = MaterialTheme.colors.background
             ), enabled = true
          ) {
             Text(
                text = stringResource(id = R.string.ContinueButton),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.background
             )
          }
       }
    }
}