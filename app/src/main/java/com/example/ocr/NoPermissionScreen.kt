package com.example.ocr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NoPermissionScreen(
    onReqestPermission: () -> Unit
){
    NoPermissionContent(onReqestPermission = onReqestPermission)
}

@Composable
fun NoPermissionContent(onReqestPermission: () -> Unit){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ){
            Text(
                text="Please grant The permission",
                fontSize = 20.sp
            )
            Button(onClick = onReqestPermission){
                Icon(painter = painterResource(id = R.drawable.camshutter), contentDescription = "Permission Button", modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp))
                Text(text = "Grant Permission",
                    fontSize = 20.sp)
            }
        }
    }
}