package br.com.fiap.wtccrm.model

data class Message(

    val id: String? = null,
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val status: String? = null,
    val timestamp: String? = null
)