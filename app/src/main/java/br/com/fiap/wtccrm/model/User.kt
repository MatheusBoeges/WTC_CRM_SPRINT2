package br.com.fiap.wtccrm.model

data class User(
    val email: String,
    val password: String,
    val role: String = "ROLE_CLIENT"
)