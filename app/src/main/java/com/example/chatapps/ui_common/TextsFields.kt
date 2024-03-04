package com.example.chatapps.ui_common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.chatapps.R
import com.example.chatapps.components.createUser.presentation.createProfile.ProfileEvents

@Composable
fun TextsFieldBasic(
    modifier : Modifier,
    title : String,
    placeholder: String,
    focus : FocusManager,
    onEvent: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = {
            onEvent(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colors.secondary
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.background,
            unfocusedBorderColor = MaterialTheme.colors.secondary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focus.clearFocus()
        })
    )
}