package com.example.chatapps.components.createUser.presentation.mainChat.addContact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapps.R
import com.example.chatapps.createUser.presentation.selectCountry.Country
import com.example.chatapps.ui_common.TextsFieldBasic

@Composable
fun AddContactScreen() {
    Scaffold(
        topBar = {
            AddContactTopbar()
        }
    ) {
        AddContactBody(modifier = Modifier.padding(it))
    }
}

@Composable
fun AddContactTopbar() {
    TopAppBar(
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        IconButton(onClick = {}) {
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

@Composable
fun AddContactBody(
    modifier: Modifier,
    viewModel: AddContactViewModel = hiltViewModel()
) {
    val listCountry = Country.values()
    val focus = LocalFocusManager.current
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
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
                tint = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            TextsFieldBasic(
                modifier = Modifier,
                title = state.name,
                focus = focus,
                placeholder = stringResource(
                    id = R.string.name
                )
            ) {
                viewModel.onEvent(AddContactEvents.ChangeName(it))
            }
        }
        TextsFieldBasic(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 30.dp),
            title = "",
            focus = focus,
            placeholder = stringResource(
                id = R.string.lastName
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
                tint = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = stringResource(id = R.string.country),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colors.secondary
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
                    IconButton(onClick = { viewModel.onEvent(AddContactEvents.ChangeExpanded(true)) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Seleccionar país",
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(MaterialTheme.colors.secondary),
                        expanded = state.expanded,
                        onDismissRequest = { viewModel.onEvent(AddContactEvents.ChangeExpanded(false)) }) {
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
        }
    }
}
