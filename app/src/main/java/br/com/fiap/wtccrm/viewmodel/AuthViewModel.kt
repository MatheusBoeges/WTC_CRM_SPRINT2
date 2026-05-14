package br.com.fiap.wtccrm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.wtccrm.model.User
import br.com.fiap.wtccrm.network.RetrofitInstance
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var token: String? = null

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response = RetrofitInstance.api.login(
                    mapOf(
                        "email" to email,
                        "password" to password
                    )
                )

                token = response["token"]

                if (token == null) {
                    onError("Token inválido")
                    return@launch
                }

                RetrofitInstance.token = token

                RetrofitInstance.currentUserEmail = email

                onSuccess()

            } catch (e: Exception) {

                Log.e("LOGIN_ERROR", e.message ?: "Erro")

                onError(
                    e.message ?: "Erro ao fazer login"
                )
            }
        }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                RetrofitInstance.api.register(
                    User(
                        email = email,
                        password = password
                    )
                )

                onSuccess()

            } catch (e: Exception) {

                Log.e("REGISTER_ERROR", e.message ?: "Erro")

                onError(
                    e.message ?: "Erro ao registrar"
                )
            }
        }
    }
}