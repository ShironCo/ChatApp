package com.example.chatapps.components.mainScreenChats.presentation.mainScreens


import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R
import com.example.chatapps.components.mainScreenChats.presentation.addContacts.AddContactEvents
import com.example.chatapps.components.mainScreenChats.presentation.addContacts.AddContactScreen
import com.example.chatapps.components.mainScreenChats.presentation.addContacts.AddContactViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MainScreenChat(
    viewModel: MainScreenViewModel = hiltViewModel(),
    viewModelContact: AddContactViewModel = hiltViewModel()
) {
    val state = viewModel.states

    BackHandler(true) {
        if (state.toggleContact) {
            viewModelContact.onEvent(AddContactEvents.CleanVariables)
            viewModel.onEvent(MainScreenEvents.ChangeToggleContact(false))
        }
    }
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopLayout(viewModel) },
        floatingActionButton = {
            FloatingButtonMainScreen() {
                viewModel.onEvent(it)
            }
        }
    ) {
        ContentMainScreenChat(
            modifier = Modifier.padding(it)
        )
    }
    AnimatedVisibility(
        visible = state.toggleContact,
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 500,
                easing = EaseIn
            ),
            initialOffsetX = { it * 2 }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 500,
                easing = EaseOut
            ),
            targetOffsetX = { it * 2 }
        )
    ) {
        AddContactScreen() {
            viewModel.onEvent(it)
        }
    }
}

@Composable
fun FloatingButtonMainScreen(
    onEvent: (MainScreenEvents) -> Unit
) {
    Button(
        shape = MaterialTheme.shapes.large.copy(
            CornerSize(100)
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onSecondary
        ),
        onClick = {
            onEvent(MainScreenEvents.ChangeToggleContact(toggleContact = true))
        }
    ) {
        Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            tint = MaterialTheme.colors.background,
            modifier = Modifier.size(30.dp)
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
                        MainScreenEvents.ChangeToggleSearch(
                            !state.toggleSearch
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

    LaunchedEffect(key1 = states.toggleSearch) {
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
                    MainScreenEvents.ChangeSearch(
                        search = it,

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
                        MainScreenEvents.ChangeToggleSearch(
                            toggleSearch = !states.toggleSearch
                        )
                    )
                    onEvent(MainScreenEvents.ChangeSearch(search = null))
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
                    onEvent(MainScreenEvents.ChangeSearch(search = null))
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
        ChatScreen(state = state) {
            viewModel.onEvent(it)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    state: MainScreenStates,
    onEvent: (MainScreenEvents) -> Unit
) {
    val list = state.list
    val pagerStates = rememberPagerState()

    LaunchedEffect(key1 = state.selectedTabIndex) {
        pagerStates.animateScrollToPage(state.selectedTabIndex)
    }
    LaunchedEffect(key1 = pagerStates.currentPage) {
        onEvent(MainScreenEvents.ChangeSelectedTabIndex(pagerStates.currentPage))
    }

    HorizontalPager(
        pageCount = state.list.size,
        state = pagerStates,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> {
                Chats()
            }
            1 -> {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    state: MainScreenStates,
    onEvent: (MainScreenEvents) -> Unit
) {

    TabRow(
        selectedTabIndex = state.selectedTabIndex,
        modifier = Modifier.height(60.dp),
        backgroundColor = MaterialTheme.colors.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[state.selectedTabIndex]
                ),
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
        state.list.forEachIndexed { index, list ->
            Tab(
                selected = state.selectedTabIndex == index, onClick = {
                    Log.d(TAG, state.selectedTabIndex.toString())
                    onEvent(MainScreenEvents.ChangeSelectedTabIndex(index))
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

@Composable
fun Chats() {
    LazyColumn() {
        repeat(100) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Text(
                        text = "HOLA COMO ESTAS",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ItemChat() {

}