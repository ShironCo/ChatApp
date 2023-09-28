package com.example.chatapps.createUser.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chatapps.R
import com.example.chatapps.ui.theme.Roboto
import com.example.chatapps.ui_common.CirculeProgress

@Composable
fun UserScreen(
    navHostController: NavHostController,
    code: String?,
    flag: String?,
    viewModel: CreateUserViewModel = hiltViewModel()
) {
    UserContent(viewModel, navHostController, code, flag)
}

@Composable
fun UserContent(
    viewModel: CreateUserViewModel,
    navHostController: NavHostController,
    code: String?,
    flag: String?
) {
    val state = viewModel.state
    val focus = LocalFocusManager.current
    val context = LocalContext.current
    BackHandler(
        enabled = state.sendCodeToggle
    ) {
        viewModel.onEvent(CreateUserEvents.CodeToggle)
    }
    if (state.code.length == 6 && !state.ProgressToggle){
        focus.clearFocus()
        viewModel.onEvent(CreateUserEvents.VerifyCredential(context, navHostController))
    }
    if (state.dialogToggle) {
        DialogConfirmPhone("$code ${state.phone}") {
            viewModel.onEvent(it)
        }
    }
    if (state.ProgressToggle){
        CirculeProgress(text = "Cargando"){}
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 30.dp)
    ) {
        TextInformation()
        Spacer(modifier = Modifier.height(40.dp))
        ButtonSelectCountry(flag) {
            viewModel.onEvent(CreateUserEvents.NavSelectCountry(navHostController))
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldNumber(
            state, code
        ) {
            viewModel.onEvent(it)
        }

        if (state.phoneError){
            Box{
                Text(
                    text = stringResource(id = R.string.invaliPhone),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.start),
            contentDescription = null,
            modifier = Modifier.size(270.dp)
        )
        ButtonScreenMain(R.string.nextButton, state){
            viewModel.onEvent(CreateUserEvents.ValidatePhone)
        }
    }
    AnimatedVisibility(
        visible = state.sendCodeToggle,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        if (code != null) {
            SendSms(
                codeFlag = code,
                state = state
            ) {
                viewModel.onEvent(it)
            }
        }
    }
}

@Composable
fun TextInformation() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
        )
        Divider(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            color = MaterialTheme.colors.secondary
        )
        Text(
            text = stringResource(id = R.string.instruction),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.instructionVerify),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
        )
    }
}

@Composable
fun ButtonScreenMain(
    title: Int,
    state: CreateUserStates,
    onEvent: () -> Unit
){
    Button(
        onClick = {
            onEvent()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onSecondary,
            disabledBackgroundColor = MaterialTheme.colors.background
        ), enabled = state.phone.length == 10
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.background
        )
    }
}

@Composable
fun ButtonSelectCountry(
    flag: String?,
    onEvent: () -> Unit
) {
    Button(
        onClick = { onEvent() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!flag.isNullOrBlank()) {
                Text(text = flag)
            } else {
                Text(
                    text = stringResource(id = R.string.selectCountry),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.ExpandMore, contentDescription = null,
                tint = MaterialTheme.colors.background, modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TextFieldNumber(
    states: CreateUserStates,
    code: String?,
    onEvent: (CreateUserEvents) -> Unit
) {
    val focus = LocalFocusManager.current
    if (states.phone.length == 10 && !states.sendCodeToggle) {
        focus.clearFocus()
    }
    TextField(
        value = states.phone, onValueChange = {
            onEvent(CreateUserEvents.ChangePhone(it))
        },
        textStyle = MaterialTheme.typography.body2.copy(
            fontWeight = FontWeight.Normal
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.secondaryVariant,
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colors.secondary
        ),
        placeholder = {
            Text(
                text = "número",
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colors.secondary
            )
        },
        leadingIcon = {
            Text(
                text = code ?: "+",
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colors.secondaryVariant
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            focus.clearFocus()
        }),
        enabled = (code != null && code.length > 1) && !states.sendCodeToggle
    )
}

@Composable
fun DialogConfirmPhone(
    phone: String,
    onEvent: (CreateUserEvents) -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { onEvent(CreateUserEvents.DialogToggle) },
        title = {
            Text(
                text = stringResource(id = R.string.titleDialogPhone),
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.secondaryVariant
            )
        },
        text = {
            Column() {
                Text(
                    text = phone,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(id = R.string.textDialogPhone),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onEvent(CreateUserEvents.DialogToggle)
            }) {
                Text(
                    text = stringResource(id = R.string.edit),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onEvent(CreateUserEvents.VerifyPhone(context, phone.replace("\\s".toRegex(), "")))
                onEvent(CreateUserEvents.DialogToggle)
            }) {
                Text(
                    text = stringResource(id = R.string.continu),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun SendSms(
    codeFlag: String?,
    state: CreateUserStates,
    onEvent: (CreateUserEvents) -> Unit
) {
    val phone = state.phone
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            IconButton(onClick = {
                onEvent(CreateUserEvents.CodeToggle)
                onEvent(CreateUserEvents.ChangeCode(""))
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.code),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.titleCode),
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Text(
                        text =
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            ) {
                                append(stringResource(id = R.string.bodyCode))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append(" $codeFlag $phone")
                            }
                        },
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextFieldsCodes(state = state)
                }
            }
        }
    }
}

@Composable
fun TextFieldsCodes(
    state: CreateUserStates,
    viewModel: CreateUserViewModel = hiltViewModel()
){
    val focus = LocalFocusManager.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (i in 0..5){
            TextFieldCode(
                ucode = if (state.code.length>i) state.code[i].toString() else "",
                focus = focus
            ){
                viewModel.onEvent(CreateUserEvents.ChangeCode(it))
            }
        }
    }
}

@Composable
fun TextFieldCode(
    ucode : String = "",
    focus : FocusManager,
    onEvent: (String) -> Unit
){
    TextField(
        value = ucode,
        onValueChange = {
            if (it.length == 1) {
                onEvent(it)
                focus.moveFocus(focusDirection = FocusDirection.Next)
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondary,
            textColor = MaterialTheme.colors.primary
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number),
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.body1,
        modifier = Modifier.width(45.dp),
        enabled = ucode.length != 1
    )
}