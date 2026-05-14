package br.com.fiap.wtccrm.network

import android.util.Log
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

object WebSocketManager {

    private lateinit var stompClient: StompClient

    private var topicDisposable: Disposable? = null
    private var lifecycleDisposable: Disposable? = null

    private var connected = false

    fun connect(
        customerId: String,
        onMessageReceived: (String) -> Unit
    ) {

        try {

            stompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                "ws://10.0.2.2:8080/ws"
            )

            lifecycleDisposable = stompClient.lifecycle()
                .subscribe(

                    { lifecycleEvent ->

                        when (lifecycleEvent.type) {

                            LifecycleEvent.Type.OPENED -> {

                                connected = true

                                Log.d(
                                    "SOCKET",
                                    "WebSocket conectado"
                                )

                                subscribeTopic(
                                    customerId,
                                    onMessageReceived
                                )
                            }

                            LifecycleEvent.Type.ERROR -> {

                                connected = false

                                Log.e(
                                    "SOCKET",
                                    "Erro WebSocket",
                                    lifecycleEvent.exception
                                )
                            }

                            LifecycleEvent.Type.CLOSED -> {

                                connected = false

                                Log.d(
                                    "SOCKET",
                                    "WebSocket fechado"
                                )
                            }

                            else -> {}
                        }
                    },

                    { error ->

                        connected = false

                        Log.e(
                            "SOCKET",
                            "Erro lifecycle",
                            error
                        )
                    }
                )

            stompClient.connect()

        } catch (e: Exception) {

            connected = false

            Log.e(
                "SOCKET_CONNECT",
                e.message ?: "Erro"
            )
        }
    }

    private fun subscribeTopic(
        customerId: String,
        onMessageReceived: (String) -> Unit
    ) {

        try {

            topicDisposable = stompClient
                .topic("/topic/messages/$customerId")
                .subscribe(

                    { topicMessage ->

                        Log.d(
                            "SOCKET_MESSAGE",
                            topicMessage.payload
                        )

                        onMessageReceived(topicMessage.payload)
                    },

                    { error ->

                        Log.e(
                            "SOCKET_TOPIC",
                            "Erro topic",
                            error
                        )
                    }
                )

        } catch (e: Exception) {

            Log.e(
                "SOCKET_SUBSCRIBE",
                e.message ?: "Erro"
            )
        }
    }

    fun disconnect() {

        try {

            topicDisposable?.dispose()
            lifecycleDisposable?.dispose()

            if (::stompClient.isInitialized && connected) {

                stompClient.disconnect()
            }

        } catch (e: Exception) {

            Log.e(
                "SOCKET_DISCONNECT",
                e.message ?: "Erro"
            )
        }
    }
}