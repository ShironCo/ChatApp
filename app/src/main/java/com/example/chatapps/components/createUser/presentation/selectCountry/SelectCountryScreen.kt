package com.example.chatapps.components.createUser.presentation.selectCountry

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chatapps.R
import com.example.chatapps.components.createUser.presentation.CreateUserEvents
import com.example.chatapps.components.createUser.presentation.CreateUserViewModel
import com.example.chatapps.createUser.presentation.selectCountry.Country


@Composable
fun SelectCountryApp(
    navHostController: NavHostController,
    viewModel: CreateUserViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            SelectCountryTopBar {
                viewModel.onEvent(CreateUserEvents.NavBack(navHostController))
            }
        }
    ) {
        SelectCountryContent(modifier = Modifier.padding(it)){country ->
            viewModel.onEvent(CreateUserEvents.NavBackCountry(
                navHostController, country.code, country.flag
            ))
        }
    }
    BackHandler() {
        viewModel.onEvent(CreateUserEvents.NavBack(navHostController))
    }
}

@Composable
fun SelectCountryTopBar(
    onEvent: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
                IconButton(onClick = {
                    onEvent()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
                Text(
                    text = stringResource(id = R.string.selectCountry),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
    }
}


@Composable
fun SelectCountryContent(modifier: Modifier, onEvent: (Country) -> Unit) {
    val listCountry = Country.values()
   LazyColumnSelect(country = listCountry){
       onEvent(it)
   }

}

@Composable
fun LazyColumnSelect(
    country: Array<Country>,
    onEvent: (Country) -> Unit
){
    LazyColumn(
    ){
        items(country){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 5.dp)
                    .clickable {
                        onEvent(it)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(text = it.flag)
                    Spacer(modifier = Modifier.width(10.dp))
                    TextSelect(text = it.name)
                }
                TextSelect(text = it.code)
            }
            Divider(
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun TextSelect(
    text : String
){
    Text(text = text,
        style = MaterialTheme.typography.subtitle1.copy(
            fontSize = 16.sp
        ),
        color = MaterialTheme.colors.secondaryVariant
    )
}
