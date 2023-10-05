package com.example.chatapps.components.createUser.presentation.createProfile


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R
import com.example.chatapps.components.createUser.data.repository.OptionsSheet
import com.example.chatapps.components.createUser.data.repository.OptionsSheet.Companion.listBottom
import com.example.chatapps.components.createUser.presentation.CreateUserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen() {
    ProfileContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileContent(
    viewModel: CreateUserViewModel = hiltViewModel()
) {


    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()
    val bottomList: List<OptionsSheet> = listBottom
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            println("Hi")
        }
    }

    ModalBottomSheetLearn(
        state, bottomList,
        bottomClick = {
            when(it){
                R.string.bottomSheetCamera -> {
                    println("Hola")
                }
                R.string.bottomSheetGallery -> {
                    launcher.launch("image/*")
                }
            }
        }
    ) {
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
                IconButton(onClick = {
                    scope.launch {
                        state.show()
                    }
                }) {
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
                    Text(
                        text = stringResource(id = R.string.infoProfilePlaceholder),
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.background,
                    backgroundColor = MaterialTheme.colors.background,
                    unfocusedBorderColor = MaterialTheme.colors.background
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSecondary,
                        disabledBackgroundColor = MaterialTheme.colors.background
                    ), enabled = true
                ) {
                    Text(
                        text = stringResource(id = R.string.continueButton),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetLearn(
    state : ModalBottomSheetState,
    bottomList: List<OptionsSheet>,
    bottomClick: (Int) -> Unit,
    content: @Composable () -> Unit
) {

    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier.padding(top = 30.dp, start = 20.dp)
            ){
                Text(
                    text = stringResource(id = R.string.titleSheet),
                    style = MaterialTheme.typography.h1.copy(
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                ){
                    items(bottomList){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(10.dp)
                        ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(100)
                                )
                                .background(MaterialTheme.colors.secondary)
                                .padding(5.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                                IconButton(onClick = {
                                        bottomClick(it.title)
                                }) {
                                    Icon(imageVector = it.icon, contentDescription = null,
                                    tint = MaterialTheme.colors.onPrimary)
                                }
                        }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = stringResource(it.title),
                                style = MaterialTheme.typography.body1.copy(
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }
        },
        sheetState = state,
        sheetShape = RoundedCornerShape(
            topStartPercent = 20,
            topEndPercent = 20
        ),
        sheetBackgroundColor = MaterialTheme.colors.background

    ) {
        content()
    }
}

@Composable
fun SheetContent(){

}