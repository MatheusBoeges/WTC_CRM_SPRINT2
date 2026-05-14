package br.com.fiap.wtccrm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.wtccrm.model.Message
import br.com.fiap.wtccrm.network.RetrofitInstance
import br.com.fiap.wtccrm.network.WebSocketManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val gson = Gson()

    private val _messages =
        MutableStateFlow<List<Message>>(emptyList())

    val messages: StateFlow<List<Message>> = _messages

    fun connectSocket(customerId: String) {

        try {

            WebSocketManager.connect(customerId) { payload ->

                try {

                    Log.d("SOCKET_PAYLOAD", payload)

                    val message = gson.fromJson(
                        payload,
                        Message::class.java
                    )

                    _messages.value =
                        (_messages.value + message)
                            .distinctBy { it.id }

                } catch (e: Exception) {

                    Log.e(
                        "SOCKET_PARSE_ERROR",
                        e.message ?: "Erro parse"
                    )
                }
            }

        } catch (e: Exception) {

            Log.e(
                "SOCKET_CONNECT_ERROR",
                e.message ?: "Erro socket"
            )
        }
    }

    fun disconnectSocket() {
        WebSocketManager.disconnect()
    }

    fun loadInbox(customerId: String) {

        viewModelScope.launch {

            try {

                _messages.value =
                    RetrofitInstance.api.inbox(customerId)

            } catch (e: Exception) {

                Log.e(
                    "INBOX_ERROR",
                    e.message ?: "Erro inbox"
                )
            }
        }
    }

    fun send(message: Message) {

        // ✅ adiciona instantaneamente na tela
        _messages.value =
            _messages.value + message

        viewModelScope.launch {

            try {

                RetrofitInstance.api.sendMessage(message)

            } catch (e: Exception) {

                Log.e(
                    "SEND_ERROR",
                    e.message ?: "Erro enviar"
                )
            }
        }
    }
}