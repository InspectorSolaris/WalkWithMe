package com.example.walkwithme.retrofit.helloworld

import com.example.walkwithme.retrofit.helloworld.HelloWorldResponse
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {

    @GET("/api/helloworld")
    fun getHelloWorld(): Call<ArrayList<String>>

    @GET("/api/helloworld/{id}")
    fun getHelloWorldId(@Path("id") id: Int): Call<HelloWorldResponse>

    @POST("/api/helloworld/")
    fun postHelloWorld(@Body value: String): Call<HelloWorldResponse>

    @PUT("/api/helloworld/{id}")
    fun putHelloWorld(@Path("id") id: Int, @Body value: String): Call<HelloWorldResponse>

    @DELETE("/api/helloworld/{id}")
    fun deleteHelloWorld(@Path("id") id: Int): Call<HelloWorldResponse>

}