package br.com.fiap.wtccrm.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    var token: String? = null
    var currentUserEmail: String? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->

            val requestBuilder = chain.request()
                .newBuilder()

            token?.let {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $it"
                )
            }

            chain.proceed(requestBuilder.build())
        })
        .build()

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl("http://SEU_IP:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}