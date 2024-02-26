package com.example.chatapps.components.createUser.presentation.createProfile


import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.example.chatapps.R
import com.example.chatapps.components.createUser.data.repository.OptionsSheet
import com.example.chatapps.components.createUser.data.repository.OptionsSheet.Companion.listBottom
import com.example.chatapps.core.domain.preferences.Variable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


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
    val imageLoader = context.imageLoader
    val windows = context as Activity
    val scope = rememberCoroutineScope()
    val bottomList: List<OptionsSheet> = listBottom
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            viewModel.onEvent(ProfileEvents.SetImagePreview(it, context, imageLoader = imageLoader))
        }
    }
    val configuration = LocalConfiguration.current
    val screenHeight: Dp = configuration.screenHeightDp.dp
    val screenWidth: Dp = configuration.screenWidthDp.dp

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ProfileEvents.Init(context))
    }

    windows.window.statusBarColor = MaterialTheme.colors.background.toArgb()
    windows.window.navigationBarColor = MaterialTheme.colors.background.toArgb()
    BackHandler(
        enabled = state.camera
    ) {
        viewModel.onEvent(ProfileEvents.SetCamera)
        scope.launch {
            stateSheet.hide()
        }
    }
    ModalBottomSheetBody(
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
        ContentProfile(scope = scope, stateSheet = stateSheet, state = state) {
            viewModel.onEvent(it)
        }
    }
    AnimatedVisibility(
        visible = state.camera,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        RequestCameraPermissions(
            screenHeight = screenHeight,
            screenWidth = screenWidth,
            imageLoader = imageLoader,
            states = state
        ) {
            viewModel.onEvent(it)
        }
    }
    if (state.pictureToggle) {
        state.previewPicture?.let {
            ShowPicture(
                picture = it,
                screenHeight = screenHeight,
                screenWidth = screenWidth,
                context = context
            ) { event ->
                viewModel.onEvent(event)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContentProfile(
    scope: CoroutineScope,
    stateSheet: ModalBottomSheetState,
    state: ProfileStates,
    onEvent: (ProfileEvents) -> Unit
) {
    val localFocus = LocalFocusManager.current
    val context = LocalContext.current
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
                AsyncImage(
                    model = state.image ?: R.drawable.profilephoto,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(50))
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
                onEvent(ProfileEvents.SetName(it))
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.infoProfilePlaceholder),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.primary
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.background,
                backgroundColor = MaterialTheme.colors.background,
                unfocusedBorderColor = MaterialTheme.colors.background,
                textColor = MaterialTheme.colors.onPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                localFocus.clearFocus()
            })
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetBody(
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
                    items(bottomList) { ItemSheet ->
                        ModelBottomSheetItem(OptionSheet = ItemSheet) {
                            bottomClick(it)
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
fun ModelBottomSheetItem(
    OptionSheet: OptionsSheet,
    bottomClick: (Int) -> Unit
) {
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
                bottomClick(OptionSheet.title)
            }) {
                Icon(
                    imageVector = OptionSheet.icon, contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(OptionSheet.title),
            style = MaterialTheme.typography.body1.copy(
                fontSize = 14.sp
            ),
            color = MaterialTheme.colors.onPrimary,
        )
    }
}


@Composable
fun TakePicture(
    screenHeight: Dp,
    screenWidth: Dp,
    states: ProfileStates,
    imageLoader: ImageLoader,
    onEvent: (ProfileEvents) -> Unit
) {

    val context = LocalContext.current
    val windows = context as Activity
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        windows.window.statusBarColor = Color.Black.toArgb()
        windows.window.navigationBarColor = Color.Black.toArgb()
        Box(
            modifier = Modifier
                .height(screenHeight * 0.85f)
                .width(screenWidth)
        ) {
            PreviewImage(
                screenHeight,
                screenWidth,
                states
            ) {
                onEvent(it)
            }
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
                    onEvent(ProfileEvents.SetCamera)
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
                    onEvent(ProfileEvents.CapturePicture(context, imageLoader))
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
                    onEvent(ProfileEvents.SetFacing)
                    println(states.facing)
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

@Composable
fun PreviewImage(
    screenHeight: Dp,
    screenWidth: Dp,
    states: ProfileStates,
    onEvent: (ProfileEvents) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewView: PreviewView? by remember {
        mutableStateOf(null)
    }
    AndroidView(
        factory = {
            previewView = PreviewView(it)
            previewView!!
        },
        modifier = Modifier
            .height(screenHeight * 0.85f)
            .width(screenWidth),
    )
    LaunchedEffect(key1 = states.facing) {
        onEvent(ProfileEvents.ShowPreview(previewView!!, lifecycleOwner))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermissions(
    screenHeight: Dp,
    screenWidth: Dp,
    states: ProfileStates,
    imageLoader: ImageLoader,
    onEvent: (ProfileEvents) -> Unit
) {
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
    } else {
        TakePicture(
            screenHeight = screenHeight,
            screenWidth = screenWidth,
            imageLoader = imageLoader,
            states = states
        ) {
            onEvent(it)
        }
    }
}

@Composable
fun ShowPicture(
    picture: Uri,
    screenHeight: Dp,
    screenWidth: Dp,
    context: Context,
    onEvent: (ProfileEvents) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(screenHeight * 0.85f)
                .width(screenWidth)
        ) {
            AsyncImage(
                model = picture,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
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
                    onEvent(ProfileEvents.SetPictureToggle)
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
                    onEvent(ProfileEvents.DonePicture(context))
                }) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}