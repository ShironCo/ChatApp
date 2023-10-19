package com.example.chatapps.components.createUser.presentation.createProfile


import android.Manifest
import android.app.Activity
import android.icu.text.DateTimePatternGenerator.DisplayWidth
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.Window
import android.view.WindowMetrics
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R
import com.example.chatapps.components.createUser.data.repository.OptionsSheet
import com.example.chatapps.components.createUser.data.repository.OptionsSheet.Companion.listBottom
import com.example.chatapps.components.createUser.presentation.CreateUserEvents
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen() {
    ProfileContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileContent(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val stateSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val context = LocalContext.current
    val windows = context as Activity
    val scope = rememberCoroutineScope()
    val bottomList: List<OptionsSheet> = listBottom
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            println("Hi")
        }
    }
    BackHandler(
        enabled = state.camera
    ) {
        viewModel.onEvent(ProfileEvents.SetCamera)
        scope.launch {
            stateSheet.hide()
        }
    }
    AnimatedVisibility(
        visible = state.camera,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        TakePicture(state) {
            viewModel.onEvent(it)
        }
    }
    if (!state.camera) {
        windows.window.navigationBarColor = MaterialTheme.colors.background.toArgb()
        windows.window.statusBarColor = MaterialTheme.colors.background.toArgb()
        ModalBottomSheetLearn(
            stateSheet, bottomList,
            bottomClick = {
                when (it) {
                    R.string.bottomSheetCamera -> {
                        viewModel.onEvent(ProfileEvents.SetCamera)
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
                            stateSheet.show()
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
                    value = state.name,
                    onValueChange = {
                        viewModel.onEvent(ProfileEvents.SetName(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.infoProfilePlaceholder),
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colors.secondary
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.background,
                        backgroundColor = MaterialTheme.colors.background,
                        unfocusedBorderColor = MaterialTheme.colors.background,
                        textColor = MaterialTheme.colors.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body1
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
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetLearn(
    state: ModalBottomSheetState,
    bottomList: List<OptionsSheet>,
    bottomClick: (Int) -> Unit,
    content: @Composable () -> Unit
) {

    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier.padding(top = 30.dp, start = 20.dp)
            ) {
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
                ) {
                    items(bottomList) {
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
                                    Icon(
                                        imageVector = it.icon, contentDescription = null,
                                        tint = MaterialTheme.colors.onPrimary
                                    )
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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TakePicture(
    states: ProfileStates,
    onEvent: (ProfileEvents) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight: Dp = configuration.screenHeightDp.dp
    val screenWidth: Dp = configuration.screenWidthDp.dp
    val permission = if (Build.VERSION.SDK_INT <= 28) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        listOf(Manifest.permission.CAMERA)
    }
    val permissionState = rememberMultiplePermissionsState(permissions = permission)
    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewView: PreviewView
    val windows = context as Activity
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (permissionState.allPermissionsGranted && states.camera) {
            windows.window.statusBarColor = Color.Black.toArgb()
            windows.window.navigationBarColor = Color.Black.toArgb()
            Box(
                modifier = Modifier
                    .height(screenHeight * 0.85f)
                    .width(screenWidth)
            ) {
                AndroidView(
                    factory = {
                        previewView = PreviewView(it)
                       //onEvent(ProfileEvents.ShowPreview(previewView, lifecycleOwner))
                        previewView
                    },
                    modifier = Modifier
                        .height(screenHeight * 0.85f)
                        .width(screenWidth),
                    update = {
                        onEvent(ProfileEvents.ShowPreview(it, lifecycleOwner))
                    }
                )
            }
            Box(
                modifier = Modifier
                    .height(screenHeight * 0.15f)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            onEvent(ProfileEvents.SetCamera)
                        } else {
                            Toast.makeText(
                                context,
                                "Porfavor acepta los permisos en la configuracion de la app",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                            onEvent(ProfileEvents.CapturePicture(context))
                        } else {
                            Toast.makeText(
                                context,
                                "Porfavor acepta los permisos en la configuracion de la app",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.RadioButtonChecked,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = {
                        if (permissionState.allPermissionsGranted) {
                           onEvent(ProfileEvents.SetFacing)
                            println(states.facing)
                        } else {
                            Toast.makeText(
                                context,
                                "Porfavor acepta los permisos en la configuracion de la app",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

