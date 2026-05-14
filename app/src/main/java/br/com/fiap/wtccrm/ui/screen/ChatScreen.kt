package br.com.fiap.wtccrm.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtccrm.model.Message
import br.com.fiap.wtccrm.viewmodel.ChatViewModel
import br.com.fiap.wtccrm.network.RetrofitInstance


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ChatScreen(navController: NavController) {

    val vm = ChatViewModel()

    val currentUser =
        RetrofitInstance.currentUserEmail ?: "unknown"

    var text by remember { mutableStateOf("") }

    val messages by vm.messages.collectAsState()

    LaunchedEffect(Unit) {

        val currentUser =
            RetrofitInstance.currentUserEmail ?: return@LaunchedEffect

        vm.connectSocket(currentUser)

        vm.loadInbox(currentUser)
    }

    DisposableEffect(true) {

        onDispose {

            vm.disconnectSocket()
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "WTC CRM",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = RetrofitInstance.currentUserEmail ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },

                actions = {

                    TextButton(

                        onClick = {

                            RetrofitInstance.token = null
                            RetrofitInstance.currentUserEmail = null

                            navController.navigate("login") {

                                popUpTo("chat") {
                                    inclusive = true
                                }
                            }
                        }
                    ) {

                        Text("Sair")
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                reverseLayout = false
            ) {

                items(messages.filter { it.content.isNotBlank() }) { msg ->

                    MessageItem(msg)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {

                        if (text.isNotBlank()) {

                            val targetUser =
                                if (currentUser.contains("admin"))
                                    "cliente@wtc.com"
                                else
                                    "admin@wtc.com"

                            vm.send(
                                Message(
                                    senderId = currentUser,
                                    receiverId = targetUser,
                                    content = text
                                )
                            )

                            text = ""
                        }
                    },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Composable
fun MessageItem(msg: Message) {

    val isMine =
        msg.senderId == RetrofitInstance.currentUserEmail

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (isMine) Arrangement.End else Arrangement.Start
    ) {

        Card(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .widthIn(max = 280.dp),

            colors = CardDefaults.cardColors(
                containerColor =
                    if (isMine)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.LightGray
            )
        ) {

            Text(
                text = msg.content,
                modifier = Modifier.padding(12.dp),
                color =
                    if (isMine)
                        Color.White
                    else
                        Color.Black
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable()
fun ChatScreenPreview() {
    ChatScreen(navController = rememberNavController())
}