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
    modifier : Modifier = Modifier,
    title : String,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions,
    onFocus : () -> Unit,
    label : @Composable (() -> Unit)? = null,
    isError : Boolean = false,
    onEvent: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = {
            onEvent(it)
        },
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.background,
            unfocusedIndicatorColor = MaterialTheme.colors.primary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        label = label,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body1,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
           onFocus()
        })
    )
}