package br.com.fiap.wtccrm.network

import br.com.fiap.wtccrm.model.Customer
import br.com.fiap.wtccrm.model.Message
import br.com.fiap.wtccrm.model.User
import retrofit2.http.*

interface ApiService {

    // 🔐 LOGIN
    @POST("auth/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): Map<String, String>

    // 📝 REGISTER
    @POST("auth/register")
    suspend fun register(
        @Body user: User
    ): User

    // 👥 CUSTOMERS
    @GET("customers")
    suspend fun getCustomers(): List<Customer>

    // 💬 SEND MESSAGE
    @POST("messages")
    suspend fun sendMessage(
        @Body message: Message
    ): Message

    // 📥 INBOX
    @GET("messages/inbox/{id}")
    suspend fun inbox(
        @Path("id") id: String
    ): List<Message>
}