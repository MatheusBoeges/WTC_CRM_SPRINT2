package br.com.fiap.wtccrm.model

data class Customer(
    val id: String? = null,
    val name: String,
    val email: String,
    val tags: List<String> = emptyList(),
    val status: String = "",
    val score: Int = 0
)