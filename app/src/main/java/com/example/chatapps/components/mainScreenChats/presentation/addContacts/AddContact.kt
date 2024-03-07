package com.example.chatapps.components.mainScreenChats.presentation.addContacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R
import com.example.chatapps.components.mainScreenChats.presentation.mainScreens.MainScreenEvents
import com.example.chatapps.createUser.presentation.selectCountry.Country
import com.example.chatapps.ui_common.TextsFieldBasic

@Composable
fun AddContactScreen(
    viewModel: AddContactViewModel = hiltViewModel(),
    onEvent: (MainScreenEvents) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AddContactTopbar(
                viewModel = viewModel
            ) {
                onEvent(it)
            }
        }
    ) {
        AddContactBody(modifier = Modifier.padding(it), viewModel = viewModel)
    }
}

@Composable
fun AddContactTopbar(
    viewModel: AddContactViewModel,
    onEvent: (MainScreenEvents) -> Unit
) {
    TopAppBar(
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        IconButton(onClick = {
            viewModel.onEvent(AddContactEvents.CleanVariables)
            onEvent(MainScreenEvents.ChangeToggleContact(false))
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
        Text(
            text = stringResource(id = R.string.newContact),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddContactBody(
    modifier: Modifier,
    viewModel: AddContactViewModel
) {
    val listCountry = Country.values()
    val focus = LocalFocusManager.current
    val state = viewModel.state
    val context = LocalContext.current
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextsFieldBasic(
                    modifier = Modifier,
                    title = state.name,
                    onFocus = {
                        focus.moveFocus(focusDirection = FocusDirection.Next)
                    },
                    placeholder = stringResource(
                        id = R.string.name
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                ) {
                    viewModel.onEvent(AddContactEvents.ChangeName(it))
                }
            }
            TextsFieldBasic(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 40.dp),
                title = state.lastName,
                onFocus = {
                    focus.moveFocus(focusDirection = FocusDirection.Next)
                },
                placeholder = stringResource(
                    id = R.string.lastName
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            ) {
                viewModel.onEvent(AddContactEvents.ChangeLastName(it))
            }
            Row(
                modifier = Modifier.padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    Text(
                        text = stringResource(id = R.string.country),
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (state.code.isNullOrEmpty()) "" else "${state.flag} ${state.code}",
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colors.onPrimary
                        )
                        IconButton(onClick = {
                            viewModel.onEvent(
                                AddContactEvents.ChangeExpanded(
                                    true
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Seleccionar país",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier.background(MaterialTheme.colors.secondary),
                            expanded = state.expanded,
                            onDismissRequest = {
                                viewModel.onEvent(
                                    AddContactEvents.ChangeExpanded(
                                        false
                                    )
                                )
                            }) {
                            listCountry.forEach {
                                DropdownMenuItem(onClick = {
                                    viewModel.onEvent(AddContactEvents.ChangeExpanded(false))
                                    viewModel.onEvent(AddContactEvents.ChangeCode(it.code))
                                    viewModel.onEvent(AddContactEvents.ChangeFlag(it.flag))
                                }) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = it.flag)
                                        Text(
                                            text = "${it.name}  ${it.code}",
                                            style = MaterialTheme.typography.subtitle1.copy(
                                                fontSize = 14.sp
                                            ),
                                            color = MaterialTheme.colors.secondaryVariant
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
                TextsFieldBasic(
                    modifier = Modifier,
                    title = state.telephone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onFocus = { focus.clearFocus() },
                    isError = state.errorTxphone,
                    label = {
                        Text(
                            text = stringResource(id = R.string.phone),
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 14.sp
                            ),
                            color = MaterialTheme.colors.primary
                        )
                    },
                    onEvent = {
                        viewModel.onEvent(AddContactEvents.ChangeTelephone(it))
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            )
            {
                Button(
                    modifier = Modifier
                        .imePadding()
                        .padding(bottom = 20.dp),
                    onClick = {
                        viewModel.onEvent(AddContactEvents.AddContact)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSecondary,
                        disabledBackgroundColor = MaterialTheme.colors.background
                    ), enabled = true
                ) {
                    Text(
                        text = WindowInsets.isImeVisible.toString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}
