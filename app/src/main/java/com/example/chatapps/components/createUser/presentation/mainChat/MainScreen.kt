package com.example.chatapps.components.createUser.presentation.mainChat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusProperties
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R

@Composable
fun MainScreenChat(
    viewModel : MainScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {TopLayout(viewModel) },
        floatingActionButton = {
            Button(
                shape = MaterialTheme.shapes.large.copy(
                    CornerSize(100)
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onSecondary
                ),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = MaterialTheme.colors.background,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) {
        ContentMainScreenChat(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun ContentMainScreenChat(
    modifier: Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BodyChat(viewModel)
    }
}


@Composable
fun TopLayout(
    viewModel: MainScreenViewModel
) {
    val state = viewModel.states
    val scaleA: Dp by animateDpAsState(targetValue = if (state.toggleSearch) 100.dp else 40.dp)
    Box(
        modifier = Modifier
            .height(scaleA)
            .padding(horizontal = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(start = 20.dp)
            )
            Row() {
                IconButton(onClick = {
                    viewModel.onEvent(
                        MainScreenEvents.ChangeText(
                            text = MainScreenStates(
                                toggleSearch = !state.toggleSearch,
                                search = state.search
                            )
                        )
                    )
                }) {
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

        AnimatedVisibility(
            visible = state.toggleSearch,
            enter = slideIn(
                initialOffset = { fullSize -> IntOffset(0, -fullSize.height) }
            ) + fadeIn(),
            exit = slideOut(
                targetOffset = { fullSize -> IntOffset(0, -fullSize.height) }
            ) + fadeOut())
        {
            SearchBox(states = state) {
                viewModel.onEvent(it)
            }
        }
    }
}


@Composable
fun SearchBox(
    states: MainScreenStates,
    onEvent: (MainScreenEvents) -> Unit
) {
   val focus = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = states.toggleSearch){
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colors.secondary),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = states.search ?: "",
            onValueChange = {
                onEvent(
                    MainScreenEvents.ChangeText(
                        MainScreenStates(
                            search = it,
                            toggleSearch = states.toggleSearch,
                            selectedTabIndex = states.selectedTabIndex
                        )
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.secondary
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                //focusRequester.freeFocus()
                focus.clearFocus()
            }),
            leadingIcon = {
                IconButton(onClick = {
                    focusRequester.freeFocus()
                    onEvent(
                        MainScreenEvents.ChangeText(
                            MainScreenStates(
                                toggleSearch = !states.toggleSearch,
                                search = null,
                                selectedTabIndex = states.selectedTabIndex
                            )
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.search),
                        tint = MaterialTheme.colors.background,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    onEvent(
                        MainScreenEvents.ChangeText(
                            MainScreenStates(
                                toggleSearch = states.toggleSearch,
                                search = null,
                                selectedTabIndex = states.selectedTabIndex
                            )
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            shape = MaterialTheme.shapes.large.copy(
                CornerSize(30.dp)
            ),
            textStyle = MaterialTheme.typography.body1.copy(
                fontSize = 17.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                textColor = MaterialTheme.colors.background,
                cursorColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun BodyChat(
    viewModel: MainScreenViewModel
) {
    val state = viewModel.states
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabScreen(
            state = state
        ) {
            viewModel.onEvent(it)
        }
        ChatScreen()
    }
}

@Composable
fun ChatScreen(){
    LazyColumn(){
    }
}
@Composable
fun TabScreen(
    state: MainScreenStates,
    onEvent: (MainScreenEvents) -> Unit
) {
    val list = listOf("Chat", "Contactos")
    TabRow(
        selectedTabIndex = 1,
        modifier = Modifier.height(60.dp),
        backgroundColor = MaterialTheme.colors.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTabIndex]),
                height = 3.dp,
                color = MaterialTheme.colors.onSecondary
            )
        },
        divider = {
            TabRowDefaults.Divider(
                color = MaterialTheme.colors.background
            )
        }
    ) {
        list.forEachIndexed { index, list ->
            Tab(
                selected = state.selectedTabIndex == index, onClick = {
                    onEvent(
                        MainScreenEvents.ChangeText(
                            MainScreenStates(
                                search = state.search,
                                toggleSearch = state.toggleSearch,
                                selectedTabIndex = index
                            )
                        )
                    )
                })
            {
                Text(
                    text = list,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}
